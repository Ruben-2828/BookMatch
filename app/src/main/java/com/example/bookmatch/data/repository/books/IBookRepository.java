package com.example.bookmatch.data.repository.books;

import androidx.lifecycle.LiveData;

import com.example.bookmatch.model.Book;

import java.util.ArrayList;
import java.util.List;

public interface IBookRepository {

    public void fetchBooks(String genre);

    public void updateBook(Book book);

    public LiveData<List<Book>> getSavedBooks();


}
