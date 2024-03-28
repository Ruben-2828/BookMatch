package com.example.bookmatch.ui.main.book_page;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.bookmatch.databinding.ActivityFullscreenImageBinding;

public class FullscreenImageActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityFullscreenImageBinding binding = ActivityFullscreenImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String imageUri = getIntent().getStringExtra("image uri");
        String bookTitle = getIntent().getStringExtra("book title");
        boolean isBookSaved = getIntent().getBooleanExtra("book status", false);

        binding.bookTitleAppbar.setText(bookTitle);

        if (isBookSaved) {
            binding.savedButton.setVisibility(View.VISIBLE);
            binding.notSavedButton.setVisibility(View.INVISIBLE);
        } else {
            binding.savedButton.setVisibility(View.INVISIBLE);
            binding.notSavedButton.setVisibility(View.VISIBLE);
        }

        Glide.with(this).load(imageUri).into(binding.imageView);

        binding.goBackButton.setOnClickListener(v -> finish());
    }
}
