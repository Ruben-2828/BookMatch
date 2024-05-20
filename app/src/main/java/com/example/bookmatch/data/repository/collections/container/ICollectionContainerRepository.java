package com.example.bookmatch.data.repository.collections.container;

import androidx.lifecycle.LiveData;

import com.example.bookmatch.model.CollectionContainer;

import java.util.List;

public interface ICollectionContainerRepository {
    boolean insertCollectionContainer(CollectionContainer collection);
    void deleteCollectionContainer(CollectionContainer collection);
    void updateCollectionContainer(String name, String description, byte[] image, String oldName);
    void updateCollectionName(String name, String oldName);
    void updateCollectionDescription(String name, String description);
    void updateCollectionImage(String name, byte[] image);
    Boolean collectionContainerExists(String name);
    LiveData<CollectionContainer> getCollectionContainerByNameLiveData(String name);
    LiveData<Integer> getCountCollectionContainersLiveData();
    LiveData<List<CollectionContainer>> getAllCollectionContainersLiveData();
}
