package com.example.bookmatch.ui.main;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.bookmatch.data.repository.collections.CollectionRepository;
import com.example.bookmatch.data.repository.collections.ICollectionRepository;
import com.example.bookmatch.model.Collection;
import java.util.List;

public class CollectionViewModel extends AndroidViewModel {

    private final ICollectionRepository collectionRepository;

    public CollectionViewModel(Application application) {
        super(application);
        this.collectionRepository = new CollectionRepository(application);
    }

    public LiveData<List<Collection>> getAllCollectionsLiveData() {
        return collectionRepository.getAllCollectionsLiveData();
    }

    public LiveData<Integer> getCountCollectionLiveData() {
        return collectionRepository.getCountCollectionLiveData();
    }

    public Collection getCollectionByName(String name) {
        return collectionRepository.getCollectionByName(name);
    }

    public boolean insertCollection(Collection collection) {
        return collectionRepository.insertCollection(collection);
    }

    public void deleteCollection(Collection collection) {
        collectionRepository.deleteCollection(collection);
    }
}
