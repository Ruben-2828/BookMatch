package com.example.bookmatch.data.repository.books;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.bookmatch.data.service.BookAPIService;
import com.example.bookmatch.model.Book;
import com.example.bookmatch.model.BooksListApiResponse;
import com.example.bookmatch.utils.ServiceLocator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookRepository implements IBookRepository{

    private static final String TAG = BookRepository.class.getSimpleName();

    private final BookAPIService bookAPIService;

    public BookRepository() {
        this.bookAPIService = ServiceLocator.getInstance().getBooksApiService();
    }

    @Override
    public void fetchBooks(String genre) {

        Call<BooksListApiResponse> booksResponseCall = bookAPIService.getBooks(genre);

        booksResponseCall.enqueue(new Callback<BooksListApiResponse>() {

            @Override
            public void onResponse(Call<BooksListApiResponse> call,
                                   Response<BooksListApiResponse> response) {

                Log.d(TAG, ""+ response.body());
                Log.d(TAG, genre);

                if (response.body() != null && response.isSuccessful()) {
                    Log.d(TAG, ""+ response.body().getBooksList());
                } else {
                    Log.d(TAG, "eerroraccio");
                }
            }

            @Override
            public void onFailure(Call<BooksListApiResponse> call,
                                  Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }
}
