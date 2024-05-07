package com.example.bookmatch.ui.main.collections;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.bookmatch.R;
import com.example.bookmatch.databinding.ActivityEditCollectionBinding;
import com.example.bookmatch.model.CollectionContainer;
import com.example.bookmatch.ui.main.CollectionContainerViewModel;
import com.example.bookmatch.ui.main.CollectionContainerViewModelFactory;
import com.example.bookmatch.ui.main.CollectionGroupViewModel;
import com.example.bookmatch.ui.main.CollectionGroupViewModelFactory;
import com.example.bookmatch.utils.Converters;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class EditCollectionActivity extends AppCompatActivity {

    private ActivityEditCollectionBinding binding;

    private CollectionContainerViewModel collectionViewModel;
    private CollectionGroupViewModel groupViewModel;

    private ActivityResultLauncher<String> galleryLauncher;
    private byte[] selectedImageData;
    private CollectionContainer pastCollectionContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEditCollectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        galleryLauncher();
        changePic();

        CollectionContainerViewModelFactory factoryCollection = new CollectionContainerViewModelFactory(this.getApplication());
        collectionViewModel = new ViewModelProvider(this, factoryCollection).get(CollectionContainerViewModel.class);

        CollectionGroupViewModelFactory factoryGroup = new CollectionGroupViewModelFactory(this.getApplication());
        groupViewModel = new ViewModelProvider(this, factoryGroup).get(CollectionGroupViewModel.class);

        Bundle args = getIntent().getExtras();
        if (args != null) {
            String collectionName = args.getString("collectionName");
            LiveData<CollectionContainer> pastCollectionContainerLiveData = collectionViewModel.getCollectionByNameLiveData(collectionName);
            pastCollectionContainerLiveData.observe(this, pastCollectionContainer -> {
                if (pastCollectionContainer != null) {
                    loadImageIntoCollection(pastCollectionContainer.getImageData());
                    binding.collectionEditNameInput.setText(pastCollectionContainer.getName());
                    binding.collectionEditDescriptionInput.setText(pastCollectionContainer.getDescription());

                    this.pastCollectionContainer = pastCollectionContainer;
                } else {
                    Snackbar.make(binding.getRoot(), "error", Snackbar.LENGTH_SHORT).show();
                    finish();
                }
            });
        }

        binding.editCollectionButton.setOnClickListener(view -> {
            String collectionName = Objects.requireNonNull(binding.collectionEditNameInput.getText()).toString().trim();
            String collectionDescription = Objects.requireNonNull(binding.collectionEditDescriptionInput.getText()).toString().trim();

            if (validateInput(collectionName, collectionDescription)) {
                AtomicBoolean hasChanges = new AtomicBoolean(false);

                if (!pastCollectionContainer.getDescription().equals(collectionDescription)) {
                    collectionViewModel.updateCollectionDescription(collectionName, collectionDescription);
                    hasChanges.set(true);
                }

                if (!Arrays.equals(pastCollectionContainer.getImageData(), selectedImageData)) {
                    collectionViewModel.updateCollectionImage(collectionName, selectedImageData);
                    hasChanges.set(true);
                }


                //TODO: fix that title doesn't upload instantly when going back to collection.
                if (!pastCollectionContainer.getName().equals(collectionName)) {
                    LiveData<Boolean> checkCollectionContainerExistsLiveData = collectionViewModel.collectionContainerExistsLiveData(collectionName);
                    checkCollectionContainerExistsLiveData.observe(this, exists -> {
                        checkCollectionContainerExistsLiveData.removeObservers(this);

                        if (exists) {
                            Snackbar.make(view, getString(R.string.error_collection_name_exists), Snackbar.LENGTH_SHORT).show();
                        } else {
                            collectionViewModel.updateCollectionName(collectionName, pastCollectionContainer.getName());
                            groupViewModel.updateContainerName(pastCollectionContainer.getName(), collectionName);
                            hasChanges.set(true);
                        }
                    });
                }

                if (!hasChanges.get()) {
                    Snackbar.make(view, getString(R.string.error_no_changes), Snackbar.LENGTH_SHORT).show();
                }

                Intent resultIntent = new Intent();
                resultIntent.putExtra("newCollectionName", collectionName);
                setResult(RESULT_OK, resultIntent);
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