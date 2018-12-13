package de.a0zero.rssdl.dto;

import com.google.gson.annotations.SerializedName;

import java.util.*;

/**
 * User: Markus Schulz <msc@0zero.de>
 */
public class JsonUser {

	@SerializedName("uid")
	private Integer userId;

	@SerializedName("name")
	private String name;

	@SerializedName("mail")
	private String email;

	@SerializedName("token")
	private String authToken;

	@SerializedName("roles")
	private Map<Integer, String> roles;

	public Integer getUserId() {
		return userId;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getAuthToken() {
		return authToken;
	}

	public Collection<String> getRoles() {
		return roles.values();
	}
}
