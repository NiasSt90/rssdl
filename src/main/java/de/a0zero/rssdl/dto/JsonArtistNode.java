package de.a0zero.rssdl.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class JsonArtistNode implements Serializable {

	@SerializedName("nid")
	public int nid;


	@SerializedName("title")
	public String title;


	public JsonArtistNode(int nid, String title) {
		this.nid = nid;
		this.title = title;
	}
}
