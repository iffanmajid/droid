package com.gdilab.gnews.model.api;

public class CreateKeywordForm {

	private String accessToken;
	private String name;
	
	public CreateKeywordForm() {
	
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
