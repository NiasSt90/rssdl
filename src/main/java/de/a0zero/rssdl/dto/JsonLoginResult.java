package de.a0zero.rssdl.dto;

import com.google.gson.annotations.SerializedName;

/**
 * User: Markus Schulz <msc@0zero.de>
 *
 *    Ergebnis von POST-Request an http://www.manitoba-team.de/js-api/user/login mit Form-Params username=<user/> und password=<password/>
 */
public class JsonLoginResult {

	@SerializedName("sessid")
	private String sessionId;

	@SerializedName("session_name")
	private String sessionName;

	@SerializedName("user")
	private JsonUser user;

	public String getSessionId() {
		return sessionId;
	}

	public String getSessionName() {
		return sessionName;
	}

	public JsonUser getUser() {
		return user;
	}
}
