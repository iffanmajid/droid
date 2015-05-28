package com.gdilab.gnews.model.form;

public class CreateChannelForm {

	private String keywords;
	
	public CreateChannelForm() {
		// TODO Auto-generated constructor stub
	}

	public CreateChannelForm(String keywords) {
		super();
		this.keywords = keywords;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	
}
