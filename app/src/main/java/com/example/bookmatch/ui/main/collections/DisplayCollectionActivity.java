package com.example.bookmatch.ui.main.collections;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

import java.util.List;

public class DisplayCollectionActivity extends AppCompatActivity {
    private static final int ADD_BOOKS_REQUEST_CODE = 1;
    private ActivityResultLauncher<Intent> addBooksLauncher;
    private ActivityDisplayCollectionBinding binding;
    private CollectionGroupViewModel collectionGroupViewModel;
    private String collectionName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDisplayCollectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupViewModel();
        retrieveInfos();
        setupRecyclerView();
        setupAddBooksLauncher();
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
    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.recyclerViewDisplayCollectionBooks.setLayoutManager(linearLayoutManager);

        observeSavedBooks();
    }

    private void observeSavedBooks() {
        LiveData<List<Book>> savedGroupOfBooksLiveData = collectionGroupViewModel.getBooksInContainer(collectionName);
        savedGroupOfBooksLiveData.observe(this, savedBooks -> {
            savedGroupOfBooksLiveData.removeObservers(this);

            CollectionGroupsRecyclerViewAdapter recyclerViewAdapter = new CollectionGroupsRecyclerViewAdapter(
                    savedBooks, new CollectionGroupsRecyclerViewAdapter.OnBookSelectedListener() {
                @Override
                public void onBookSelected(List<Book> selectedBooks) {
                    selectedBooks.clear();
                    selectedBooks.addAll(selectedBooks);
                }
            });
            binding.recyclerViewDisplayCollectionBooks.setAdapter(recyclerViewAdapter);
        });
    }

    private void setupAddBooksLauncher() {
        addBooksLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            List<Book> selectedBooks = data.getParcelableArrayListExtra("selectedBooks");
                            if (selectedBooks != null) {
                                for (Book book : selectedBooks) {
                                    collectionGroupViewModel.insertInCollection(collectionName, book);
                                }
                            }
                        }
                    }
                });
    }

    private void setupClickListeners() {
        binding.topAppBar.setOnClickListener(v -> finish());
        binding.topAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.add_books_to_collection) {
                Intent intent = new Intent(this, AddBookToCollectionActivity.class);
                intent.putExtra("collectionName", collectionName);
                addBooksLauncher.launch(intent);
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
