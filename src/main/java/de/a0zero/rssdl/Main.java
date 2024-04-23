package de.a0zero.rssdl;

import com.beust.jcommander.JCommander;
import de.a0zero.rssdl.download.OkHttpDownloader;
import de.a0zero.rssdl.junkies.JunkiesClient;
import io.reactivex.Observable;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * User: Markus Schulz <msc@0zero.de>
 */
public class Main {

	private static final org.slf4j.Logger log = LoggerFactory.getLogger(Main.class);


	public static void main(String [ ] args) throws Exception {

		MainArguments myArgs = new MainArguments();
		JCommander jCommander = JCommander.newBuilder().addObject(myArgs).build();
		jCommander.parse(args);

		if (myArgs.rssFeedURLs == null || myArgs.rssFeedURLs.isEmpty()) {
			jCommander.usage();
			return;
		}
		Observable.fromArray(Logger.getLogger( "" ).getHandlers())
				.doOnNext(h -> h.setLevel(MainArguments.quiet ? Level.INFO : Level.FINE))
				.blockingSubscribe();

		JunkiesAPI api = new JunkiesClient().api(myArgs.djJunkiesURL);
		api.login(myArgs.username, myArgs.password).blockingFirst();

		SetDuplicateCheck duplicateCheck = new LocalFileDupCheck(myArgs.duplicateDB);
		RssFeedDownloader downloader = new RssFeedDownloader(myArgs, api, new OkHttpDownloader(myArgs), duplicateCheck);
		for (String url : myArgs.rssFeedURLs) {
			try {
				downloader.parse(new URL(url), myArgs.limitEntriesPerFeed);
				api.login(myArgs.username, myArgs.password).blockingFirst();//Cookie-Session is not long enough...
			}
			catch (Exception e) {
				log.error("Error on rss-feed " + url + ": " + e.getMessage(), e);
			}
		}
	}


	static {
		System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$4.4s] %5$s %n");
	}
}
