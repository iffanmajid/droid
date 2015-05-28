package com.gdilab.gnews.model.api;

import java.util.Date;

public class Channel {

    private Long id;
	private String name;
	private String description;
	private String keywords;
	private Date createdAt;
	
	public Channel() {
		// TODO Auto-generated constructor stub
	}

	public Channel(Long id, String name, String description, String keywords,
			Date createdAt) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.setKeywords(keywords);
		this.createdAt = createdAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	
}
