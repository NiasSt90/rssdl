package de.a0zero.rssdl.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * User: Markus Schulz <msc@0zero.de>
 */
public class JsonUserVote implements Serializable {

	@SerializedName("uid")
	private int uid;

	@SerializedName("name")
	private String name;

	@SerializedName("vote")
	private int vote;

	@SerializedName("timestamp")
	private Date date;

	public int getUid() {
		return uid;
	}

	public String getName() {
		return name;
	}

	public int getVote() {
		return vote;
	}

	public Date getDate() {
		return date;
	}
}
