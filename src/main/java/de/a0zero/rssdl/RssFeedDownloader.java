package de.a0zero.rssdl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.rometools.modules.itunes.EntryInformation;
import com.rometools.rome.feed.module.Module;
import com.rometools.rome.feed.synd.SyndEnclosure;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import de.a0zero.rssdl.dto.OldJsonDJ;
import de.a0zero.rssdl.junkies.create.CreateSetNode;
import de.a0zero.rssdl.junkies.create.CreateSetNodeResult;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


/**
 * User: Markus Schulz <msc@0zero.de>
 */
public class RssFeedDownloader {

	private static final Logger log = Logger.getLogger(RssFeedDownloader.class.getName());

	public static final String ITUNES_DTD = "http://www.itunes.com/dtds/podcast-1.0.dtd";

	private final MainArguments arguments;

	private final FileDownloader downloader;

	private final JunkiesAPI api;

	private final SetDuplicateCheck duplicateCheck;

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

		log.log(Level.INFO, () -> String.format("Start Handling %d entries from RSS Feed from %s (%s)", limit, syndfeed.getTitle(), url.toString()));
		Flowable.fromIterable(syndfeed.getEntries())
				.filter(e -> arguments.publishedNotBefore == null || arguments.publishedNotBefore.before(e.getPublishedDate()))
				.limit(limit)
				.parallel(arguments.downloadParallel)
				.runOn(Schedulers.io())
				.doOnNext(this::handleEntry)
				.sequential()
				.blockingSubscribe();
		log.log(Level.INFO, () -> String.format("Finished %d entries from RSS Feed %s",  limit, url.toString()));
	}

	private void handleEntry(SyndEntry entry) throws IOException {
		final List<SyndEnclosure> audioFiles = entry.getEnclosures().stream()
				.filter(e -> "audio/x-m4a".equals(e.getType()) || "audio/mpeg".equals(e.getType()))
				.collect(Collectors.toList());
		if (audioFiles.size() > 0) {
			// (0) Create new Node
			final int nid = createNode(entry);

			//(1) Download
			//TODO: RX-Stream und Retry wenn "Caused by: java.net.SocketException: Socket closed"
			audioFiles.forEach(file -> downloader.downloadSet(nid, entry, file));

			//(2) Force Trackinfo Update on site...
			api.forcemp3info(nid).execute();

			//(3) Publish the new Set now...
			publishNode(nid);
		}
		else {
			log.log(Level.WARNING, () -> String.format("RSS-Item %s hat keine Audio-Dateien!", entry.getUri()));
		}
	}


	private int createNode(SyndEntry entry) throws IOException {
		final Integer nodeId = duplicateCheck.getNodeId(entry.getUri());
		if (nodeId != null) return nodeId;

		CreateSetNode node = new CreateSetNode();
		node.status = 0;
		node.title = entry.getTitle();
		node.created = new Date();
		node.setSetCreated(entry.getPublishedDate());
		final Module module = entry.getModule(ITUNES_DTD);
		EntryInformation entryInformation = (EntryInformation) module;
		if (entryInformation != null && entryInformation.getAuthor() != null) {
			node.artistnames = entryInformation.getAuthor();
			node.title = entryInformation.getAuthor() + " - " + node.title;
			node.fulltitle = node.title;
		}
		if (node.artistnames != null) {
			final List<OldJsonDJ> oldJsonDJS = api.findDJ(node.artistnames).blockingFirst();
			if (oldJsonDJS.size() == 0 || oldJsonDJS.size() > 1) {
				node.artistnames = null;
			}
		}
		final CreateSetNodeResult body = api.createSet(node).execute().body();
		final int nid = body.nid;
		duplicateCheck.addEntry(entry.getUri(), entry.getTitle(), nid);
		log.log(Level.INFO, () -> String.format("Created Set %d title = %s, url = %s", nid, node.title, body.url));
		return nid;
	}


	private void publishNode(int nid) throws IOException {
		final Response<JsonObject> rawLoad = api.rawLoad(nid).execute();
		final JsonObject rawSet = rawLoad.body();
		if ("0".equals(rawSet.get("status").getAsString())) {
			final JsonElement trackinfo = rawSet.get("trackinfo");
			if (trackinfo instanceof JsonArray && ((JsonArray) trackinfo).size() > 0) {
				rawSet.add("status", new JsonPrimitive(1));
				final Response<JsonObject> rawUpdate = api.rawUpdate(nid, rawSet).execute();
				if (!rawUpdate.isSuccessful()) {
					log.log(Level.SEVERE,
							() -> String.format("Set %d konnte nicht veröffentlicht werden! Http-Status=%d", nid, rawUpdate.code()));
				}
			}
			else {
				log.log(Level.WARNING, () -> String.format("Set %d hat keine Tracks und wird daher nicht aktiviert!", nid));
			}
		}
	}
}
