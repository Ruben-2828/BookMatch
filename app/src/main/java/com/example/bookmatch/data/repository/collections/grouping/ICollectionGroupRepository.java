package com.example.bookmatch.data.repository.collections.grouping;

import androidx.lifecycle.LiveData;
import com.example.bookmatch.model.CollectionGroup;

import java.util.List;

public interface ICollectionGroupRepository {
    boolean insertCollectionGroup(CollectionGroup collectionGroup);
    void deleteCollectionGroup(CollectionGroup collectionGroup);
    void deleteGroupsInContainer(String collectionName);
    void updateContainerName(String oldName, String newName);
    LiveData<List<String>> getBookIdsInContainerLiveData(String containerName);
    LiveData<Integer> getBooksInContainerCountLiveData(String collectionName);
    LiveData<Boolean> isBookInContainerLiveData(String collectionName, String bookId);
}