package com.example.bookmatch.data.repository.books;

import androidx.lifecycle.LiveData;

import com.example.bookmatch.model.Book;
import com.example.bookmatch.utils.callbacks.BookAPIResponseCallback;

import java.util.List;

public interface IBookRepository {

    void fetchBooks(String genre, int startIndex);

    List<Book> getAllBooks();

    List<Book> getSavedBooks();

    Integer getSavedBooksCount();

    Integer getReviewedBooksCount();

    void insertBook(Book book);

    void updateBook(Book book);

    void deleteBook(Book book);

    void setCallback(BookAPIResponseCallback callback);

    LiveData<List<Book>> getSavedBooksLiveData();

    LiveData<List<Book>> getReviewedBooksLiveData();

    Book getBookById(String id);

    List<Book> getBooksByIds(List<String> id);

    LiveData<Boolean> isBookSavedLiveData(String id);
}
