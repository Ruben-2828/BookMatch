package com.example.bookmatch.ui.main.collections;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.bookmatch.R;
import com.example.bookmatch.databinding.ActivityCreateCollectionBinding;
import com.example.bookmatch.model.CollectionContainer;
import com.example.bookmatch.ui.main.CollectionContainerViewModel;
import com.example.bookmatch.ui.main.CollectionContainerViewModelFactory;
import com.example.bookmatch.utils.Converters;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class CreateCollectionActivity extends AppCompatActivity {

    private ActivityCreateCollectionBinding binding;
    private CollectionContainerViewModel collectionViewModel;
    private ActivityResultLauncher<String> galleryLauncher;
    private byte[] selectedImageData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateCollectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        galleryLauncher();
        changePic();

        CollectionContainerViewModelFactory factoryCollection = new CollectionContainerViewModelFactory(this.getApplication());
        collectionViewModel = new ViewModelProvider(this, factoryCollection).get(CollectionContainerViewModel.class);

        binding.button.setOnClickListener(view -> {
            String collectionName = Objects.requireNonNull(binding.collectionNameInput.getText()).toString().trim();
            String collectionDescription = Objects.requireNonNull(binding.collectionDescriptionInput.getText()).toString().trim();

            if (validateInput(collectionName, collectionDescription)) {
                CollectionContainer collection = new CollectionContainer(collectionName, collectionDescription, selectedImageData);
                if (!collectionViewModel.insertCollection(collection)) {
                    Snackbar.make(view, "CollectionContainer with this name already existing!", Snackbar.LENGTH_SHORT).show();
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
                try {
                    InputStream inputStream = getContentResolver().openInputStream(result);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    byte[] imageData = Converters.fromBitmap(bitmap);
                    loadImageIntoCollection(imageData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
    }

    private void loadImageIntoCollection(byte[] imageData) {
        Bitmap bitmap = Converters.toBitmap(imageData);
        binding.collectionImage.setImageBitmap(bitmap);
        selectedImageData = imageData;
    }

    private void changePic() {
        binding.editPicButton.setOnClickListener(v -> galleryLauncher.launch("image/*"));
    }
}
