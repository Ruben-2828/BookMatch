package com.example.bookmatch.data.repository.books;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.bookmatch.data.database.BookDao;
import com.example.bookmatch.data.database.BookRoomDatabase;
import com.example.bookmatch.data.service.BookAPIService;
import com.example.bookmatch.model.Book;
import com.example.bookmatch.model.BooksListApiResponse;
import com.example.bookmatch.utils.ServiceLocator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookRepository implements IBookRepository{

    private static final String TAG = BookRepository.class.getSimpleName();
    private final BookAPIService bookAPIService;
    private final BookDao bookDao;

    public BookRepository(Application application) {
        this.bookAPIService = ServiceLocator.getInstance().getBooksApiService();
        BookRoomDatabase bookRoomDatabase = ServiceLocator.getInstance().getBookDao(application);
        this.bookDao = bookRoomDatabase.bookDao();
    }

    @Override
    public void fetchBooks(String genre) {

        Call<BooksListApiResponse> booksResponseCall = bookAPIService.getBooks("subject:" + genre);
        Log.d(TAG, "genre: " + genre);

        booksResponseCall.enqueue(new Callback<BooksListApiResponse>() {

            @Override
            public void onResponse(Call<BooksListApiResponse> call,
                                   Response<BooksListApiResponse> response) {

                Log.d(TAG, "Response: "+ response);

                if (response.body() != null && response.isSuccessful()) {
                    Log.d(TAG, ""+ response.body().getBooksList());
                    List<Book> bookList = response.body().getBooksList();
                    saveDataInDatabase(bookList);
                } else {
                    Log.d(TAG, "eerroraccio");
                }
            }

            @Override
            public void onFailure(Call<BooksListApiResponse> call,
                                  Throwable t) {
                Log.d(TAG, "Failure: "+ t.getMessage());
            }
        });
    }

    //LiveData

    public LiveData<List<Book>> getSavedBooksLiveData() {
        return bookDao.getSavedBooksLiveData();
    }

    public LiveData<List<Book>> getAllBooksLiveData() {
        return bookDao.getAllBooksLiveData();
    }

    public LiveData<Integer> getSavedBooksCountLiveData() {
        return bookDao.getSavedBooksCountLiveData();
    }

    //Database operations

    public void updateBook(Book book) {
        BookRoomDatabase.databaseWriteExecutor.execute(() -> {
            bookDao.updateSingleSavedBook(book);
        });
    }

    public void removeBookFromSaved(Book book) {
        book.setSaved(false);
        updateBook(book);
    }

    public void deleteBook(Book book) {
        BookRoomDatabase.databaseWriteExecutor.execute(() -> bookDao.deleteBook(book));
    }

    private void saveDataInDatabase(List<Book> bookList) {
        BookRoomDatabase.databaseWriteExecutor.execute(() -> {
            bookDao.insertBookList(bookList);
        });
    }

}
