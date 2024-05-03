package com.example.bookmatch.data.database.collections.container;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.bookmatch.model.CollectionContainer;

import java.util.List;

@Dao
public interface CollectionContainerDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertCollectionContainer(CollectionContainer collection);

    @Delete
    void deleteCollectionContainer(CollectionContainer collection);

    @Query("SELECT * FROM CollectionContainer")
    LiveData<List<CollectionContainer>> getAllCollectionContainersLiveData();

    @Query("SELECT COUNT(*) FROM CollectionContainer")
    LiveData<Integer> getCountCollectionContainersLiveData();

    @Query("SELECT * FROM CollectionContainer WHERE name = :name")
    CollectionContainer getCollectionContainerByName(String name);
}
