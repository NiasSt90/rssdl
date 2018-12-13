package de.a0zero.rssdl.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * User: Markus Schulz <msc@0zero.de>
 *
 *    Ergebnis der Anfrage an z.B.
 *    POST Request an http://www.manitoba-team.de/js-api/mischungxl/tracks.json?id=<NodeID/>
 */
public class JsonSetNodeTracks {

	@SerializedName("node")
	private JsonSetNode jsonSetNode;

	@SerializedName("sets")
	private List<JsonSetNodeTrack> trackList;

	public JsonSetNode getJsonSetNode() {
		return jsonSetNode;
	}

	public List<JsonSetNodeTrack> getTrackList() {
		return trackList;
	}
}
