package com.example.bookmatch.utils;

import com.example.bookmatch.model.Book;

import java.util.ArrayList;

public interface ResponseCallback {

    void onSuccess(ArrayList<Book> books);
    void onFailure(String message);
}
