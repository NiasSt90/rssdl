package de.a0zero.rssdl.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.*;

/**
 * User: Markus Schulz <msc@0zero.de>
 */
public class JsonLastFmArtistInfo implements Serializable {

	@SerializedName("name")
	private String name;

	@SerializedName("mbid")
	private String mbid;

	@SerializedName("url")
	private String url;

	@SerializedName("image")
	private List<JsonLastFmImage> imageList;

	@SerializedName("similar")
	private JsonSimilarDjs similarDjs;

	@SerializedName("bio")
	private JsonDjBio bioContent;

	public String getLargestImgUrl() {
		if (imageList == null) return null;
		String url = "";
		LastFmImageSize size = LastFmImageSize.small;
		for (JsonLastFmImage image : imageList) {
			try {
				LastFmImageSize curSize = LastFmImageSize.valueOf(image.getSize());
				if (curSize.ordinal() >= size.ordinal() && image.getUrl() != null && image.getUrl().matches("^https?://.+")) {
					size = curSize;
					url = image.getUrl();
				}
			}
			catch (IllegalArgumentException e) {
				//ok
			}
		}
		return url;
	}

	enum LastFmImageSize {
		small,
		medium,
		large,
		extralarge,
		mega
	}

	public String getName() {
		return name;
	}

	public String getMbid() {
		return mbid;
	}

	public String getUrl() {
		return url;
	}

	public List<JsonLastFmImage> getImageList() {
		return imageList;
	}

	public List<JsonLastFmArtistInfo> getSimilarDjs() {
		return similarDjs != null ? similarDjs.artist : new ArrayList<JsonLastFmArtistInfo>();
	}

	public String getBioContent() {
		return bioContent != null ? bioContent.bioContent : "";
	}

	public String getBioSummary() {
		return bioContent != null ? bioContent.bioSummary : "";
	}


	public static class JsonSimilarDjs implements Serializable {
		@SerializedName("artist")
		public List<JsonLastFmArtistInfo> artist = new ArrayList<>();
	}

	public static class JsonDjBio implements Serializable {
		@SerializedName("summary")
		public String bioSummary;

		@SerializedName("content")
		public String bioContent;
	}

}
