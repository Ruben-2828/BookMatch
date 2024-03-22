package com.example.bookmatch.data.repository.books;

import androidx.lifecycle.LiveData;

import com.example.bookmatch.model.Book;
import com.example.bookmatch.utils.ResponseCallback;

import java.util.List;

public interface IBookRepository {

    void fetchBooks(String genre, int startIndex);

    LiveData<List<Book>> getAllBooksLiveData();

    List<Book> getAllBooks();

    void insertBook(Book book);

    void updateBook(Book book);

    LiveData<List<Book>> getSavedBooks();

    void removeBookFromSaved(Book book);

    void deleteBook(Book book);

    LiveData<Integer> getSavedBooksCount();

    void setCallback(ResponseCallback callback);
}
