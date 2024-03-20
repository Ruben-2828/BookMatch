package com.example.bookmatch.data.repository.books;

import androidx.lifecycle.LiveData;

import com.example.bookmatch.model.Book;

import java.util.ArrayList;
import java.util.List;

public interface IBookRepository {

    void fetchBooks(String genre);

    LiveData<List<Book>> getAllBooks();

    void updateBook(Book book);

    LiveData<List<Book>> getSavedBooks();

    void removeBookFromSaved(Book book);

    void deleteBook(Book book);

    LiveData<Integer> getSavedBooksCount();

}
