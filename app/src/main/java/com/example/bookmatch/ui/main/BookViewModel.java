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
    private final MutableLiveData<ArrayList<Book>> extractedBooks;
    private String prevGenre;
    private int startIndex;

    public BookViewModel(Application application) {
        this.bookRepository = new BookRepository(application);
        this.extractedBooks = new MutableLiveData<>();
        prevGenre = null;
        startIndex = 0;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public void fetchBooks(String selectedGenre) {
        String englishGenre = GenreMapping.getEnglishGenre(selectedGenre);

        if (englishGenre != null) {
            if (!englishGenre.equals(prevGenre)) {
                prevGenre = englishGenre;
                startIndex = 0;
            }

            bookRepository.setCallback(this);
            bookRepository.fetchBooks(englishGenre, startIndex * API_SEARCH_BOOK_MAX_RESULTS_VALUE);
        } else {
            if (!selectedGenre.equals(prevGenre)) {
                prevGenre = selectedGenre;
                startIndex = 0;
            }

            bookRepository.setCallback(this);
            bookRepository.fetchBooks("", startIndex * API_SEARCH_BOOK_MAX_RESULTS_VALUE);
        }
        startIndex++;
    }

    // if mode true, book saved as favorite, if false book saved as deleted
    public void saveBook(Book book, boolean mode) {
        book.setSaved(mode);
        bookRepository.insertBook(book);
    }

    public void deleteBook(Book book) {
        bookRepository.deleteBook(book);
    }

    public MutableLiveData<ArrayList<Book>> getExtractedBooksLiveData() {
        return extractedBooks;
    }

    // LiveData methods
    public LiveData<List<Book>> getSavedBooksLiveData() {
        return bookRepository.getSavedBooksLiveData();
    }

    public LiveData<List<Book>> getReviewedBooksLiveData() { return bookRepository.getReviewedBooksLiveData(); }

    public MutableLiveData<Integer> getSavedBooksCountLiveData() {
        return new MutableLiveData<>(bookRepository.getSavedBooksCount());
    }

    public MutableLiveData<Integer> getReviewedBooksCountLiveData() {
        return new MutableLiveData<>(bookRepository.getReviewedBooksCount());
    }

    public List<Book> getBooksByIds(List<String> ids) {
        return bookRepository.getBooksByIds(ids);
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
        Log.e(TAG, message);
    }

    public void updateBook(Book book) {
        bookRepository.updateBook(book);
    }
}
