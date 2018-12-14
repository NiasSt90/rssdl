package de.a0zero.rssdl.junkies.create;

import com.google.gson.annotations.SerializedName;
import de.a0zero.rssdl.dto.JsonArtistNode;

import java.io.Serializable;
import java.util.Date;


public class CreateSetNodeResult implements Serializable {

	@SerializedName("nid")
	public int nid;

	@SerializedName("uri")
	public String url;

}
