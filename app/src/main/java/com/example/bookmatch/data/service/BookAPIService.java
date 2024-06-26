package com.example.bookmatch.data.service;

import static com.example.bookmatch.utils.Constants.API_SEARCH_BOOK_ENDPOINT;
import static com.example.bookmatch.utils.Constants.API_SEARCH_BOOK_MAX_RESULTS;
import static com.example.bookmatch.utils.Constants.API_SEARCH_BOOK_QUERY;
import static com.example.bookmatch.utils.Constants.API_SEARCH_BOOK_START_INDEX;

import com.example.bookmatch.model.BooksListApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BookAPIService {

    @GET(API_SEARCH_BOOK_ENDPOINT)
    Call<BooksListApiResponse> getBooks(
            @Query(API_SEARCH_BOOK_QUERY) String subject,
            @Query(API_SEARCH_BOOK_MAX_RESULTS) int maxResults,
            @Query(API_SEARCH_BOOK_START_INDEX) int startIndex);

}
