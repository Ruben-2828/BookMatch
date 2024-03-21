package com.example.bookmatch.data.repository.collections;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.bookmatch.data.database.collections.CollectionDao;
import com.example.bookmatch.data.database.collections.CollectionRoomDatabase;
import com.example.bookmatch.model.Collection;

import java.util.List;

public class CollectionRepository implements ICollectionRepository {

    private final CollectionDao collectionDao;

    public CollectionRepository(Application application) {
        CollectionRoomDatabase database = CollectionRoomDatabase.getDatabase(application);
        collectionDao = database.collectionDao();
    }

    public void insertCollection(Collection collection) {
        CollectionRoomDatabase.databaseWriteExecutor.execute(() -> {
            collectionDao.insertCollection(collection);
        });
    }

    public void deleteCollection(Collection collection) {
        CollectionRoomDatabase.databaseWriteExecutor.execute(() -> {
            collectionDao.deleteCollection(collection);
        });
    }

    public LiveData<Integer> getCountCollectionLiveData() {
        return collectionDao.getCountCollectionLiveData();
    }

    public LiveData<List<Collection>> getAllCollectionsLiveData() {
        return collectionDao.getAllCollectionsLiveData();
    }



}

