package de.a0zero.rssdl.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * User: Markus Schulz <msc@0zero.de>
 */
public class JsonCommentResult implements Serializable {

	@SerializedName("cid")
	private Long cid;

	@SerializedName("uri")
	private String uri;

	public Long getCid() {
		return cid;
	}

	public String getUri() {
		return uri;
	}
}
