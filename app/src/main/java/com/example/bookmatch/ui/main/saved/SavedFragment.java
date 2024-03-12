package com.example.bookmatch.ui.main.saved;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookmatch.R;
import com.example.bookmatch.adapter.SavedRecyclerViewAdapter;
import com.example.bookmatch.databinding.FragmentSavedBinding;
import com.example.bookmatch.model.Book;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class SavedFragment extends Fragment {

    private FragmentSavedBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSavedBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView savedRecyclerView = view.findViewById(R.id.recyclerView_saved);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        savedRecyclerView.setLayoutManager(linearLayoutManager);

        // TODO: extract current user saved projects
        List<Book> savedList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            savedList.add(new Book("title " + i,
                    "author "+ i,
                    "plot ezez abcd",
                    "fantasy",
                    "1792"));
        }

        SavedRecyclerViewAdapter recyclerViewAdapter = new SavedRecyclerViewAdapter(savedList,
                saved -> {
                    Snackbar.make(view, "Clicked on: "+ saved.getTitle(), Snackbar.LENGTH_LONG).show();
                });
        savedRecyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}