package de.a0zero.rssdl.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.*;

/**
 * User: Markus Schulz <msc@0zero.de>
 */
public class JsonDJ implements Serializable {

	@SerializedName("id")
	private int id;

	@SerializedName("nid")
	private int nid;

	@SerializedName("name")
	private String name;

	@SerializedName("country")
	private String country;

	@SerializedName("lastfm_artistinfo_timestamp")
	private Date lastfmArtistinfoTime;

	@SerializedName("lastfm_artistinfo")
	private JsonLastFmArtistInfo lastfmArtistinfo;

	@SerializedName("taxonomy")
	private Map<Integer, JsonGenre> genre;

	@SerializedName("similar_artists")
	private List<JSonSimilarArtists> similarArtists;


	public JsonDJ(int id, String name) {
		this.id = id;
		this.name = name;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getNid() {
		return nid;
	}


	public void setNid(int nid) {
		this.nid = nid;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getCountry() {
		return country;
	}


	public void setCountry(String country) {
		this.country = country;
	}


	public Date getLastfmArtistinfoTime() {
		return lastfmArtistinfoTime;
	}


	public JsonLastFmArtistInfo getLastfmArtistinfo() {
		return lastfmArtistinfo;
	}


	public Map<Integer, JsonGenre> getGenre() {
		return genre;
	}


	public List<JSonSimilarArtists> getSimilarArtists() {
		return similarArtists;
	}
}
