package com.example.bookmatch.data.database.collections;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.bookmatch.model.Collection;

import java.util.List;

@Dao
public interface CollectionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertCollection(Collection collection);

    @Query("SELECT * FROM Collection")
    LiveData<List<Collection>> getAllCollectionsLiveData();

    @Query("SELECT COUNT(*) FROM Collection")
    LiveData<Integer> getCountCollectionLiveData();

    @Delete
    void deleteCollection(Collection collection);

    @Query("SELECT * FROM Collection WHERE name = :name")
    Collection getCollectionByName(String name);
}
