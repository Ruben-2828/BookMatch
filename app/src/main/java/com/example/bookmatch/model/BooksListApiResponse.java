package com.example.bookmatch.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BooksListApiResponse {

    @SerializedName("totalItems")
    private int totResults;

    @SerializedName("items")
    private List<Book> booksList;

    public BooksListApiResponse(int totResults, int start, List<Book> booksList) {
        this.totResults = totResults;
        this.booksList = booksList;
    }

    public int getTotResults() {
        return totResults;
    }


    public List<Book> getBooksList() {
        return booksList;
    }

    public void setTotResults(int totResults) {
        this.totResults = totResults;
    }


    public void setBooksList(List<Book> booksList) {
        this.booksList = booksList;
    }
}
