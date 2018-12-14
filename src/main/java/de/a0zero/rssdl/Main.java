package de.a0zero.rssdl;

import com.beust.jcommander.JCommander;
import de.a0zero.rssdl.download.OkHttpDownloader;
import de.a0zero.rssdl.dto.JsonLoginResult;
import de.a0zero.rssdl.junkies.JunkiesClient;

import java.net.URL;


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

		JunkiesAPI api = new JunkiesClient().api(myArgs.djJunkiesURL);
		JsonLoginResult jsonLoginResult = api.login(myArgs.username, myArgs.password).blockingFirst();
		System.out.println("Successful logged in " + jsonLoginResult.getSessionName());

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
