package com.gdilab.gnews.model.api;

public class DeleteFilterForm {

	private String accessToken;
	private Long keywordId;
	
	public DeleteFilterForm() {
	
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Long getKeywordId() {
		return keywordId;
	}

	public void setKeywordId(Long keywordId) {
		this.keywordId = keywordId;
	}
	
}
