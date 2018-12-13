package de.a0zero.rssdl.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * User: Markus Schulz <msc@0zero.de>
 */
public class JsonVote implements Serializable {

	@SerializedName("my")
	private Integer myVote;

	@SerializedName("avg")
	private Double average;

	@SerializedName("count")
	private int count;

	@SerializedName("firstgoodvote")
	private JsonUserVote firstGoodVote;

	@SerializedName("all")
	private List<JsonUserVote> votesList;

	@SerializedName("sum")
	private int voteSum;

	public Integer getMyVote() {
		return myVote;
	}

	public Double getAverage() {
		return average;
	}

	public int getCount() {
		return count;
	}

	public JsonUserVote getFirstGoodVote() {
		return firstGoodVote;
	}

	public List<JsonUserVote> getVotesList() {
		return votesList;
	}

	public int getVoteSum() {
		return voteSum;
	}
}
