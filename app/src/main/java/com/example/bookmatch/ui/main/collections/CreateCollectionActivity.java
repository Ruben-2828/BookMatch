package com.example.bookmatch.ui.main.collections;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.bookmatch.R;
import com.example.bookmatch.adapter.CollectionsRecyclerViewAdapter;
import com.example.bookmatch.databinding.ActivityCreateCollectionBinding;
import com.example.bookmatch.model.Collection;
import com.example.bookmatch.ui.main.BookViewModel;
import com.example.bookmatch.ui.main.BookViewModelFactory;
import com.example.bookmatch.ui.main.CollectionViewModel;
import com.example.bookmatch.ui.main.CollectionViewModelFactory;


import java.util.ArrayList;
import java.util.Objects;

public class CreateCollectionActivity extends AppCompatActivity {

    private ActivityCreateCollectionBinding binding;
    private CollectionViewModel collectionViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateCollectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        CollectionViewModelFactory factoryCollection = new CollectionViewModelFactory(this.getApplication());
        collectionViewModel = new ViewModelProvider(this, factoryCollection).get(CollectionViewModel.class);

        binding.button.setOnClickListener(view -> {

            String collectionName = Objects.requireNonNull(binding.collectionNameInput.getText()).toString().trim();
            String collectionDescription = Objects.requireNonNull(binding.collectionDescriptionInput.getText()).toString().trim();

            if (validateInput(collectionName, collectionDescription)) {
                Collection collection = new Collection(null, collectionName, collectionDescription);
                collectionViewModel.insertCollection(collection);
                collectionViewModel.getCountCollectionLiveData().observe(this, count -> {
                    if (count != null && count > 0) {
                        finish();

                    }
                });
            }

        });

        binding.goBackButton.setOnClickListener(view -> finish());
    }

    private boolean validateInput(String name, String description) {
        boolean isValid = true;

        if (name.isEmpty()) {
            binding.collectionName.setError(getString(R.string.error_collection_name_required));
            isValid = false;
        } else {
            binding.collectionName.setError(null);
        }

        if (description.isEmpty()) {
            binding.collectionDescription.setError(getString(R.string.error_collection_description_required));
            isValid = false;
        } else {
            binding.collectionDescription.setError(null);
        }

        return isValid;
    }

}
