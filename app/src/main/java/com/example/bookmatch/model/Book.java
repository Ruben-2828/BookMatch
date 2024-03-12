package com.example.bookmatch.model;

public class Book {

    private final String id;
    private final String title;
    private final String author;
    private final String plot;
    private final String genre;
    private final String publicationYear;
    private final String cover;

    public Book(String id, String title, String author, String plot, String genre, String publicationYear, String cover) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.plot = plot;
        this.genre = genre;
        this.publicationYear = publicationYear;
        this.cover = cover;
    }

    public String getId() { return id; }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPlot() {
        return plot;
    }

    public String getGenre() {
        return genre;
    }

    public String getPublicationYear() {
        return publicationYear;
    }

    public String getCover() { return cover; }
}
