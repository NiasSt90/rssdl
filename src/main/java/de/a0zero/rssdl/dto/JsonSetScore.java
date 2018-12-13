package de.a0zero.rssdl.dto;

import com.google.gson.annotations.SerializedName;

/**
 * User: Markus Schulz <msc@0zero.de>
 */
public class JsonSetScore {

	@SerializedName("nid")
	private int nid;

	@SerializedName("score")
	private Integer score;


	public int getNid() {
		return nid;
	}


	public Integer getScore() {
		return score;
	}
}
