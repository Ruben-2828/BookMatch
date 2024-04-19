package com.example.bookmatch.ui.main.collections;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.bookmatch.R;
import com.example.bookmatch.databinding.ActivityCreateCollectionBinding;
import com.example.bookmatch.model.Collection;
import com.example.bookmatch.ui.main.CollectionViewModel;
import com.example.bookmatch.ui.main.CollectionViewModelFactory;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class CreateCollectionActivity extends AppCompatActivity {

    private ActivityCreateCollectionBinding binding;
    private CollectionViewModel collectionViewModel;
    private ActivityResultLauncher<String> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateCollectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        galleryLauncher();
        changePic();

        CollectionViewModelFactory factoryCollection = new CollectionViewModelFactory(this.getApplication());
        collectionViewModel = new ViewModelProvider(this, factoryCollection).get(CollectionViewModel.class);

        binding.button.setOnClickListener(view -> {
            String collectionImage = binding.collectionImage.getTag() != null ? binding.collectionImage.getTag().toString() : "";
            String collectionName = Objects.requireNonNull(binding.collectionNameInput.getText()).toString().trim();
            String collectionDescription = Objects.requireNonNull(binding.collectionDescriptionInput.getText()).toString().trim();

            if (validateInput(collectionName, collectionDescription)) {
                Collection collection = new Collection(collectionName, collectionDescription, collectionImage);
                if (!collectionViewModel.insertCollection(collection)) {
                    Snackbar.make(view, "Collection with this name already existing!", Snackbar.LENGTH_SHORT).show();
                }
                finish();
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

    private void galleryLauncher() {
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
            result -> {
                if (result != null) {
                    String selectedImagePath = result.toString();
                    loadImageIntoCollection(selectedImagePath);
                }
            });
    }

    private void loadImageIntoCollection(String imagePath) {
        Glide.with(this).load(imagePath).into(binding.collectionImage);
    }

    private void changePic() {
        binding.editPicButton.setOnClickListener(v -> galleryLauncher.launch("image/*"));
    }
}
