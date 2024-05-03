package com.example.bookmatch.data.repository.collections.container;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.bookmatch.data.database.collections.container.CollectionContainerDao;
import com.example.bookmatch.data.database.collections.container.CollectionContainerRoomDatabase;
import com.example.bookmatch.model.CollectionContainer;

import java.util.List;

public class CollectionContainerRepository implements ICollectionContainerRepository {

    private final CollectionContainerDao collectionDao;

    public CollectionContainerRepository(Application application) {
        CollectionContainerRoomDatabase database = CollectionContainerRoomDatabase.getDatabase(application);
        collectionDao = database.collectionDao();
    }

    @Override
    public boolean insertCollectionContainer(CollectionContainer collection) {
        try{
            CollectionContainerRoomDatabase.databaseWriteExecutor.execute(() -> {
                collectionDao.insertCollectionContainer(collection);
            });
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public void deleteCollectionContainer(CollectionContainer collection) {
        CollectionContainerRoomDatabase.databaseWriteExecutor.execute(() -> {
            collectionDao.deleteCollectionContainer(collection);
        });
    }

    @Override
    public CollectionContainer getCollectionContainerByName(String name) {
        return collectionDao.getCollectionContainerByName(name);
    }

    @Override
    public LiveData<Integer> getCountCollectionContainersLiveData() {
        return collectionDao.getCountCollectionContainersLiveData();
    }

    @Override
    public LiveData<List<CollectionContainer>> getAllCollectionContainersLiveData() {
        return collectionDao.getAllCollectionContainersLiveData();
    }



}

