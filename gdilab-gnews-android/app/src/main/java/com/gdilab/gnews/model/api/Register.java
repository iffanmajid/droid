package com.gdilab.gnews.model.api;

public class Register {
	private String clientId;
	private String clientSecret;
	private Long twitterUserId;
	private String twitterUsername;
	private String fullname;
	private String twitterAccessToken;
	private String twitterAccessTokenSecret;
	private String profilePicture;

	public Register() {
		// TODO Auto-generated constructor stub
	}

	public Register(String clientId, String clientSecret, Long twitterUserId,
			String twitterUsername, String fullname, String twitterAccessToken,
			String twitterAccessTokenSecret, String profilePicture) {
		super();
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.twitterUserId = twitterUserId;
		this.twitterUsername = twitterUsername;
		this.fullname = fullname;
		this.twitterAccessToken = twitterAccessToken;
		this.twitterAccessTokenSecret = twitterAccessTokenSecret;
        this.profilePicture = profilePicture;
	}

	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getClientSecret() {
		return clientSecret;
	}
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	public Long getTwitterUserId() {
		return twitterUserId;
	}
	public void setTwitterUserId(Long twitterUserId) {
		this.twitterUserId = twitterUserId;
	}
	public String getTwitterUsername() {
		return twitterUsername;
	}
	public void setTwitterUsername(String twitterUsername) {
		this.twitterUsername = twitterUsername;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getTwitterAccessToken() {
		return twitterAccessToken;
	}
	public void setTwitterAccessToken(String twitterAccessToken) {
		this.twitterAccessToken = twitterAccessToken;
	}
	public String getTwitterAccessTokenSecret() {
		return twitterAccessTokenSecret;
	}
	public void setTwitterAccessTokenSecret(String twitterAccessTokenSecret) {
		this.twitterAccessTokenSecret = twitterAccessTokenSecret;
	}

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
