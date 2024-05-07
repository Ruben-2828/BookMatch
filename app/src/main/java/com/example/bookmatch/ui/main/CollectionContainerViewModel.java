package com.example.bookmatch.ui.main;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.bookmatch.data.database.collections.container.CollectionContainerRoomDatabase;
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

    public boolean insertCollection(CollectionContainer collection) {
        return collectionRepository.insertCollectionContainer(collection);
    }

    public void deleteCollection(CollectionContainer collection) {
        collectionRepository.deleteCollectionContainer(collection);
    }

    public void updateCollectionContainer(String name, String description, byte[] image, String oldName) {
        collectionRepository.updateCollectionContainer(name, description, image, oldName);
    }

    public void updateCollectionName(String name, String oldName) {
        collectionRepository.updateCollectionName(name, oldName);
    }

    public void updateCollectionDescription(String name, String description) {
        collectionRepository.updateCollectionDescription(name, description);
    }

    public void updateCollectionImage(String name, byte[] image) {
        collectionRepository.updateCollectionImage(name, image);
    }

    public LiveData<Boolean> collectionContainerExistsLiveData(String name) {
        return collectionRepository.collectionContainerExistsLiveData(name);
    }

    public LiveData<List<CollectionContainer>> getAllCollectionsLiveData() {
        return collectionRepository.getAllCollectionContainersLiveData();
    }

    public LiveData<Integer> getCountCollectionLiveData() {
        return collectionRepository.getCountCollectionContainersLiveData();
    }

    public LiveData<CollectionContainer> getCollectionByNameLiveData(String name) {
        return collectionRepository.getCollectionContainerByNameLiveData(name);
    }
}
