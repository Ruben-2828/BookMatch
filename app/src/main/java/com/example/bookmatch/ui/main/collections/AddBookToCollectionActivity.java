package com.example.bookmatch.ui.main.collections;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bookmatch.adapter.AddBookToCollectionRecyclerViewAdapter;
import com.example.bookmatch.databinding.ActivityAddBookToCollectionBinding;
import com.example.bookmatch.model.Book;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddBookToCollectionActivity extends AppCompatActivity {

    private ActivityAddBookToCollectionBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBookToCollectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.recyclerViewAddBookToCollection.setLayoutManager(linearLayoutManager);

        List<Book> savedList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            savedList.add(new Book(
                    "Occhi nel Codice: Il Genio di Jouness Amsaet. Parte  " + i,
                    new ArrayList<String>(Arrays.asList("Paco Quackez", "acacaca")),
                    new ArrayList<String>(Arrays.asList("Avventura ezezez")),
                    "2024",
                    "https://heymondo.it/blog/wp-content/uploads/2023/07/Maldive-2.jpg",
                    false
            ));
        }

        AddBookToCollectionRecyclerViewAdapter recyclerViewAdapter = new AddBookToCollectionRecyclerViewAdapter(savedList
        );

        binding.recyclerViewAddBookToCollection.setAdapter(recyclerViewAdapter);

        binding.goBackButton.setOnClickListener(v -> finish());

        binding.addBooks.setOnClickListener(v -> finish());
    }
}
