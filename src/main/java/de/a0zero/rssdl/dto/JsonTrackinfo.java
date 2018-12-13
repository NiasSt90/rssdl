package de.a0zero.rssdl.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * User: Markus Schulz <msc@0zero.de>
 */
public class JsonTrackinfo implements Serializable {

	@SerializedName("filename")
	private String filename;

	@SerializedName("downloadfilename")
	private String downloadFilename;

	@SerializedName("fsize")
	private Integer filesize;

	@SerializedName("duration")
	private Integer duration;

	@SerializedName("frequency")
	private Integer frequency;

	@SerializedName("bitrate")
	private Short bitrate;

	//joint stereo, ...
	@SerializedName("mode")
	private String mp3Mode;

	@SerializedName("artist")
	private String artist;

	@SerializedName("title")
	private String title;

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public Integer getFilesize() {
		return filesize;
	}

	public void setFilesize(Integer filesize) {
		this.filesize = filesize;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

	public Short getBitrate() {
		return bitrate;
	}

	public void setBitrate(Short bitrate) {
		this.bitrate = bitrate;
	}

	public String getMp3Mode() {
		return mp3Mode;
	}

	public void setMp3Mode(String mp3Mode) {
		this.mp3Mode = mp3Mode;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDownloadFilename() {
		return downloadFilename;
	}

	public void setDownloadFilename(String downloadFilename) {
		this.downloadFilename = downloadFilename;
	}
}
