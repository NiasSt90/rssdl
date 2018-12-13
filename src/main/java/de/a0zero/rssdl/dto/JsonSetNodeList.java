package de.a0zero.rssdl.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * User: Markus Schulz <msc@0zero.de>
 *
 *    Ergebnis von GET-Request auf http://www.manitoba-team.de/js-api/mischungxl.json?<SearchParams/>
 */
public class JsonSetNodeList implements Serializable {

	@SerializedName("results")
	private List<JsonSetNode> sets;

	public List<JsonSetNode> getSets() {
		return sets;
	}
}
