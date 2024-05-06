package com.example.bookmatch.ui.main.collections;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bookmatch.R;
import com.example.bookmatch.adapter.AddBookToCollectionRecyclerViewAdapter;
import com.example.bookmatch.adapter.CollectionGroupsRecyclerViewAdapter;
import com.example.bookmatch.databinding.ActivityDisplayCollectionBinding;
import com.example.bookmatch.model.Book;
import com.example.bookmatch.ui.main.BookViewModel;
import com.example.bookmatch.ui.main.BookViewModelFactory;
import com.example.bookmatch.ui.main.CollectionGroupViewModel;
import com.example.bookmatch.ui.main.CollectionGroupViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class DisplayCollectionActivity extends AppCompatActivity {
    private ActivityDisplayCollectionBinding binding;
    private CollectionGroupViewModel collectionGroupViewModel;
    private String collectionName;
    private BookViewModel bookViewModel;

    ActivityResultLauncher<Intent> addBookToCollectionLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    observeSavedBooks();
                }
            });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDisplayCollectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupViewModel();
        retrieveInfos();
        setupRecyclerView();
        setupClickListeners();
    }

    private void retrieveInfos() {
        Bundle args = getIntent().getExtras();
        if (args != null) {
            collectionName = args.getString("collectionName");

            binding.topAppBar.setTitle("Collection " + collectionName);
        }
    }

    private void setupViewModel() {
        CollectionGroupViewModelFactory factory = new CollectionGroupViewModelFactory(getApplication());
        collectionGroupViewModel = new ViewModelProvider(this, factory).get(CollectionGroupViewModel.class);
        BookViewModelFactory factoryBook = new BookViewModelFactory(getApplication());
        bookViewModel = new ViewModelProvider(this, factoryBook).get(BookViewModel.class);
    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.recyclerViewDisplayCollectionBooks.setLayoutManager(linearLayoutManager);

        observeSavedBooks();
    }

    private void observeSavedBooks() {
        LiveData<List<String>> savedGroupOfBooksLiveData = collectionGroupViewModel.getBooksInContainer(collectionName);
        savedGroupOfBooksLiveData.observe(this, savedBookIds -> {
            savedGroupOfBooksLiveData.removeObservers(this);

            List<Book> booksSavedInCollection = bookViewModel.getBooksByIds(savedBookIds);

            CollectionGroupsRecyclerViewAdapter recyclerViewAdapter = new CollectionGroupsRecyclerViewAdapter(
                    booksSavedInCollection, new CollectionGroupsRecyclerViewAdapter.OnBookSelectedListener() {
                @Override
                public void onBookSelected(List<Book> selectedBooks) {
                    selectedBooks.clear();
                    selectedBooks.addAll(selectedBooks);
                }
            });
            binding.recyclerViewDisplayCollectionBooks.setAdapter(recyclerViewAdapter);
        });
    }

    private void setupClickListeners() {
        binding.topAppBar.setOnClickListener(v -> finish());
        binding.topAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.add_books_to_collection) {
                LiveData<List<String>> savedGroupOfBooksLiveData = collectionGroupViewModel.getBooksInContainer(collectionName);
                savedGroupOfBooksLiveData.observe(this, savedBookIds-> {
                    savedGroupOfBooksLiveData.removeObservers(this);

                    List<Book> booksSavedInCollection = bookViewModel.getBooksByIds(savedBookIds);

                    Intent intent = new Intent(this, AddBookToCollectionActivity.class);
                    intent.putExtra("collectionName", collectionName);
                    intent.putParcelableArrayListExtra("savedBooksInContainer", new ArrayList<>(booksSavedInCollection));

                    addBookToCollectionLauncher.launch(intent);
                });
                return true;
            }
            if(item.getItemId() == R.id.edit_collection){
                //edit collection
                return true;
            }
            return false;
        });
    }
}
