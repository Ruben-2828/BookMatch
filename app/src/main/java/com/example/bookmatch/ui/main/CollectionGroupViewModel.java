package com.example.bookmatch.ui.main;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.bookmatch.data.repository.collections.grouping.CollectionGroupRepository;
import com.example.bookmatch.data.repository.collections.grouping.ICollectionGroupRepository;
import com.example.bookmatch.model.Book;
import com.example.bookmatch.model.CollectionGroup;

import java.util.List;

public class CollectionGroupViewModel extends AndroidViewModel {

    private final ICollectionGroupRepository collectionGroupRepository;

    public CollectionGroupViewModel(Application application) {
        super(application);
        this.collectionGroupRepository = new CollectionGroupRepository(application);
    }

    public boolean insertInCollection(CollectionGroup collectionGroup) {
        return collectionGroupRepository.insertCollectionGroup(collectionGroup);
    }

    public boolean insertInCollection(String collectionName, String bookId) {
        CollectionGroup collectionGroup = new CollectionGroup(collectionName, bookId);
        return collectionGroupRepository.insertCollectionGroup(collectionGroup);
    }

    public boolean insertInCollection(String collectionName, Book selectedBook) {
        CollectionGroup collectionGroup = new CollectionGroup(collectionName, selectedBook.getId());
        return collectionGroupRepository.insertCollectionGroup(collectionGroup);
    }

    public void deleteCollectionGroup(CollectionGroup collectionGroup) {
        collectionGroupRepository.deleteCollectionGroup(collectionGroup);
    }

    public void deleteCollectionGroup(String collectionName, String bookId) {
        CollectionGroup collectionGroup = new CollectionGroup(collectionName, bookId);
        collectionGroupRepository.deleteCollectionGroup(collectionGroup);
    }

    public LiveData<List<String>> getBooksInContainer(String containerName) {
        return collectionGroupRepository.getBookIdsInContainerLiveData(containerName);
    }

    public LiveData<Integer> getNumberBooksInContainer(String containerName) {
        return collectionGroupRepository.getBooksInContainerCountLiveData(containerName);
    }
}