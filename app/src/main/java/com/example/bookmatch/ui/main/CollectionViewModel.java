package com.example.bookmatch.ui.main;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookmatch.data.repository.collections.CollectionRepository;
import com.example.bookmatch.data.repository.collections.ICollectionRepository;
import com.example.bookmatch.model.Collection;

import java.util.List;


public class CollectionViewModel extends ViewModel {

    private final ICollectionRepository collectionRepository;

    public CollectionViewModel(Application application) {
        this.collectionRepository = new CollectionRepository(application);
    }

    public LiveData<List<Collection>> getAllCollectionsLiveData() {
        return collectionRepository.getAllCollectionsLiveData();
    }

    public LiveData<Integer> getCountCollectionLiveData() {
        return collectionRepository.getCountCollectionLiveData();
    }

    public void insertCollection(Collection collection) {
        collectionRepository.insertCollection(collection);
    }
}

