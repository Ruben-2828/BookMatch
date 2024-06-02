package com.example.bookmatch.data.repository.books;

import com.example.bookmatch.model.Book;

import java.util.ArrayList;

public interface IBookSavedLiveData {

    public ArrayList<Book> getSavedBooks();
    public boolean saveBooks(ArrayList<Book> books);
}
