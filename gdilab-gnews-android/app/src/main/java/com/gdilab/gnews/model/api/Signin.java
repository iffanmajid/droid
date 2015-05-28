package com.gdilab.gnews.model.api;

public class Signin {
	private String clientId;
	private String clientSecret;
	private String email;
	private String password;
	
	public Signin() {
		// TODO Auto-generated constructor stub
	}

	public Signin(String clientId, String clientSecret, String email,
			String password) {
		super();
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.email = email;
		this.password = password;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
