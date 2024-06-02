package com.example.bookmatch.model;

import com.example.bookmatch.data.repository.books.BookRepository;

import java.util.ArrayList;

public abstract class Result {

    public boolean isSuccess(){
        if(this instanceof PreferencesResponseSuccess || this instanceof UserResponseSuccess
        || this instanceof BooksResponseSuccess){
            return true;
        } else {
            return false;
        }
    }
    public static final class UserResponseSuccess extends Result{
        private final User user;
        public UserResponseSuccess(User user) {
            this.user = user;
        }
        public User getData() {
            return user;
        }
    }

    public static final class BooksResponseSuccess extends Result{
        private final ArrayList<Book> books;
        public BooksResponseSuccess(ArrayList<Book> books){
            this.books = books;
        }

        public ArrayList<Book> getBooks(){
            return books;
        }
    }

    public static final class PreferencesResponseSuccess extends Result{
        private final UserPreferences userPreference;
        public PreferencesResponseSuccess(UserPreferences userPreference){
            this.userPreference = userPreference;
        }

        public UserPreferences getUserPreference(){
            return userPreference;
        }
    }

    public static final class Error extends Result{
        private final String message;
        public Error(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }
}
