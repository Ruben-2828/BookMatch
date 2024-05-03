package com.example.bookmatch.data.repository.collections.container;

import androidx.lifecycle.LiveData;

import com.example.bookmatch.model.CollectionContainer;

import java.util.List;

public interface ICollectionContainerRepository {
    boolean insertCollectionContainer(CollectionContainer collection);
    void deleteCollectionContainer(CollectionContainer collection);
    CollectionContainer getCollectionContainerByName(String name);
    LiveData<Integer> getCountCollectionContainersLiveData();
    LiveData<List<CollectionContainer>> getAllCollectionContainersLiveData();
}
