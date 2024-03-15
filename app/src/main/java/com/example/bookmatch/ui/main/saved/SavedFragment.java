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



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        binding.recyclerViewSaved.setLayoutManager(linearLayoutManager);

        // TODO: extract current user saved books
        List<Book> savedList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            savedList.add(new Book(String.valueOf(i),
                    "Nusy eepy" + i,
                    "Rubini",
                    "La nusy fa eepy",
                    "Eepy",
                    "2023",
                    "https://heymondo.it/blog/wp-content/uploads/2023/07/Maldive-2.jpg"));
        }

        SavedRecyclerViewAdapter recyclerViewAdapter = new SavedRecyclerViewAdapter(savedList,
                saved -> {
                    Bundle bundle = new Bundle();
                    bundle.putString("title", saved.getTitle());
                    bundle.putString("author", saved.getAuthor());
                    bundle.putString("plot", saved.getPlot());
                    bundle.putString("genre", saved.getGenre());
                    bundle.putString("year", saved.getPublicationYear());
                    bundle.putString("cover", saved.getCover());

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