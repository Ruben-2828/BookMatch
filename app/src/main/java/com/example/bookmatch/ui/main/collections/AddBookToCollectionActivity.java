package com.example.bookmatch.ui.main.collections;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bookmatch.adapter.AddBookToCollectionRecyclerViewAdapter;
import com.example.bookmatch.adapter.CollectionsRecyclerViewAdapter;
import com.example.bookmatch.databinding.ActivityAddBookToCollectionBinding;
import com.example.bookmatch.model.Book;
import com.example.bookmatch.ui.main.BookViewModel;
import com.example.bookmatch.ui.main.BookViewModelFactory;

import java.util.List;

public class AddBookToCollectionActivity extends AppCompatActivity {

    private ActivityAddBookToCollectionBinding binding;
    private BookViewModel bookViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBookToCollectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupViewModel();
        setupRecyclerView();
        setupClickListeners();
    }

    private void setupViewModel() {
        BookViewModelFactory factory = new BookViewModelFactory(getApplication());
        bookViewModel = new ViewModelProvider(this, factory).get(BookViewModel.class);
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

            AddBookToCollectionRecyclerViewAdapter recyclerViewAdapter = new AddBookToCollectionRecyclerViewAdapter(savedBooks, new AddBookToCollectionRecyclerViewAdapter.OnBookSelectedListener() {
                @Override
                public void onBookSelected(List<Book> selectedBooks) {
                    selectedBooks.clear();
                    selectedBooks.addAll(selectedBooks);
                }
            });
            binding.recyclerViewAddBookToCollection.setAdapter(recyclerViewAdapter);
        });
    }




    private void setupClickListeners() {
        binding.goBackButton.setOnClickListener(v -> finish());
        binding.addBooks.setOnClickListener(v -> addBooksToCollection());
    }


    private void addBooksToCollection() {
        finish();
    }
}
