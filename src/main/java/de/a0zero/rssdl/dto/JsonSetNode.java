package de.a0zero.rssdl.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.*;

public class JsonSetNode implements Serializable {

	@SerializedName("nid")
	private int nid;

	@SerializedName("type")
	private String type = "mischungxl";

	@SerializedName("uid")
	private Integer uid;

	@SerializedName("name")
	private String userName;

	@SerializedName("title")
	private String title;

	@SerializedName("status")
	private Integer status;

	@SerializedName("created")
	private Date created;

	@SerializedName("changed")
	private Date modified;

	@SerializedName("setcreated")
	private Date setcreated;

	@SerializedName("lastheard")
	private Date lastheared;

	@SerializedName("dj")
	private JsonDJ dj;

	@SerializedName("artistnids")
	private List<Integer> artistnids;

	@SerializedName("duration")
	private Integer duration;

	@SerializedName("taxonomy")
	private Map<Integer, JsonGenre> genre;

	@SerializedName("votes")
	private JsonVote vote;

	@SerializedName("bookmarked")
	private Boolean bookmarked;

	@SerializedName("myplaycount")
	private int myPlaycount;

	@SerializedName("allplaycount")
	private int allPlaycount;

	@SerializedName("trackinfo")
	private List<JsonTrackinfo> trackinfos;

	@SerializedName("comments")
	private List<JsonComment> comments;

	//choose Genre Update Mode:
	/*
		0: take FROM dj again
		1: write TO dj
		2: write TO set
	 */
	@SerializedName("taxonomy_by_dj")
	private Integer taxonomyByDj;

	@SerializedName("score")
	private Integer score;

	public JsonSetNode(int nid) {
		this.nid = nid;
	}

	public int getNid() {
		return nid;
	}


	public List<Integer> getArtistnids() {
		return artistnids;
	}


	public void setArtistnids(List<Integer> artistnids) {
		this.artistnids = artistnids;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public Integer getUid() {
		return uid;
	}

	public String getUserName() {
		return userName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getStatus() {
		return status;
	}

	public Date getCreated() {
		return created;
	}

	public Date getModified() {
		return modified;
	}

	public Date getSetcreated() {
		return setcreated;
	}

	public Date getLastheared() {
		return lastheared;
	}

	public JsonDJ getDj() {
		return dj;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public Collection<JsonGenre> getGenres() {
		return genre.values();
	}

	public JsonVote getVote() {
		return vote;
	}

	public Boolean getBookmarked() {
		return bookmarked;
	}

	public List<JsonTrackinfo> getTrackinfos() {
		return trackinfos;
	}

	public List<JsonComment> getComments() {
		return comments;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public void setSetcreated(Date setcreated) {
		this.setcreated = setcreated;
	}

	public void setLastheared(Date lastheared) {
		this.lastheared = lastheared;
	}

	public void setDj(JsonDJ dj) {
		this.dj = dj;
	}

	public void setGenre(Map<Integer, JsonGenre> genre) {
		this.genre = genre;
	}

	public void setVote(JsonVote vote) {
		this.vote = vote;
	}

	public void setBookmarked(Boolean bookmarked) {
		this.bookmarked = bookmarked;
	}

	public void setTrackinfos(List<JsonTrackinfo> trackinfos) {
		this.trackinfos = trackinfos;
	}

	public void setComments(List<JsonComment> comments) {
		this.comments = comments;
	}

	public Integer getTaxonomyByDj() {
		return taxonomyByDj;
	}

	public void setTaxonomyByDj(Integer taxonomyByDj) {
		this.taxonomyByDj = taxonomyByDj;
	}

	public int getMyPlaycount() {
		return myPlaycount;
	}

	public int getAllPlaycount() {
		return allPlaycount;
	}


	public Integer getScore() {
		return score;
	}
}
