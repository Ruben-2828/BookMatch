package com.example.bookmatch.ui.main.collections;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bookmatch.R;
import com.example.bookmatch.adapter.AddBookToCollectionRecyclerViewAdapter;
import com.example.bookmatch.databinding.ActivityAddBookToCollectionBinding;
import com.example.bookmatch.model.Book;
import com.example.bookmatch.ui.main.BookViewModel;
import com.example.bookmatch.ui.main.BookViewModelFactory;
import com.example.bookmatch.ui.main.CollectionGroupViewModel;
import com.example.bookmatch.ui.main.CollectionGroupViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class AddBookToCollectionActivity extends AppCompatActivity {

    private ActivityAddBookToCollectionBinding binding;
    private BookViewModel bookViewModel;
    private String collectionName;
    private List<Book> selectedBooks = new ArrayList<>();
    private CollectionGroupViewModel collectionGroupViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBookToCollectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupViewModel();
        retrieveInfos();
        // Retrieve the list of saved books from the intent extras
        List<Book> savedBooksInContainer = getIntent().getParcelableArrayListExtra("savedBooksInContainer");

        setupRecyclerView(savedBooksInContainer);
        setupClickListeners();
    }

    private void setupViewModel() {
        BookViewModelFactory factory = new BookViewModelFactory(getApplication());
        bookViewModel = new ViewModelProvider(this, factory).get(BookViewModel.class);
        CollectionGroupViewModelFactory factoryCollectionGroup = new CollectionGroupViewModelFactory(getApplication());
        collectionGroupViewModel = new ViewModelProvider(this, factoryCollectionGroup).get(CollectionGroupViewModel.class);
    }

    private void retrieveInfos() {
        Bundle args = getIntent().getExtras();
        if (args != null) {
            collectionName = args.getString("collectionName");
        }
    }

    private void setupRecyclerView(List<Book> savedBooksInContainer) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.recyclerViewAddBookToCollection.setLayoutManager(linearLayoutManager);

        observeSavedBooks(savedBooksInContainer);
    }

    private void observeSavedBooks(List<Book> savedBooksInContainer) {
        LiveData<List<Book>> savedBooksLiveData = bookViewModel.getSavedBooksLiveData();
        savedBooksLiveData.observe(this, savedBooks -> {
            savedBooksLiveData.removeObservers(this);

            List<Book> filteredBooks = new ArrayList<>(savedBooks);
            filteredBooks.removeAll(savedBooksInContainer);

            AddBookToCollectionRecyclerViewAdapter recyclerViewAdapter = new AddBookToCollectionRecyclerViewAdapter(
                    filteredBooks, new AddBookToCollectionRecyclerViewAdapter.OnBookSelectedListener() {
                @Override
                public void onBookSelected(Book book, String action) {
                    if (action.equals("remove")) {
                        selectedBooks.remove(book);
                    } else if (action.equals("add")) {
                        selectedBooks.add(book);
                    }
                }
            }, findViewById(R.id.add_books));
            binding.recyclerViewAddBookToCollection.setAdapter(recyclerViewAdapter);
        });
    }

    private void setupClickListeners() {
        binding.goBackButton.setOnClickListener(v -> finish());

        binding.addBooks.setOnClickListener(v -> {
            for (Book book : selectedBooks) {
                collectionGroupViewModel.insertInCollection(collectionName, book);
            }

            setResult(RESULT_OK);
            finish();
        });
    }
}
