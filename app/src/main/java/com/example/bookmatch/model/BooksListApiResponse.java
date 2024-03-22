package com.example.bookmatch.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BooksListApiResponse {

    private class Item {

        private class VolumeInfo {
            private String title;
            private ArrayList<String> authors;
            private String description;
            private String publishedDate;
            private HashMap<String, String> imageLinks;

            public VolumeInfo(String title,
                              ArrayList<String> authors,
                              String description,
                              String publishedDate,
                              HashMap<String, String> imageLinks) {
                this.title = title;
                this.authors = authors;
                this.description = description;
                this.publishedDate = publishedDate;
                this.imageLinks = imageLinks;
            }

            public String getTitle() {
                return title;
            }

            public ArrayList<String> getAuthors() {
                return authors;
            }

            public String getDescription() {
                return description;
            }

            public String getPublishedDate() {
                return publishedDate;
            }

            public HashMap<String, String> getImageLinks() {
                return imageLinks;
            }
        }
        private String id;
        @SerializedName("volumeInfo")
        private VolumeInfo bookInfos;

        public Item(String id, VolumeInfo bookInfos) {
            this.id = id;
            this.bookInfos = bookInfos;
        }

        public String getId() {
            return id;
        }

        public VolumeInfo getBookInfos() {
            return bookInfos;
        }
    }

    @SerializedName("totalItems")
    private int totResults;

    @SerializedName("items")
    private List<Item> itemsList;

    public BooksListApiResponse(int totResults, int start, List<Item> itemsList) {
        this.totResults = totResults;
        this.itemsList = itemsList;
    }

    public int getTotResults() {
        return totResults;
    }


    public ArrayList<Book> getBooksList() {
        ArrayList<Book> books = new ArrayList<>();

        if (itemsList == null)
            return null;

        for(Item i: itemsList) {
            String imageURI = null;
            if (i.getBookInfos().getImageLinks() != null)
                imageURI = i.getBookInfos().getImageLinks().getOrDefault("thumbnail", null);

            Book b = new Book(i.getId(),
                    i.getBookInfos().getTitle(),
                    i.getBookInfos().getAuthors(),
                    i.getBookInfos().getDescription(),
                    i.getBookInfos().getPublishedDate(),
                    imageURI,
                    false);
            books.add(b);
        }

        return books;
    }

    public void setTotResults(int totResults) {
        this.totResults = totResults;
    }


    public void setItemsList(List<Item> itemsList) {
        this.itemsList = itemsList;
    }
}
