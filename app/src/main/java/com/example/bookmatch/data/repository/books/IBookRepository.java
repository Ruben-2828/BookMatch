package com.example.bookmatch.data.repository.books;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.bookmatch.model.Book;
import com.example.bookmatch.utils.BookAPIResponseCallback;

import java.util.List;

public interface IBookRepository {

    void fetchBooks(String genre, int startIndex);

    List<Book> getAllBooks();

    List<Book> getSavedBooks();

    Integer getSavedBooksCount();

    void insertBook(Book book);

    void updateBook(Book book);

    void deleteBook(Book book);

    void setCallback(BookAPIResponseCallback callback);

    LiveData<List<Book>> getSavedBooksLiveData();
}
