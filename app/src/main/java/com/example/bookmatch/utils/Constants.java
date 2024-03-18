package com.example.bookmatch.utils;

public class Constants {

    public static final String BASE_API_URL = "https://openlibrary.org/";
    public static final String API_SEARCH_BOOK_ENDPOINT = "search.json?fields=key,author_name,cover_i,title,first_publish_year,first_sentence";
    public static final String API_SEARCH_BOOK_SUBJECT_PARAMETER = "subject";
    public static final String API_RETRIEVE_BOOK_BY_KEY_ENDPOINT = "works/{key}.json";
    public static final String API_RETRIEVE_BOOK_COVER_BY_ID_ENDPOINT = "b/id/%s-L.jpg";

}
