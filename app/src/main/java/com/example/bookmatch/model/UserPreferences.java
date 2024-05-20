package com.example.bookmatch.model;

import java.util.HashMap;
import java.util.Map;

public class UserPreferences {
    private String genre, author, book;
    public UserPreferences(String genre, String author, String book){
        this.genre = genre;
        this.author = author;
        this.book = book;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public Map<String, Object> toHashMap(){
        Map<String, Object> preferences = new HashMap<>();

        //mapping user info
        preferences.put("genre", genre);
        preferences.put("author", author);
        preferences.put("book", book);

        return preferences;
    }
}
