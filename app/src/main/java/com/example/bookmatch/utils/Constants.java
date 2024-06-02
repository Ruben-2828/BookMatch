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
    public static final int DATABASE_VERSION = 26;

    public static final String SHARED_PREF_NAME = "appSharedPreferences";
    public static final String USER_REMEMBER_ME_SP = "rememberMe";
    public static final String USER_PREFERENCES_PASSWORD = "password";
    public static final String USER_PREFERENCES_EMAIL = "email";
    public static final String KEY_ONBOARD_OPENED = "isOnboardOpened";

    public static final String LAST_UPDATE_PREF = "lastUpdatePref";
    public static final String USER_PREFERENCES_GOOGLE_ID_TOKEN = "userIdTokenGoogle";

    public static final String USER_PREFERENCES_GOOGLE_ACCESS_METHOD = "userGoogleAccessMethod";

    public static final String USERS_COLLECTION_NAME = "users";

    public static final String USERS_PREFERENCES_NAME = "preferences";

    public static final String ERROR_WRITING_FIRESTORE = "Error writing document";

    public static final String UNEXPECTED_ERROR = "Unexpected error";

    public static final String INVALID_USER_ERROR = "Invalid user";

    public static final String INVALID_CREDENTIALS_ERROR = "Invalid credentials";

    public static final String USER_COLLISION_ERROR = "Email already taken";

    public static final String WEAK_PASSWORD_ERROR = "password is weak";

    public static final String NO_SUCH_DOCUMENT_ERROR = "Document not found";

    public static final String NO_CONNECTION = "No connection";

    public static final String BOOKS_JSON_DIRECTORY = "books.json";

    public static final int SAVE_INTERVAL = 1000 * 60 * 5; //5 minutes in milliseconds

    public static final String BASE_URL_IMAGES_STORAGE = "images/";
    public static final String ERROR_WHILE_UPLOADING_IMAGE = "Error while uploading image";
}
