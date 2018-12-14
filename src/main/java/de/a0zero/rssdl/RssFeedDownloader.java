package de.a0zero.rssdl;

import com.rometools.modules.itunes.EntryInformation;
import com.rometools.rome.feed.module.Module;
import com.rometools.rome.feed.synd.SyndEnclosure;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import de.a0zero.rssdl.dto.JsonDJ;
import de.a0zero.rssdl.dto.JsonSetNode;
import de.a0zero.rssdl.dto.JsonSetNodeTrack;
import de.a0zero.rssdl.dto.JsonTrackinfo;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * User: Markus Schulz <msc@0zero.de>
 */
public class RssFeedDownloader {

	public static final String ITUNES_DTD = "http://www.itunes.com/dtds/podcast-1.0.dtd";

	private final MainArguments arguments;

	private final FileDownloader downloader;

	private final JunkiesAPI api;

	private final SetDuplicateCheck duplicateCheck;

	private boolean allowCreate = true;


	public RssFeedDownloader(MainArguments arguments, JunkiesAPI api, FileDownloader downloader, SetDuplicateCheck duplicateCheck) {
		this.arguments = arguments;
		this.api = api;
		this.downloader = downloader;
		this.duplicateCheck = duplicateCheck;
	}


	public void parse(URL url, int limit) throws IOException, FeedException {

		final File download = downloader.download(url, ".xml");
		SyndFeedInput input = new SyndFeedInput();
		SyndFeed syndfeed = input.build(new XmlReader(download));

		System.out.println(String.format("Start Handling %d entries from RSS Feed from %s (%s)", limit, syndfeed.getTitle(), url.toString()));
		Flowable.fromIterable(syndfeed.getEntries())
				.limit(limit)
				.parallel(arguments.downloadParallel)
				.runOn(Schedulers.io())
				.doOnNext(this::handleEntry)
				.sequential()
				.blockingSubscribe();
		System.out.println(String.format("Finished first %d entries from RSS Feed %s",  limit, url.toString()));
	}

	private void handleEntry(SyndEntry entry) throws IOException {
		JsonSetNode node = new JsonSetNode(0);
		node.setStatus(0);//inaktiver Knoten
		node.setTitle(entry.getTitle());
		node.setCreated(new Date());
		node.setSetcreated(entry.getPublishedDate());
		final Module module = entry.getModule(ITUNES_DTD);
		EntryInformation entryInformation = (EntryInformation) module;
		if (entryInformation != null && entryInformation.getDuration() != null) {
			node.setDuration((int) (entryInformation.getDuration().getMilliseconds() / 1000));
		}
		if (entryInformation != null && entryInformation.getAuthor() != null) {
			node.setDj(new JsonDJ(0, entryInformation.getAuthor()));
		}
		final int nid = createNode(entry, node);

		final List<SyndEnclosure> audioFiles = entry.getEnclosures().stream()
				.filter(e -> "audio/x-m4a".equals(e.getType()) || "audio/mpeg".equals(e.getType()))
				.collect(Collectors.toList());
		if (audioFiles.size() > 0) {
			audioFiles.forEach(file -> downloader.downloadSet(nid, entry, file));
			api.forcemp3info(nid).execute();
		}
	}


	private int createNode(SyndEntry entry, JsonSetNode node) throws IOException {
		if (!allowCreate) return 0;
		final Integer nodeId = duplicateCheck.getNodeId(entry.getUri());
		if (nodeId != null) return nodeId;
		final JsonSetNode body = api.createSet(node).execute().body();
		final int nid = body.getNid();
		duplicateCheck.addEntry(entry.getUri(), entry.getTitle(), nid);
		System.out.println(String.format("Set %d created for %s", nid, node.getTitle()));
		return nid;
	}
}
