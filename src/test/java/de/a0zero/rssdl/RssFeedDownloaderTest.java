package de.a0zero.rssdl;

import de.a0zero.rssdl.download.OkHttpDownloader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import retrofit2.Retrofit;

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
		SetDuplicateCheck duplicateCheck = new LocalFileDupCheck("target/dups.properties");
		final JunkiesAPI api = new JunkiesClient().api();
		api.login(username, password).blockingFirst();
		RssFeedDownloader downloader = new RssFeedDownloader(api, new OkHttpDownloader(), duplicateCheck);
		//downloader.parse(new URL("file:src/test/resources/eelke.xml"), 1);
		downloader.parse(new URL("http://hearthis.at/globalsets/podcast/"), 1);
	}
}