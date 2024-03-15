package com.example.bookmatch.ui.main.collections;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookmatch.databinding.ActivityAddBookToCollectionBinding;

public class AddBookToCollectionActivity extends AppCompatActivity {

    private ActivityAddBookToCollectionBinding binding;

    public AddBookToCollectionActivity() {
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBookToCollectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.goBackButton.setOnClickListener(v -> finish());
    }

}
