package de.a0zero.rssdl;

import de.a0zero.rssdl.download.OkHttpDownloader;

import java.net.MalformedURLException;
import java.net.URL;


/**
 * User: Markus Schulz <msc@0zero.de>
 */
public class Main {

	public static void main(String [ ] args) throws Exception {

		if (args.length < 3) {
			System.out.println("Usage: $URL $LOGIN $PASS");
			return;
		}
		int count = 1;
		if (args.length > 3) {
			count = Integer.parseInt(args[3]);
		}
		SetDuplicateCheck duplicateCheck = new LocalFileDupCheck("dups.properties");
		final JunkiesAPI api = new JunkiesClient().api();
		System.out.println("Logged in " + api.login(args[1], args[2]).blockingFirst().getSessionName());
		RssFeedDownloader downloader = new RssFeedDownloader(api, new OkHttpDownloader(), duplicateCheck);
		downloader.parse(new URL(args[0]), count);
	}


}
