package com.gdilab.gnews.model.api;

public class CreateChannel {
	private String accessToken;
	private String name;
	private String description;
	private String keyword;
	
	public CreateChannel() {
		// TODO Auto-generated constructor stub
	}

	public CreateChannel(String accessToken, String name, String description,
			String keyword) {
		super();
		this.accessToken = accessToken;
		this.name = name;
		this.description = description;
		this.keyword = keyword;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	
	
}
