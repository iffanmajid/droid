package com.gdilab.gnews.model.api;

public class RegisterBasic {
	private String email;
	private String accessToken;
	private String password;
	private String country;
	
	public RegisterBasic() {
		// TODO Auto-generated constructor stub
	}

	public RegisterBasic(String email, String accessToken, String password,
			String country) {
		super();
		this.email = email;
		this.accessToken = accessToken;
		this.password = password;
		this.country = country;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	
	
}
