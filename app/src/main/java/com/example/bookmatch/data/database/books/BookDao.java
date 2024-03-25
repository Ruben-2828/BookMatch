package com.example.bookmatch.data.database.books;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bookmatch.model.Book;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface BookDao {
    @Query("SELECT * FROM Book")
    List<Book> getAllBooks();

    @Query("SELECT * FROM Book WHERE id = :bookId")
    Book getBookById(String bookId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertBook(Book book);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertBookList(List<Book> bookList);

    @Query("SELECT * FROM Book WHERE is_saved = 1")
    List<Book> getSavedBooks();

    @Query("SELECT COUNT(*) FROM Book WHERE is_saved = 1")
    Integer getSavedBooksCount();

    @Query("UPDATE Book SET is_saved = :isSaved WHERE id = :bookId")
    void updateBookSavedStatus(String bookId, boolean isSaved);

    @Update
    void updateSingleSavedBook(Book book);

    @Delete
    void deleteBook(Book book);

    @Query("SELECT * FROM Book WHERE is_saved = 1")
    LiveData<List<Book>> getSavedBooksLiveData();
}

