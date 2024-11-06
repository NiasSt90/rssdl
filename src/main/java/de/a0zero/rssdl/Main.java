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

		SetDuplicateCheck duplicateCheck = new LocalFileDupCheck(myArgs.duplicateDB);

		for (String url : myArgs.rssFeedURLs) {
			try {
				log.info("START FEED " + url);
				//WORKAROUND: da es Probleme mit der Laufzeit der PHP-Session (Cookie basiert) gibt wird pro Feed der Client/Downloader neu erstellt....
				JunkiesAPI api = new JunkiesClient().api(myArgs.djJunkiesURL);
				api.login(myArgs.username, myArgs.password).blockingFirst();
				RssFeedDownloader downloader = new RssFeedDownloader(myArgs, api, new OkHttpDownloader(myArgs), duplicateCheck);
				downloader.parse(new URL(url), myArgs.limitEntriesPerFeed);
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
