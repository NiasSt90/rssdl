package de.a0zero.rssdl.dto;

import com.google.gson.annotations.SerializedName;

import java.net.URL;

/**
 * User: Markus Schulz <msc@0zero.de>
 *    Teilergebnis von {@link JsonSetNodeTracks}
 */
public class JsonSetNodeTrack {

	@SerializedName("url")
	private URL url;

	@SerializedName("name")
	private String filename;

	public URL getUrl() {
		return url;
	}

	public String getFilename() {
		return filename;
	}
}
