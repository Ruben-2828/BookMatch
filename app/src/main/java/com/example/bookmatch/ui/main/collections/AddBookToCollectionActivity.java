package com.example.bookmatch.ui.main.collections;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bookmatch.adapter.AddBookToCollectionRecyclerViewAdapter;
import com.example.bookmatch.databinding.ActivityAddBookToCollectionBinding;
import com.example.bookmatch.model.Book;
import com.example.bookmatch.ui.main.BookViewModel;
import com.example.bookmatch.ui.main.BookViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class AddBookToCollectionActivity extends AppCompatActivity {

    private ActivityAddBookToCollectionBinding binding;
    private BookViewModel bookViewModel;
    private String collectionName;
    private List<Book> selectedBooks = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBookToCollectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupViewModel();
        retrieveInfos();
        setupRecyclerView();
        setupClickListeners();
    }

    private void setupViewModel() {
        BookViewModelFactory factory = new BookViewModelFactory(getApplication());
        bookViewModel = new ViewModelProvider(this, factory).get(BookViewModel.class);
    }

    private void retrieveInfos() {
        Bundle args = getIntent().getExtras();
        if (args != null) {
            collectionName = args.getString("collectionName");
        }
    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.recyclerViewAddBookToCollection.setLayoutManager(linearLayoutManager);

        observeSavedBooks();
    }

    private void observeSavedBooks() {
        LiveData<List<Book>> savedBooksLiveData = bookViewModel.getSavedBooksLiveData();
        savedBooksLiveData.observe(this, savedBooks -> {
            savedBooksLiveData.removeObservers(this);

            AddBookToCollectionRecyclerViewAdapter recyclerViewAdapter = new AddBookToCollectionRecyclerViewAdapter(
                    collectionName, savedBooks, new AddBookToCollectionRecyclerViewAdapter.OnBookSelectedListener() {
                @Override
                public void onBookSelected(Book book) {
                    if (selectedBooks.contains(book)) {
                        selectedBooks.remove(book);
                    } else {
                        selectedBooks.add(book);
                    }
                }
            });
            binding.recyclerViewAddBookToCollection.setAdapter(recyclerViewAdapter);
        });
    }

    private void setupClickListeners() {
        binding.goBackButton.setOnClickListener(v -> finish());

        binding.addBooks.setOnClickListener(v -> {
            List<Book> selectedBooks = ((AddBookToCollectionRecyclerViewAdapter) binding.recyclerViewAddBookToCollection.getAdapter()).getSelectedBooks();
            Log.d("AddBookToCollectionActivity", "Selected Books:");
            for (Book book : selectedBooks) {
                Log.d("AddBookToCollectionActivity", "- Title: " + book.getTitle());
                Log.d("AddBookToCollectionActivity", "  Authors: " + book.getAuthors());
                // Add more book properties as needed
            }

            Intent resultIntent = new Intent();
            resultIntent.putParcelableArrayListExtra("selectedBooks", new ArrayList<>(selectedBooks));
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
