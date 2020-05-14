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


    public String getDate() {
        return date;
    }


    public String getImageUri() {
        return imageUri;
    }


    public String getNewsUrl() {
        return newsUrl;
    }


    public String getSectionName() {
        return sectionName;
    }
}
