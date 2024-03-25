package com.example.bookmatch.model;

public class OnboardItem {

    String title,description;
    int screenImg;

    public OnboardItem(String title, String description, int screenImg) {
        this.title = title;
        this.description = description;
        this.screenImg = screenImg;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getScreenImg() {
        return screenImg;
    }
}
