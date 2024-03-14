package com.example.bookmatch.ui.main.collections;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookmatch.R;
import com.example.bookmatch.databinding.ActivityCreateCollectionBinding;


import java.util.Objects;

public class CreateCollectionActivity extends AppCompatActivity {

    private ActivityCreateCollectionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateCollectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.button.setOnClickListener(view -> {

            String collectionName = Objects.requireNonNull(binding.collectionNameInput.getText()).toString().trim();
            String collectionDescription = Objects.requireNonNull(binding.collectionDescriptionInput.getText()).toString().trim();

            if (validateInput(collectionName, collectionDescription)) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("collectionName", collectionName);
                resultIntent.putExtra("collectionDescription", collectionDescription);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }

        });
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
            binding.collectionDescriptionInput.setError(getString(R.string.error_collection_description_required));
            isValid = false;
        } else {
            binding.collectionDescriptionInput.setError(null);
        }

        return isValid;
    }
}
