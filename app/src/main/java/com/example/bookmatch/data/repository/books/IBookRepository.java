package com.example.bookmatch.data.repository.books;

import androidx.lifecycle.LiveData;

import com.example.bookmatch.model.Book;
import com.example.bookmatch.utils.BookAPIResponseCallback;

import java.util.List;

public interface IBookRepository {

    void fetchBooks(String genre, int startIndex);

    List<Book> getAllBooks();

    void insertBook(Book book);

    void updateBook(Book book);

    void removeBookFromSaved(Book book);

    void deleteBook(Book book);

    //LiveData methods

    LiveData<Integer> getSavedBooksCountLiveData();

    LiveData<List<Book>> getAllBooksLiveData();

    LiveData<List<Book>> getSavedBooksLiveData();

    void setCallback(BookAPIResponseCallback callback);
}
