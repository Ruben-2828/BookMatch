package com.example.bookmatch.ui.main;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.bookmatch.data.repository.collections.container.CollectionContainerRepository;
import com.example.bookmatch.data.repository.collections.container.ICollectionContainerRepository;
import com.example.bookmatch.model.CollectionContainer;
import java.util.List;

public class CollectionContainerViewModel extends AndroidViewModel {

    private final ICollectionContainerRepository collectionRepository;

    public CollectionContainerViewModel(Application application) {
        super(application);
        this.collectionRepository = new CollectionContainerRepository(application);
    }

    public LiveData<List<CollectionContainer>> getAllCollectionsLiveData() {
        return collectionRepository.getAllCollectionContainersLiveData();
    }

    public LiveData<Integer> getCountCollectionLiveData() {
        return collectionRepository.getCountCollectionContainersLiveData();
    }

    public CollectionContainer getCollectionByName(String name) {
        return collectionRepository.getCollectionContainerByName(name);
    }

    public boolean insertCollection(CollectionContainer collection) {
        return collectionRepository.insertCollectionContainer(collection);
    }

    public void deleteCollection(CollectionContainer collection) {
        collectionRepository.deleteCollectionContainer(collection);
    }
}
