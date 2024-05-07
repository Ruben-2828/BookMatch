package com.example.bookmatch.ui.main.collections;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bookmatch.R;
import com.example.bookmatch.adapter.CollectionGroupsRecyclerViewAdapter;
import com.example.bookmatch.databinding.ActivityDisplayCollectionBinding;
import com.example.bookmatch.model.Book;
import com.example.bookmatch.model.CollectionContainer;
import com.example.bookmatch.ui.main.BookViewModel;
import com.example.bookmatch.ui.main.BookViewModelFactory;
import com.example.bookmatch.ui.main.CollectionContainerViewModel;
import com.example.bookmatch.ui.main.CollectionContainerViewModelFactory;
import com.example.bookmatch.ui.main.CollectionGroupViewModel;
import com.example.bookmatch.ui.main.CollectionGroupViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class DisplayCollectionActivity extends AppCompatActivity {
    private ActivityDisplayCollectionBinding binding;

    private CollectionGroupViewModel collectionGroupViewModel;
    private CollectionContainerViewModel collectionContainerViewModel;
    private BookViewModel bookViewModel;

    String collectionName;

    ActivityResultLauncher<Intent> addBookToCollectionLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    observeSavedBooks();
                }
            });

    ActivityResultLauncher<Intent> editCollectionLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        String newCollectionName = data.getStringExtra("newCollectionName");
                        if (newCollectionName != null) {
                            collectionName = newCollectionName;
                            binding.topAppBar.setTitle(collectionName);
                        }
                    }
                }
            });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDisplayCollectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupViewModel();
        retrieveInfo();
        setupRecyclerView();
        setupClickListeners();
    }

    private void setupViewModel() {
        CollectionGroupViewModelFactory factory = new CollectionGroupViewModelFactory(getApplication());
        collectionGroupViewModel = new ViewModelProvider(this, factory).get(CollectionGroupViewModel.class);

        CollectionContainerViewModelFactory factoryContainer = new CollectionContainerViewModelFactory(getApplication());
        collectionContainerViewModel = new ViewModelProvider(this, factoryContainer).get(CollectionContainerViewModel.class);

        BookViewModelFactory factoryBook = new BookViewModelFactory(getApplication());
        bookViewModel = new ViewModelProvider(this, factoryBook).get(BookViewModel.class);
    }

    private void retrieveInfo() {
        Bundle args = getIntent().getExtras();
        if (args != null) {
            collectionName = args.getString("collectionName");

            if (collectionName != null) {
                binding.topAppBar.setTitle(collectionName);
            }
        }
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


            Log.d("DisplayCollectionActivity", "BookIds " + savedBookIds);
            for (String bookId : savedBookIds) {
                LiveData<Boolean> isBookSavedLiveData = bookViewModel.isBookSavedLiveData(bookId);
                isBookSavedLiveData.observe(this, isBookSaved -> {
                    if (!isBookSaved) {
                        collectionGroupViewModel.deleteCollectionGroup(collectionName, bookId);
                        // log that the book is being eliminated
                        //Log.d("DisplayCollectionActivity", "Book " + bookId + " eliminated from " + collectionContainer.getName());
                    }
                });
            }

            List<Book> booksSavedInCollection = bookViewModel.getBooksByIds(savedBookIds);

            CollectionGroupsRecyclerViewAdapter recyclerViewAdapter = new CollectionGroupsRecyclerViewAdapter(
                    booksSavedInCollection, new CollectionGroupsRecyclerViewAdapter.OnBookSelectedListener() {
                @Override
                public void onBookSelected(String bookId, String action) {
                    if (action.equals("remove")) {
                        collectionGroupViewModel.deleteCollectionGroup(collectionName, bookId);
                    } else if (action.equals("add")) {
                        collectionGroupViewModel.insertInCollection(collectionName, bookId);
                    }
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
                Intent intent = new Intent(this, EditCollectionActivity.class);
                intent.putExtra("collectionName", collectionName);

                editCollectionLauncher.launch(intent);
                return true;
            }
            return false;
        });
    }
}
