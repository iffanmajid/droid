package com.gdilab.gnews.model.api;

public class Keyword {

    private Long id;
    private String name;
    private	String preferedImage;
    private String filter;

    public Keyword() {

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

    public String getPreferedImage() {
        return preferedImage;
    }

    public void setPreferedImage(String preferedImage) {
        this.preferedImage = preferedImage;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
}
