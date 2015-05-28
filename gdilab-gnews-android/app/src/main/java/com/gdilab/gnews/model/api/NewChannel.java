package com.gdilab.gnews.model.api;

public class NewChannel {
	
	private String accessToken;
	private String name;
	private String description;
	private String[] keywords;
	
	public NewChannel() {

	}

	public NewChannel(String accessToken, String name, String description,
                      String[] keywords) {
		super();
		this.accessToken = accessToken;
		this.name = name;
		this.description = description;
		this.keywords = keywords;
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

	public String[] getKeywords() {
		return keywords;
	}

	public void setKeywords(String[] keywords) {
		this.keywords = keywords;
	}
	
	
	
}
