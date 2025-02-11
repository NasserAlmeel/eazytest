package com.nasser.eazytest;

import com.google.gson.annotations.SerializedName;

public class Image {
    @SerializedName("id")
    private int id;

    @SerializedName("url")
    private String url;

    @SerializedName("description")
    private String description;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
