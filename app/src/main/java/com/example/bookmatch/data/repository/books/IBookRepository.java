package com.example.bookmatch.data.repository.books;

import androidx.lifecycle.LiveData;

import com.example.bookmatch.model.Book;

import java.util.List;

public interface IBookRepository {

    void fetchBooks(String genre);

    void updateBook(Book book);

    void removeBookFromSaved(Book book);

    void deleteBook(Book book);

    //LiveData methods

    LiveData<Integer> getSavedBooksCountLiveData();

    LiveData<List<Book>> getAllBooksLiveData();

    LiveData<List<Book>> getSavedBooksLiveData();

}
