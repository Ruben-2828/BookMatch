package com.example.bookmatch.data.database;

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

    @Query("UPDATE Book SET is_saved = :isSaved WHERE book_id = :bookId")
    void updateBookSavedStatus(Long bookId, boolean isSaved);

    @Update
    void updateSingleSavedBook(Book book);

    @Delete
    void deleteBook(Book book);

    @Query("SELECT * FROM Book WHERE book_id = :bookId")
    Book getBookById(String bookId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertBookList(List<Book> bookList);


    //LiveData queries

    @Query("SELECT * FROM Book")
    LiveData<List<Book>> getAllBooksLiveData();

    @Query("SELECT * FROM Book WHERE is_saved = 1")
    LiveData<List<Book>> getSavedBooksLiveData();

    @Query("SELECT COUNT(*) FROM Book WHERE is_saved = 1")
    LiveData<Integer> getSavedBooksCountLiveData();


}

