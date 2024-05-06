package com.example.bookmatch.data.repository.books;

import static com.example.bookmatch.utils.Constants.API_SEARCH_BOOK_MAX_RESULTS_VALUE;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.bookmatch.R;
import com.example.bookmatch.data.database.books.BookDao;
import com.example.bookmatch.data.database.books.BookRoomDatabase;
import com.example.bookmatch.data.service.BookAPIService;
import com.example.bookmatch.model.Book;
import com.example.bookmatch.model.BooksListApiResponse;
import com.example.bookmatch.utils.BookAPIResponseCallback;
import com.example.bookmatch.utils.ServiceLocator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookRepository implements IBookRepository{

    private final BookAPIService bookAPIService;
    private final BookDao bookDao;
    private BookAPIResponseCallback callback;

    public BookRepository(Application application) {
        this.bookAPIService = ServiceLocator.getInstance().getBooksApiService();
        BookRoomDatabase bookRoomDatabase = ServiceLocator.getInstance().getBookDao(application);
        this.bookDao = bookRoomDatabase.bookDao();
        this.callback = null;
    }
    @Override
    public void setCallback(BookAPIResponseCallback callback) {
        this.callback = callback;
    }


    @Override
    public void fetchBooks(String genre, int startIndex) {

        assert callback != null;

        Call<BooksListApiResponse> booksResponseCall = bookAPIService.getBooks("subject:" + genre,
                API_SEARCH_BOOK_MAX_RESULTS_VALUE,
                startIndex);

        booksResponseCall.enqueue(new Callback<BooksListApiResponse>() {

            @Override
            public void onResponse(@NonNull Call<BooksListApiResponse> call,
                                   @NonNull Response<BooksListApiResponse> response) {

                if (response.body() != null && response.isSuccessful()) {
                    // Remove books already seen by user
                    ArrayList<Book> finalBooks = response.body().getBooksList();
                    if (finalBooks != null) {
                        finalBooks.removeAll(getAllBooks());
                    }
                    callback.onSuccess(finalBooks);
                } else {
                    callback.onFailure("Error generating response");
                }
            }

            @Override
            public void onFailure(@NonNull Call<BooksListApiResponse> call,
                                  @NonNull Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    @Override
    public List<Book> getAllBooks() {
        return bookDao.getAllBooks();
    }

    @Override
    public List<Book> getSavedBooks() {
        return bookDao.getSavedBooks();
    }

    @Override
    public Integer getSavedBooksCount() {
        return bookDao.getSavedBooksCount();
    }

    public Integer getReviewedBooksCount() {
        return bookDao.getReviewedBooksCount();
    }


    //Database operations
    @Override
    public void updateBook(Book book) {
        BookRoomDatabase.databaseWriteExecutor.execute(() -> {
            bookDao.updateSingleSavedBook(book);
        });
    }
    @Override
    public void insertBook(Book book) {
        BookRoomDatabase.databaseWriteExecutor.execute(() -> {
            bookDao.insertBook(book);
        });
    }

    @Override
    public void deleteBook(Book book) {
        BookRoomDatabase.databaseWriteExecutor.execute(() -> bookDao.deleteBook(book));
    }

    @Override
    public LiveData<List<Book>> getSavedBooksLiveData() {
        return bookDao.getSavedBooksLiveData();
    }

    @Override
    public LiveData<List<Book>> getReviewedBooksLiveData() {
        return bookDao.getReviewedBooksLiveData();
    }

    @Override
    public Book getBookById(String id) {
        return bookDao.getBookById(id);
    }

    @Override
    public List<Book> getBooksByIds(List<String> ids) {
        List<Book> books = new ArrayList<>();
        for (String id : ids) {
            books.add(bookDao.getBookById(id));
        }
        return books;
    }

}
