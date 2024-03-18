package com.example.bookmatch.data.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.bookmatch.model.Book;

import java.util.List;

@Dao
public interface BookDao {

    // get all books from the database
    @Query("SELECT * FROM Book")
    List<Book> getAllBooks();

    // get a book by its id
    @Query("SELECT * FROM Book WHERE id = :bookId")
    Book getBookById(String bookId);

    // insert a book into the database
    @Insert
    void insertBook(Book book);

    // delete a book from the database
    @Delete
    void deleteBook(Book book);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertBookList(List<Book> bookList);

    // get the number of books in the database
    @Query("SELECT COUNT(*) FROM Book")
    int getNumberOfBooks();

    // get saved books
    @Query("SELECT * FROM Book WHERE is_saved = 1")
    List<Book> getSavedBooks();

    // get the number of saved books
    @Query("SELECT COUNT(*) FROM Book WHERE is_saved = 1")
    int getNumberOfSavedBooks();

}

