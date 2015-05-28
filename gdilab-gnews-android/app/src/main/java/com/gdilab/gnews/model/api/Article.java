package com.gdilab.gnews.model.api;

public class Article {
	
	private String id;
	private String url;
	private String host;
	private String title;
	private String image;
	private String date;
	private String content;
    private String twitterUsername;
    private Long totalFavorite;
	private Boolean userLiked;

	public Article() {
		
	}

	public Article(String id, String url, String host, String title,
			String image, String date, String content, Long totalFavorite,
			Boolean userLiked) {
		super();
		this.id = id;
		this.url = url;
		this.host = host;
		this.title = title;
		this.image = image;
		this.setDate(date);
		this.content = content;
		this.totalFavorite = totalFavorite;
		this.userLiked = userLiked;
	}



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getTotalFavorite() {
		return totalFavorite;
	}

	public void setTotalFavorite(Long totalFavorite) {
		this.totalFavorite = totalFavorite;
	}

	public Boolean getUserLiked() {
		return userLiked;
	}

	public void setUserLiked(Boolean userLiked) {
		this.userLiked = userLiked;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

    public String getTwitterUsername() {
        return twitterUsername;
    }

    public void setTwitterUsername(String twitterUsername) {
        this.twitterUsername = twitterUsername;
    }
}
