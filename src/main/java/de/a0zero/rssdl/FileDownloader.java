package de.a0zero.rssdl;

import com.rometools.rome.feed.synd.SyndEnclosure;
import com.rometools.rome.feed.synd.SyndEntry;

import java.io.File;
import java.io.IOException;
import java.net.URL;


/**
 * User: Markus Schulz <msc@0zero.de>
 */
public interface FileDownloader {

	void downloadSet(int nodeID, SyndEntry entry, SyndEnclosure file);

	File download(URL url, String type) throws IOException;
}
