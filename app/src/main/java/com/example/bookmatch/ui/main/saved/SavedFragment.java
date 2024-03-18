package com.example.bookmatch.ui.main.saved;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bookmatch.R;
import com.example.bookmatch.adapter.SavedRecyclerViewAdapter;
import com.example.bookmatch.databinding.FragmentSavedBinding;
import com.example.bookmatch.model.Book;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SavedFragment extends Fragment {

    private FragmentSavedBinding binding;
    private SharedViewModel sharedViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSavedBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        binding.recyclerViewSaved.setLayoutManager(linearLayoutManager);

        sharedViewModel.getSavedBooks().observe(getViewLifecycleOwner(), this::updateSavedBooksList);

        //TODO: Implement with Dao not with SharedViewModel

    }

    private void updateSavedBooksList(List<Book> savedBooks) {
        SavedRecyclerViewAdapter recyclerViewAdapter = new SavedRecyclerViewAdapter(savedBooks,
                saved -> {
                    Bundle bundle = new Bundle();;
                    bundle.putParcelable("book", saved);

                    NavController navController = Navigation.findNavController(requireView());
                    navController.navigate(R.id.action_navigation_saved_to_navigation_book, bundle);
                });
        binding.recyclerViewSaved.setAdapter(recyclerViewAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
