package com.example.bookmatch.data.repository.collections;

import androidx.lifecycle.LiveData;

import com.example.bookmatch.model.Collection;

import java.util.List;

public interface ICollectionRepository {

    boolean insertCollection(Collection collection);

    void deleteCollection(Collection collection);
    Collection getCollectionByName(String name);
    LiveData<Integer> getCountCollectionLiveData();
    LiveData<List<Collection>> getAllCollectionsLiveData();
}
