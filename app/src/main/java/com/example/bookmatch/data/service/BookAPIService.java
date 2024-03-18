package com.example.bookmatch.data.service;

import static com.example.bookmatch.utils.Constants.API_RETRIEVE_BOOK_BY_KEY_ENDPOINT;
import static com.example.bookmatch.utils.Constants.API_RETRIEVE_BOOK_COVER_BY_ID_ENDPOINT;
import static com.example.bookmatch.utils.Constants.API_SEARCH_BOOK_ENDPOINT;
import static com.example.bookmatch.utils.Constants.API_SEARCH_BOOK_SUBJECT_PARAMETER;

import com.example.bookmatch.model.BooksListApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BookAPIService {

    //TODO: implement response objects
    @GET(API_SEARCH_BOOK_ENDPOINT)
    Call<BooksListApiResponse> getBooks(
            @Query(API_SEARCH_BOOK_SUBJECT_PARAMETER) String subject);

    /*
    @GET(API_RETRIEVE_BOOK_BY_KEY_ENDPOINT)
    Call<BookByKeyApiResponse> getBookByKey(
            @Path("key") String bookKey);

    @GET(API_RETRIEVE_BOOK_COVER_BY_ID_ENDPOINT)
    Call<BookCoverApiResponse> getBookCover(
            @Path("id") String bookKey);*/
}
