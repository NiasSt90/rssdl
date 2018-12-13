package de.a0zero.rssdl.dto;

import com.google.gson.annotations.SerializedName;

/**
 * User: Markus Schulz <msc@0zero.de>
 */
public class JSonSimilarArtists {

	@SerializedName("nid")
	int nid;

	@SerializedName("match")
	float match;

	public int getNid() {
		return nid;
	}

	public float getMatch() {
		return match;
	}
}
