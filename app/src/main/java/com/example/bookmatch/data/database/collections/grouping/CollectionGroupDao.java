package com.example.bookmatch.data.database.collections.grouping;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.bookmatch.model.Book;
import com.example.bookmatch.model.CollectionContainer;
import com.example.bookmatch.model.CollectionGroup;

import java.util.List;

@Dao
public interface CollectionGroupDao {
    @Insert
    void insertGroup(CollectionGroup collectionGroup);

    @Delete
    void deleteGroup(CollectionGroup collectionGroup);

    // Query all books in a given container
    @Query("SELECT bookId FROM collectionGroup WHERE collectionName = :collectionName")
    LiveData<List<String>> getBookIdsInContainerLiveData(String collectionName);

    // Query number of books in a given container
    @Query("SELECT COUNT(*) FROM collectionGroup WHERE collectionName = :collectionName")
    LiveData<Integer> getBooksInContainerCountLiveData(String collectionName);
}
