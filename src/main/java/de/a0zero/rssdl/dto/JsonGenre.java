package de.a0zero.rssdl.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * User: Markus Schulz <msc@0zero.de>
 *
 *      "taxonomy": {
 "20": {
 "tid": "20",
 "vid": "2",
 "name": "house",
 "description": "term for a genre of mischungxl (House)",
 "weight": "0"
 },
 "115": {
 "tid": "115",
 "vid": "2",
 "name": "progressive",
 "description": "",
 "weight": "0"
 }
 },

 */
public class JsonGenre implements Serializable {

	@SerializedName("tid")
	private int tid;

	@SerializedName("vid")
	private int vid;

	@SerializedName("name")
	private String name;

	@SerializedName("description")
	private String description;

	public JsonGenre(int tid, String name) {
		this.tid = tid;
		this.name = name;
	}

	public int getTid() {
		return tid;
	}

	public void setTid(int tid) {
		this.tid = tid;
	}

	public int getVid() {
		return vid;
	}

	public void setVid(int vid) {
		this.vid = vid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
