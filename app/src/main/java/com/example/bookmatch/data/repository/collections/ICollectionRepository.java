package com.example.bookmatch.data.repository.collections;

import androidx.lifecycle.LiveData;

import com.example.bookmatch.model.Collection;

import java.util.List;

public interface ICollectionRepository {

    void insertCollection(Collection collection);
    LiveData<Integer> getCountCollectionLiveData();

    LiveData<List<Collection>> getAllCollectionsLiveData();
}
