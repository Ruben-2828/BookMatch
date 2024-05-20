package com.example.bookmatch.ui.main.collections;

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

import com.example.bookmatch.adapter.CollectionContainersRecyclerViewAdapter;
import com.example.bookmatch.databinding.FragmentCollectionsBinding;
import com.example.bookmatch.model.CollectionContainer;
import com.example.bookmatch.ui.main.CollectionContainerViewModel;
import com.example.bookmatch.ui.main.CollectionContainerViewModelFactory;
import com.example.bookmatch.ui.main.CollectionGroupViewModel;
import com.example.bookmatch.ui.main.CollectionGroupViewModelFactory;

import java.util.ArrayList;
import java.util.Objects;

public class CollectionsFragment extends Fragment {

    private FragmentCollectionsBinding binding;
    private CollectionContainersRecyclerViewAdapter adapter;

    private CollectionContainerViewModel collectionViewModel;
    private CollectionGroupViewModel collectionGroupViewModel;

    private final ActivityResultLauncher<Intent> createCollectionLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    String name = data.getStringExtra("collectionName");
                    String description = data.getStringExtra("collectionDescription");
                    byte[] imageData = data.getByteArrayExtra("collectionImageData");

                    CollectionContainer newCollection = new CollectionContainer(Objects.requireNonNull(name), description, imageData);
                    collectionViewModel.insertCollection(newCollection);

                    Toast.makeText(getActivity(), "CollectionContainer added successfully", Toast.LENGTH_SHORT).show();
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

        CollectionContainerViewModelFactory factoryCollection = new CollectionContainerViewModelFactory(requireActivity().getApplication());
        collectionViewModel = new ViewModelProvider(this, factoryCollection).get(CollectionContainerViewModel.class);

        CollectionGroupViewModelFactory factory = new CollectionGroupViewModelFactory(requireActivity().getApplication());
        collectionGroupViewModel = new ViewModelProvider(this, factory).get(CollectionGroupViewModel.class);

        collectionViewModel.getAllCollectionsLiveData().observe(getViewLifecycleOwner(), collections -> {
            adapter.setCollections(collections);
        });

        binding.fabCreateCollection.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CreateCollectionActivity.class);
            createCollectionLauncher.launch(intent);
        });
    }

    private void setupRecyclerView() {
        adapter = new CollectionContainersRecyclerViewAdapter(new ArrayList<>(),
                new CollectionContainersRecyclerViewAdapter.OnCollectionClickListener() {
                    @Override
                    public void onItemClick(CollectionContainer collection) {
                        Intent intent = new Intent(getActivity(), DisplayCollectionActivity.class);
                        intent.putExtra("collectionName", collection.getName());
                        startActivity(intent);
                    }

                    @Override
                    public void onDeleteButtonClick(CollectionContainer collection) {
                        collectionViewModel.deleteCollection(collection);
                        collectionGroupViewModel.deleteGroupsInContainer(collection.getName());
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
