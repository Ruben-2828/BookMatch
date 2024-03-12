package com.example.bookmatch.model;

public class Book {
    private final String title;
    private final String author;
    private final String plot;
    private final String genre;
    private final String publicationYear;

    public Book(String title, String author, String plot, String genre, String publicationYear) {
        this.title = title;
        this.author = author;
        this.plot = plot;
        this.genre = genre;
        this.publicationYear = publicationYear;
    }

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
}
