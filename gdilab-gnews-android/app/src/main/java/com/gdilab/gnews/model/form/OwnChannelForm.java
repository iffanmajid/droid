package com.gdilab.gnews.model.form;

public class OwnChannelForm {
	
	private String name;
	private String description;
	
	public OwnChannelForm() {
		// TODO Auto-generated constructor stub
	}

	public OwnChannelForm(String name, String description) {
		super();
		this.name = name;
		this.description = description;
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
	
}
