package de.a0zero.rssdl;

import com.beust.jcommander.JCommander;
import de.a0zero.rssdl.download.OkHttpDownloader;
import de.a0zero.rssdl.dto.JsonLoginResult;
import de.a0zero.rssdl.junkies.JunkiesClient;
import io.reactivex.Observable;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * User: Markus Schulz <msc@0zero.de>
 */
public class Main {

	public static void main(String [ ] args) throws Exception {

		MainArguments myArgs = new MainArguments();
		JCommander jCommander = JCommander.newBuilder().addObject(myArgs).build();
		jCommander.parse(args);

		if (myArgs.rssFeedURLs == null || myArgs.rssFeedURLs.isEmpty()) {
			jCommander.usage();
			return;
		}
		Observable.fromArray(Logger.getLogger( "" ).getHandlers())
				.doOnNext(h -> h.setLevel(MainArguments.quiet ? Level.WARNING : Level.INFO))
				.blockingSubscribe();

		JunkiesAPI api = new JunkiesClient().api(myArgs.djJunkiesURL);
		api.login(myArgs.username, myArgs.password).blockingFirst();

		SetDuplicateCheck duplicateCheck = new LocalFileDupCheck(myArgs.duplicateDB);
		RssFeedDownloader downloader = new RssFeedDownloader(myArgs, api, new OkHttpDownloader(myArgs), duplicateCheck);
		for (String url : myArgs.rssFeedURLs) {
			downloader.parse(new URL(url), myArgs.limitEntriesPerFeed);
		}
	}


	static {
		System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$4.4s] %5$s %n");
/*
		InputStream stream = Main.class.getClassLoader().getResourceAsStream("logging.properties");
		try {
			LogManager.getLogManager().readConfiguration(stream);

		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
}
