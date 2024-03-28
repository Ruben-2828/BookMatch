package com.example.bookmatch.ui.main.collections;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bookmatch.adapter.CollectionsRecyclerViewAdapter;
import com.example.bookmatch.databinding.FragmentCollectionsBinding;
import com.example.bookmatch.model.Book;
import com.example.bookmatch.model.Collection;
import com.example.bookmatch.ui.main.CollectionViewModel;
import com.example.bookmatch.ui.main.CollectionViewModelFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CollectionsFragment extends Fragment {

    private FragmentCollectionsBinding binding;
    private CollectionsRecyclerViewAdapter adapter;

    private CollectionViewModel collectionViewModel;

    private final ActivityResultLauncher<Intent> createCollectionLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    String name = data.getStringExtra("collectionName");
                    String description = data.getStringExtra("collectionDescription");

                    Collection newCollection = new Collection(Objects.requireNonNull(name), description);
                    collectionViewModel.insertCollection(newCollection);

                    Toast.makeText(getActivity(), "Collection added successfully", Toast.LENGTH_SHORT).show();
                }
            }
    );

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCollectionsBinding.inflate(inflater, container, false);
        setupRecyclerView();
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CollectionViewModelFactory factoryCollection = new CollectionViewModelFactory(requireActivity().getApplication());
        collectionViewModel = new ViewModelProvider(this, factoryCollection).get(CollectionViewModel.class);

        collectionViewModel.getAllCollectionsLiveData().observe(getViewLifecycleOwner(), collections -> {
            adapter.setCollections(collections);
        });

        binding.fabCreateCollection.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CreateCollectionActivity.class);
            createCollectionLauncher.launch(intent);
        });
    }

    private void setupRecyclerView() {
        adapter = new CollectionsRecyclerViewAdapter(new ArrayList<>(),
                new CollectionsRecyclerViewAdapter.OnCollectionClickListener() {
                    @Override
                    public void onItemClick(Collection collection) {
                        Intent intent = new Intent(getActivity(), AddBookToCollectionActivity.class);
                        intent.putExtra("collectionId", collection.getName());
                        startActivity(intent);
                    }

                    @Override
                    public void onDeleteButtonClick(Collection collection) {
                        collectionViewModel.deleteCollection(collection);
                    }
                });

        binding.collectionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.collectionsRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
