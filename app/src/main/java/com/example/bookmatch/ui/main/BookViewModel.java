package com.example.bookmatch.ui.main;

import static com.example.bookmatch.utils.Constants.API_SEARCH_BOOK_MAX_RESULTS_VALUE;
import static com.example.bookmatch.utils.Constants.API_SEARCH_BOOK_MIN_RESULTS_VALUE;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookmatch.data.repository.books.BookRepository;
import com.example.bookmatch.data.repository.books.IBookRepository;
import com.example.bookmatch.model.Book;
import com.example.bookmatch.utils.BookAPIResponseCallback;

import java.util.ArrayList;
import java.util.List;

public class BookViewModel extends ViewModel implements BookAPIResponseCallback {
    private static final String TAG = BookViewModel.class.getSimpleName();
    private final IBookRepository bookRepository;
    private MutableLiveData<ArrayList<Book>> savedBooks;
    private MutableLiveData<ArrayList<Book>> extractedBooks;
    private String prevGenre;
    private int startIndex;

    public BookViewModel(Application application) {
        this.bookRepository = new BookRepository(application);
        savedBooks = bookRepository.getSavedBooksLiveData();
        this.extractedBooks = new MutableLiveData<>();
        prevGenre = null;
        startIndex = 0;
    }

    public void fetchBooks(String genre) {

        if (!genre.equals(prevGenre)) {
            prevGenre = genre;
            startIndex = 0;
        }

        Log.d(TAG, "Fetching books of genre: " + genre + ", starting at: " + startIndex * API_SEARCH_BOOK_MAX_RESULTS_VALUE);
        bookRepository.setCallback(this);
        bookRepository.fetchBooks(genre, startIndex * API_SEARCH_BOOK_MAX_RESULTS_VALUE);
        startIndex++;
    }

    // if mode true, book saved as favorite, if false book saved as deleted
    public void saveBook(Book book, boolean mode) {
        book.setSaved(mode);
        bookRepository.insertBook(book);
    }

    public MutableLiveData<ArrayList<Book>> getSavedBooks() {
        return savedBooks;
    }

    public void deleteBook(Book book) {

        bookRepository.deleteBook(book);
    }

    public MutableLiveData<ArrayList<Book>> getExtractedBooksLiveData() {
        return extractedBooks;
    }

    public List<Book> getAllBooks() {
        return bookRepository.getAllBooks();

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

    @Override
    public void onSuccess(ArrayList<Book> books) {

        if(books != null) {
            if(books.size() < API_SEARCH_BOOK_MIN_RESULTS_VALUE)
                fetchBooks(prevGenre);

            this.extractedBooks.postValue(books);
        }
    }

    @Override
    public void onFailure(String message) {
        Log.d(TAG, message);
    }
}
