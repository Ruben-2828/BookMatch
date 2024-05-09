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

    // update collection container based on name
    @Query("UPDATE CollectionContainer SET name = :name, description = :description, imageData = :image WHERE name = :oldName")
    void updateCollectionContainer(String name, String description, byte[] image, String oldName);

    // update collection name based on old name
    @Query("UPDATE CollectionContainer SET name = :name WHERE name = :oldName")
    void updateCollectionName(String name, String oldName);

    // update collection description based on name
    @Query("UPDATE CollectionContainer SET description = :description WHERE name = :name")
    void updateCollectionDescription(String name, String description);

    // update collection image based on name
    @Query("UPDATE CollectionContainer SET imageData = :image WHERE name = :name")
    void updateCollectionImage(String name, byte[] image);

    // checks if collection container with same name exists returns a boolean
    @Query("SELECT EXISTS(SELECT 1 FROM CollectionContainer WHERE name = :name)")
    Boolean collectionContainerExists(String name);

    @Query("SELECT * FROM CollectionContainer")
    LiveData<List<CollectionContainer>> getAllCollectionContainersLiveData();

    @Query("SELECT COUNT(*) FROM CollectionContainer")
    LiveData<Integer> getCountCollectionContainersLiveData();

    @Query("SELECT * FROM CollectionContainer WHERE name = :name")
    LiveData<CollectionContainer> getCollectionContainerByNameLiveData(String name);
}
