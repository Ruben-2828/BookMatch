package com.example.bookmatch.data.database.books;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bookmatch.model.Book;

import java.util.List;

@Dao
public interface BookDao {
    @Query("SELECT * FROM Book")
    List<Book> getAllBooks();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBook(Book book);

    @Query("SELECT * FROM Book WHERE is_saved = 1")
    List<Book> getSavedBooks();

    @Query("SELECT COUNT(*) FROM Book WHERE is_saved = 1")
    Integer getSavedBooksCount();

    @Update
    void updateSingleSavedBook(Book book);

    @Delete
    void deleteBook(Book book);

    @Query("SELECT * FROM Book WHERE is_saved = 1")
    LiveData<List<Book>> getSavedBooksLiveData();

    @Query("SELECT * FROM Book WHERE is_reviewed = 1 AND is_saved = 1")
    LiveData<List<Book>> getReviewedBooksLiveData();

    @Query("SELECT COUNT(*) FROM Book WHERE is_reviewed = 1 AND is_saved = 1")
    Integer getReviewedBooksCount();

}

