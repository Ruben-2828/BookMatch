package com.example.bookmatch.ui.main;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookmatch.data.repository.books.BookRepository;
import com.example.bookmatch.data.repository.books.IBookRepository;
import com.example.bookmatch.model.Book;
import com.example.bookmatch.utils.ResponseCallback;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class BookViewModel extends ViewModel implements ResponseCallback {
    private static final String TAG = BookViewModel.class.getSimpleName();
    private final IBookRepository bookRepository;
    private MutableLiveData<ArrayList<Book>> savedBooks;
    private MutableLiveData<ArrayList<Book>> books;

    public BookViewModel(Application application) {
        this.bookRepository = new BookRepository(application);
        this.books = new MutableLiveData<>();
    }

    public void fetchBooks(String genre) {
        bookRepository.setCallback(this);
        bookRepository.fetchBooks(genre);
    }

    public void saveBook(Book book) {
        book.setSaved(true);
        bookRepository.updateBook(book);
    }

    public MutableLiveData<ArrayList<Book>> getSavedBooks() {
        return savedBooks;
    }

    public void deleteBook(Book book) {
        bookRepository.deleteBook(book);
    }

    public MutableLiveData<ArrayList<Book>> getAllBooks() {
        return books;
    }

    public LiveData<Integer> getSavedBooksCount() {
        return bookRepository.getSavedBooksCount();
    }


    @Override
    public void onSuccess(ArrayList<Book> books) {
        this.books.postValue(books);
    }

    @Override
    public void onFailure(String message) {
        Log.d(TAG, message);
    }
}
