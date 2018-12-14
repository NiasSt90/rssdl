package de.a0zero.rssdl;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * User: Markus Schulz <msc@0zero.de>
 */
public class MainArguments {

	@Parameter(names = {"-log", "-verbose" }, description = "Level of verbosity")
	public Integer verbose = 1;


	@Parameter(names = "-e", description = "Endpunkt URL für DJ-Junkies")
	public String djJunkiesURL = "https://test.dj-junkies.de/";

	@Parameter(names = "-u", description = "Login DJ-Junkies")
	public String username;

	@Parameter(names = "-p", description = "Passwort DJ-Junkies", password = false)
	public String password;

	@Parameter(names = "-d", description = "Pfad zur DuplicateCheck-DB")
	public String duplicateDB = "dups.properties";

	@Parameter(names = "-t", description = "Download Pfad wo die MP3/M4A abgelegt werden. ($target/$nid/$file).")
	public String djJunkiesDownloadPath = "./";

	@Parameter(names = "--parallel", description = "Anzahl an Threads die parallel Downloaded dürfen.")
	public int downloadParallel = 1;



	@Parameter(names = "-l", description = "Anzahl an Einträgen pro Feed die verarbeitet werden dürfen.")
	public int limitEntriesPerFeed = 1;

	@Parameter(description = "Liste der RSS Feed URLs die verarbeitet werden sollen....")
	public List<String> rssFeedURLs = new ArrayList<>();
}
