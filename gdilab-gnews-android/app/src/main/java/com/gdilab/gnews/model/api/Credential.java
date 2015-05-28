/**

 * 
 */
package com.gdilab.gnews.model.api;

/**
 * @author vincen
 *
 */
public class Credential {

	private String email;
	private Boolean oauth;
	private String accessToken;
	
	public Credential() {
		// TODO Auto-generated constructor stub
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean isOauth() {
		return oauth;
	}

	public void setOauth(Boolean oauth) {
		this.oauth = oauth;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
}
