package com.example.bookmatch.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BooksListApiResponse {

    @SerializedName("numFound")
    private int totResults;

    private int start;
    @SerializedName("docs")
    private List<Book> booksList;

    public BooksListApiResponse(int totResults, int start, List<Book> booksList) {
        this.totResults = totResults;
        this.start = start;
        this.booksList = booksList;
    }

    public int getTotResults() {
        return totResults;
    }

    public int getStart() {
        return start;
    }

    public List<Book> getBooksList() {
        return booksList;
    }

    public void setTotResults(int totResults) {
        this.totResults = totResults;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setBooksList(List<Book> booksList) {
        this.booksList = booksList;
    }
}
