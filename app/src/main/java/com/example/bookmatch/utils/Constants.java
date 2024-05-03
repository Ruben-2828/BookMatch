package com.example.bookmatch.utils;

public class Constants {

    public static final String API_BASE_URL = "https://www.googleapis.com/";
    public static final String API_SEARCH_BOOK_ENDPOINT = "books/v1/volumes";
    public static final String API_SEARCH_BOOK_QUERY = "q";

    public static final String API_SEARCH_BOOK_MAX_RESULTS = "maxResults";
    public static final int API_SEARCH_BOOK_MAX_RESULTS_VALUE = 20;
    public static final int API_SEARCH_BOOK_MIN_RESULTS_VALUE = 20;
    public static final String API_SEARCH_BOOK_START_INDEX = "startIndex";
    public static final String BOOK_DATABASE_NAME = "book_db";

    public static final String COLLECTION_CONTAINER_DATABASE_NAME = "container_db";
    public static final String COLLECTION_GROUP_DATABASE_NAME = "group_db";
    public static final int DATABASE_VERSION = 23;

    public static final String SHARED_PREF_NAME = "appSharedPreferences";
    public static final String USER_REMEMBER_ME_SP = "rememberMe";
    public static final String KEY_ONBOARD_OPENED = "isOnboardOpened";


}
