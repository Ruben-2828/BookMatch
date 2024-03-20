package com.example.bookmatch.utils;

public class Constants {

    public static final String API_BASE_URL = "https://www.googleapis.com/";
    public static final String API_SEARCH_BOOK_ENDPOINT = "books/v1/volumes";
    public static final String API_SEARCH_BOOK_QUERY = "q";
    public static final String API_RETRIEVE_BOOK_BY_KEY_ENDPOINT = "works/{key}.json";
    public static final String API_RETRIEVE_BOOK_COVER_BY_ID_ENDPOINT = "https://covers.openlibrary.org/b/id/%s-L.jpg";

    public static final String BOOK_DATABASE_NAME = "book_db";
    public static final int DATABASE_VERSION = 4;

}
