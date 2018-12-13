package de.a0zero.rssdl.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * User: Markus Schulz <msc@0zero.de>
 */
public class JsonLastFmImage implements Serializable {

	@SerializedName("#text")
	private String url;

	@SerializedName("size")
	private String size;

	public String getUrl() {
		return url;
	}

	public String getSize() {
		return size;
	}
}
