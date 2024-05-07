package com.example.bookmatch.data.repository.collections.grouping;

import androidx.lifecycle.LiveData;

import com.example.bookmatch.model.Book;
import com.example.bookmatch.model.CollectionContainer;
import com.example.bookmatch.model.CollectionGroup;

import java.util.List;

public interface ICollectionGroupRepository {
    boolean insertCollectionGroup(CollectionGroup collectionGroup);
    void deleteCollectionGroup(CollectionGroup collectionGroup);
    void deleteGroupsInContainer(String collectionName);
    LiveData<List<String>> getBookIdsInContainerLiveData(String containerName);
    LiveData<Integer> getBooksInContainerCountLiveData(String collectionName);
    LiveData<Boolean> isBookInContainerLiveData(String collectionName, String bookId);
}