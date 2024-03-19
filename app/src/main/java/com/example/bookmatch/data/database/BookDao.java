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



    // get all books from the database
    @Query("SELECT * FROM Book")
    List<Book> getAllBooks();



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertBookList(List<Book> bookList);



    @Query("SELECT * FROM Book WHERE is_saved = 1")
    LiveData<List<Book>> getSavedBooks();



    @Query("UPDATE Book SET is_saved = :isSaved WHERE id = :bookId")
    void updateBookSavedStatus(Long bookId, boolean isSaved);



    @Update
    void updateListSavedBooks(List<Book> book);



    @Update
    void updateSingleSavedBook(Book book);



    // get a book by its id
    @Query("SELECT * FROM Book WHERE id = :bookId")
    Book getBookById(String bookId);



    // insert a book into the database
    @Insert
    void insertBook(Book book);



    // delete a book from the database
    @Delete
    void deleteBook(Book book);



    // get the number of books in the database
    @Query("SELECT COUNT(*) FROM Book")
    int getNumberOfBooks();



    // get the number of saved books
    @Query("SELECT COUNT(*) FROM Book WHERE is_saved = 1")
    LiveData<Integer> getNumberOfSavedBooks();

}

