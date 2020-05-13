package com.rahimovjavlon1212.yourfeed.models;

public class NewsModel {
    private String title;
    private String date;
    private String imageUri;
    private String newsUrl;
    private String sectionName;

    public NewsModel(String title, String date, String imageUri, String newsUrl, String sectionName) {
        this.title = title;
        this.date = date;
        this.imageUri = imageUri;
        this.newsUrl = newsUrl;
        this.sectionName = sectionName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }
}
