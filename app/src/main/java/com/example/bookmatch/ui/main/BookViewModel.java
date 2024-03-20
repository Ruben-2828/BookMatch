package com.example.bookmatch.ui.main;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookmatch.data.repository.books.BookRepository;
import com.example.bookmatch.data.repository.books.IBookRepository;
import com.example.bookmatch.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookViewModel extends ViewModel {
    private final IBookRepository bookRepository;
    private LiveData<List<Book>> savedBooks;

    public BookViewModel(Application application) {
        this.bookRepository = new BookRepository(application);
        savedBooks = bookRepository.getSavedBooks();
    }

    public void fetchBooks(String genre) {
        bookRepository.fetchBooks(genre);
    }

    public void saveBook(Book book) {
        bookRepository.updateBook(book);
    }

    public LiveData<List<Book>> getSavedBooks() {
        return savedBooks;
    }

    public void deleteBook(Book book) {
        bookRepository.deleteBook(book);
    }

    public LiveData<List<Book>> getAllBooks() {
        return bookRepository.getAllBooks();
    }

    public LiveData<Integer> getSavedBooksCount() {
        return bookRepository.getSavedBooksCount();
    }




}
