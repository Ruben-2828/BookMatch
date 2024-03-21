package com.example.bookmatch.ui.main;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookmatch.data.repository.books.BookRepository;
import com.example.bookmatch.data.repository.books.IBookRepository;
import com.example.bookmatch.model.Book;

import java.util.List;

public class BookViewModel extends ViewModel {
    private final IBookRepository bookRepository;
    private LiveData<List<Book>> savedBooks;

    public BookViewModel(Application application) {
        this.bookRepository = new BookRepository(application);
        savedBooks = bookRepository.getSavedBooksLiveData();
    }

    public void fetchBooks(String genre) {

        bookRepository.fetchBooks(genre);
    }

    public void saveBook(Book book) {
        bookRepository.updateBook(book);
    }

    public void deleteBook(Book book) {

        bookRepository.deleteBook(book);
    }


    // LiveData methods
    public LiveData<List<Book>> getSavedBooksLiveData() {
        return savedBooks;
    }

    public LiveData<List<Book>> getAllBooksLiveData() {
        return bookRepository.getAllBooksLiveData();
    }

    public LiveData<Integer> getSavedBooksCountLiveData() {
        return bookRepository.getSavedBooksCountLiveData();
    }




}
