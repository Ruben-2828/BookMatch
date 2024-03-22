package com.example.bookmatch.data.repository.books;

import static com.example.bookmatch.utils.Constants.API_SEARCH_BOOK_MAX_RESULTS_VALUE;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.bookmatch.data.database.BookDao;
import com.example.bookmatch.data.database.BookRoomDatabase;
import com.example.bookmatch.data.service.BookAPIService;
import com.example.bookmatch.model.Book;
import com.example.bookmatch.model.BooksListApiResponse;
import com.example.bookmatch.utils.ResponseCallback;
import com.example.bookmatch.utils.ServiceLocator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookRepository implements IBookRepository{

    private static final String TAG = BookRepository.class.getSimpleName();
    private final BookAPIService bookAPIService;
    private final BookDao bookDao;
    private ResponseCallback callback;

    public void setCallback(ResponseCallback callback) {
        this.callback = callback;
    }

    public BookRepository(Application application) {
        this.bookAPIService = ServiceLocator.getInstance().getBooksApiService();
        BookRoomDatabase bookRoomDatabase = ServiceLocator.getInstance().getBookDao(application);
        this.bookDao = bookRoomDatabase.bookDao();
        this.callback = null;
    }

    @Override
    public void fetchBooks(String genre, int startIndex) {

        assert callback != null;

        Call<BooksListApiResponse> booksResponseCall = bookAPIService.getBooks("subject:" + genre,
                API_SEARCH_BOOK_MAX_RESULTS_VALUE,
                startIndex);
        //Log.d(TAG, "genre: " + genre);

        booksResponseCall.enqueue(new Callback<BooksListApiResponse>() {

            @Override
            public void onResponse(Call<BooksListApiResponse> call,
                                   Response<BooksListApiResponse> response) {

                //Log.d(TAG, "Response: "+ response);

                if (response.body() != null && response.isSuccessful()) {
                    // Remove books already seen by user
                    ArrayList<Book> finalBooks = response.body().getBooksList();
                    if (finalBooks != null)
                        finalBooks.removeAll(getAllBooks());
                    callback.onSuccess(finalBooks);
                } else {
                    callback.onFailure("Error generating response");
                }
            }

            @Override
            public void onFailure(Call<BooksListApiResponse> call,
                                  Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    public void updateBook(Book book) {
        BookRoomDatabase.databaseWriteExecutor.execute(() -> {
            bookDao.updateSingleSavedBook(book);
        });
    }

    public void insertBook(Book book) {
        BookRoomDatabase.databaseWriteExecutor.execute(() -> {
            bookDao.insertBook(book);
        });
    }

    public LiveData<List<Book>> getSavedBooks() {
        return bookDao.getSavedBooks();
    }

    public void removeBookFromSaved(Book book) {
        book.setSaved(false);
        updateBook(book);
    }

    public void deleteBook(Book book) {
        BookRoomDatabase.databaseWriteExecutor.execute(() -> bookDao.deleteBook(book));
    }

    public MutableLiveData<List<Book>> getAllBooksLiveData() {
        return new MutableLiveData<>(bookDao.getAllBooksLiveData());
    }

    @Override
    public List<Book> getAllBooks() {
        return bookDao.getAllBooks();
    }

    public LiveData<Integer> getSavedBooksCount() {
        return bookDao.getSavedBooksCount();
    }


    private void saveDataInDatabase(List<Book> bookList) {
        BookRoomDatabase.databaseWriteExecutor.execute(() -> {
            bookDao.insertBookList(bookList);
        });
    }

}
