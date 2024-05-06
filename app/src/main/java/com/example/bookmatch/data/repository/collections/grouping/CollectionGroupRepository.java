package com.example.bookmatch.data.repository.collections.grouping;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.bookmatch.data.database.collections.container.CollectionContainerDao;
import com.example.bookmatch.data.database.collections.container.CollectionContainerRoomDatabase;
import com.example.bookmatch.data.database.collections.grouping.CollectionGroupDao;
import com.example.bookmatch.data.database.collections.grouping.CollectionGroupRoomDatabase;
import com.example.bookmatch.model.Book;
import com.example.bookmatch.model.CollectionContainer;
import com.example.bookmatch.model.CollectionGroup;

import java.util.List;

public class CollectionGroupRepository implements ICollectionGroupRepository {
    private final CollectionGroupDao collectionGroupDao;

    public CollectionGroupRepository(Application application) {
        CollectionGroupRoomDatabase database = CollectionGroupRoomDatabase.getDatabase(application);
        collectionGroupDao = database.collectionGroupDao();
    }

    @Override
    public boolean insertCollectionGroup(CollectionGroup collectionGroup) {
        try {
            CollectionGroupRoomDatabase.databaseWriteExecutor.execute(() -> {
                collectionGroupDao.insertGroup(collectionGroup);
            });
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public void deleteCollectionGroup(CollectionGroup collectionGroup) {
        CollectionGroupRoomDatabase.databaseWriteExecutor.execute(() -> {
            collectionGroupDao.deleteGroup(collectionGroup);
        });
    }

    @Override
    public LiveData<List<String>> getBookIdsInContainerLiveData(String containerName) {
        return collectionGroupDao.getBookIdsInContainerLiveData(containerName);
    }

    @Override
    public LiveData<Integer> getBooksInContainerCountLiveData(String collectionName) {
        return collectionGroupDao.getBooksInContainerCountLiveData(collectionName);
    }
}
