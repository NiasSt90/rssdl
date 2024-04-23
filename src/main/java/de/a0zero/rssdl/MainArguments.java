package de.a0zero.rssdl;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.ISO8601DateConverter;
import okhttp3.logging.HttpLoggingInterceptor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * User: Markus Schulz <msc@0zero.de>
 */
public class MainArguments {

	@Parameter(names = {"-log", "-verbose" }, description = "enable REST call logger", converter = HttpLoggingEnumConverter.class)
	public static HttpLoggingInterceptor.Level verbose = HttpLoggingInterceptor.Level.NONE;

	@Parameter(names = {"-q", "--quiet"}, description = "be quiet, no DEBUG logging and no progressBar for downloads")
	public static boolean quiet = false;

	@Parameter(names = {"-e", "--endpoint"}, description = "Endpunkt URL für DJ-Junkies")
	public String djJunkiesURL = "https://test.dj-junkies.de/";

	@Parameter(names = {"-u", "--username"}, description = "Login DJ-Junkies")
	public String username;

	@Parameter(names = {"-p", "--password"}, description = "Passwort DJ-Junkies", password = false)
	public String password;

	@Parameter(names = {"-d", "--duplicate-db"}, description = "Pfad zur DuplicateCheck-DB")
	public String duplicateDB = "dups.properties";

	@Parameter(names = {"-t", "--target-path"}, description = "Download Pfad wo die MP3/M4A abgelegt werden. ($target/$nid/$file).")
	public String djJunkiesDownloadPath = "./";

	@Parameter(names = "--parallel", description = "Anzahl an Threads die parallel Downloaded dürfen.")
	public int downloadParallel = 1;

	@Parameter(names = "--not-before", description = "Skip RSS-Items published before this date", converter = ISO8601DateConverter.class)
	public Date publishedNotBefore;

	@Parameter(names = {"-l", "--limit"}, description = "Anzahl an Einträgen pro Feed die verarbeitet werden dürfen.")
	public int limitEntriesPerFeed = Integer.MAX_VALUE;

	@Parameter(names = {"-m", "--min-duration"}, description = "ignoriere RSS-Items mit Laufzeit kleiner als (in Minuten)")
	public int minDuration = 0;

	@Parameter(names = {"--allow-linkgrabbing"}, description = "Erlaube das Grabbing von .mp3 Links in referenzierten HTML Seiten.")
	public boolean allowLinkGrabbing = false;

	@Parameter(description = "Liste der RSS Feed URLs die verarbeitet werden sollen....")
	public List<String> rssFeedURLs = new ArrayList<>();

	@Parameter(names = "--dry-run", description = "Dry-Run, kein Download der Sets sondern nur Log-Ausgaben erzeugen....")
	public boolean dryRun = false;

	private static class HttpLoggingEnumConverter implements IStringConverter<HttpLoggingInterceptor.Level> {

		@Override
		public HttpLoggingInterceptor.Level convert(String s) {
			return HttpLoggingInterceptor.Level.valueOf(s.toUpperCase());
		}
	}
}
