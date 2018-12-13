package de.a0zero.rssdl.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * User: Markus Schulz <msc@0zero.de>
 */
public class JsonComment implements Serializable {

	@SerializedName("cid")
	private Long cid;

	@SerializedName("uid")
	private int uid;

	@SerializedName("nid")
	private int nid;

	@SerializedName("name")
	private String username;

	@SerializedName("timestamp")
	private Date date;

	@SerializedName("subject")
	private String subject;

	@SerializedName("comment")
	private String comment;


	public Long getCid() {
		return cid;
	}

	public int getUid() {
		return uid;
	}

	public String getUsername() {
		return username;
	}

	public Date getDate() {
		return date;
	}

	public String getSubject() {
		return subject;
	}

	public String getComment() {
		return comment;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getNid() {
		return nid;
	}

	public void setNid(int nid) {
		this.nid = nid;
	}
}
