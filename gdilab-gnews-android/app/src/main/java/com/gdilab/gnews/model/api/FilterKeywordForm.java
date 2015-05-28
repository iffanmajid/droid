package com.gdilab.gnews.model.api;

public class FilterKeywordForm {

	private String accessToken;
	private Long keywordId;
	private String filter;
	
	public FilterKeywordForm() {
	
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

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

}
