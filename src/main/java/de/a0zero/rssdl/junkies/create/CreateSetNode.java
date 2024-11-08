package de.a0zero.rssdl.junkies.create;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class CreateSetNode implements Serializable {

	@SerializedName("type")
	public String type = "mischungxl";

	@SerializedName("mxltype")
	public String mxltype = "rss";

	@SerializedName("source")
	public String source;//WIRD NICHT DURCHGEREICHT DURCHS DRUPAL FORMULAR

	@SerializedName("title")
	public String title;

	@SerializedName("fulltitle")
	public String fulltitle;

	@SerializedName("status")
	public Integer status;

	@SerializedName("created")
	public Date created;

	@SerializedName("setcreated")
	Date setcreated;//WIRD NICHT DURCHGEREICHT DURCHS FORMULAR

	@SerializedName("setcreatedfield")
	public Map<String, Integer> setcreatedfield;

	@SerializedName("artistnames")
	public String artistnames;

	/**
		choose Genre Update Mode:
		0: take FROM dj again
		1: write TO dj
		2: write TO set
	 */
	@SerializedName("taxonomy_by_dj")
	public Integer taxonomyByDj;

	@SerializedName("artistdetection_disabled")
	public int artistdetectionDisabled;

	public void setSetCreated(Date setCreated) {
		setcreatedfield = new HashMap<>();
		LocalDate localDate = setCreated.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		setcreatedfield.put("year", localDate.getYear());
		setcreatedfield.put("month", localDate.getMonthValue());
		setcreatedfield.put("day", localDate.getDayOfMonth());
	}


	@Override
	public String toString() {
		return "CreateSetNode{" +
				 "type='" + type + '\'' +
				 ", mxltype='" + mxltype + '\'' +
				 ", source='" + source + '\'' +
				 ", title='" + title + '\'' +
				 ", fulltitle='" + fulltitle + '\'' +
				 ", status=" + status +
				 ", created=" + created +
				 ", setcreated=" + setcreated +
				 ", setcreatedfield=" + setcreatedfield +
				 ", artistnames='" + artistnames + '\'' +
				 ", taxonomyByDj=" + taxonomyByDj +
				 ", artistdetectionDisabled=" + artistdetectionDisabled +
				 '}';
	}
}
