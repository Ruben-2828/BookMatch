package com.example.bookmatch.ui.main.book_page;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.bookmatch.R;
import com.example.bookmatch.databinding.ActivityFullscreenImageBinding;
import com.jsibbold.zoomage.ZoomageView;

public class FullScreenImageActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityFullscreenImageBinding binding = ActivityFullscreenImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ZoomageView imageView = findViewById(R.id.imageView);

        Glide.with(FullScreenImageActivity.this).load(getIntent().getStringExtra("image uri")).into(imageView);

        binding.goBackButton.setOnClickListener(v -> finish());
    }
}
