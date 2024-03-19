package com.example.bookmatch.ui.main.saved;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookmatch.model.Book;

import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<List<Book>> savedBooks = new MutableLiveData<>();

    public SharedViewModel() {
        savedBooks.setValue(new ArrayList<>());
    }

    public void saveBook(Book book) {
        List<Book> currentList = savedBooks.getValue();
        if (currentList == null) {
            currentList = new ArrayList<>();
        }
        currentList.add(book);
        savedBooks.setValue(currentList);
    }

    public LiveData<List<Book>> getSavedBooks() {
        return savedBooks;
    }
}
