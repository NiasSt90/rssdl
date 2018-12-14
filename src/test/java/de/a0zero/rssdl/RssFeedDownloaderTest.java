package de.a0zero.rssdl;

import de.a0zero.rssdl.download.OkHttpDownloader;
import de.a0zero.rssdl.junkies.JunkiesClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import java.net.URL;


/**
 * User: Markus Schulz <msc@0zero.de>
 */
@EnabledIfSystemProperty(named = "username", matches = ".+")
class RssFeedDownloaderTest {

	private String username;

	private String password;


	@BeforeEach
	void setUp() {
		username = System.getProperty("username");
		password = System.getProperty("password");
	}


	@Test
	void parse() throws Exception {
		MainArguments arguments = new MainArguments();
		SetDuplicateCheck duplicateCheck = new LocalFileDupCheck("target/dups.properties");
		final JunkiesAPI api = new JunkiesClient().api(arguments.djJunkiesURL);
		api.login(username, password).blockingFirst();
		RssFeedDownloader downloader = new RssFeedDownloader(arguments, api, new OkHttpDownloader(arguments), duplicateCheck);
		//downloader.parse(new URL("file:src/test/resources/eelke.xml"), 1);
		downloader.parse(new URL("http://hearthis.at/globalsets/podcast/"), 1);
	}
}