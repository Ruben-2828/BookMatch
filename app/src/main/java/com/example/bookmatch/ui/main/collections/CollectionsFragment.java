package com.example.bookmatch.ui.main.collections;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bookmatch.adapter.CollectionsRecyclerViewAdapter;
import com.example.bookmatch.databinding.FragmentCollectionsBinding;
import com.example.bookmatch.model.Collection;

import java.util.ArrayList;
import java.util.List;

public class CollectionsFragment extends Fragment {

    private FragmentCollectionsBinding binding;
    private final List<Collection> collectionsList = new ArrayList<>();
    private CollectionsRecyclerViewAdapter adapter;

    @SuppressLint("NotifyDataSetChanged")
    private final ActivityResultLauncher<Intent> createCollectionLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    String id = "1"; //TODO: generate unique id
                    String name = data.getStringExtra("collectionName");
                    String description = data.getStringExtra("collectionDescription");
                    collectionsList.add(new Collection(id, name, description));
                    adapter.notifyDataSetChanged();
                }
            }
    );

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCollectionsBinding.inflate(inflater, container, false);
        setupRecyclerView();
        return binding.getRoot();
    }

    private void setupRecyclerView() {
        adapter = new CollectionsRecyclerViewAdapter(collectionsList);
        binding.collectionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.collectionsRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.fabCreateCollection.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CreateCollectionActivity.class);
            createCollectionLauncher.launch(intent);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
