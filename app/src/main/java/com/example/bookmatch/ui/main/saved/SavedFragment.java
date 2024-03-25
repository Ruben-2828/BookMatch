package com.example.bookmatch.ui.main.saved;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.example.bookmatch.adapter.SavedRecyclerViewAdapter;
import com.example.bookmatch.databinding.FragmentSavedBinding;
import com.example.bookmatch.model.Book;
import com.example.bookmatch.ui.main.BookViewModel;
import com.example.bookmatch.ui.main.BookViewModelFactory;

import java.util.ArrayList;
import java.util.List;


public class SavedFragment extends Fragment {

    private FragmentSavedBinding binding;
    SavedRecyclerViewAdapter recyclerViewAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSavedBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BookViewModelFactory factory = new BookViewModelFactory(requireActivity().getApplication());
        BookViewModel bookViewModel = new ViewModelProvider(this, factory).get(BookViewModel.class);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        recyclerViewAdapter = new SavedRecyclerViewAdapter(new ArrayList<>(),null);
        binding.recyclerViewSaved.setLayoutManager(linearLayoutManager);
        binding.recyclerViewSaved.setAdapter(recyclerViewAdapter);

        bookViewModel.getSavedBooksLiveData().observe(getViewLifecycleOwner(), this::updateSavedBooksList);
    }

    private void updateSavedBooksList(List<Book> savedBooks) {
        recyclerViewAdapter.setBooks(savedBooks);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}


