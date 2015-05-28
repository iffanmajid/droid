package com.gdilab.gnews.model.api;

public class ActionKeywordForm {

	private String accessToken;
	private Long id;

    public ActionKeywordForm(String accessToken, Long id) {
        this.accessToken = accessToken;
        this.id = id;
    }

    public ActionKeywordForm() {
	
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
