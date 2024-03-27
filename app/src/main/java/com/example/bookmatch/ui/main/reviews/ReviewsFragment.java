package com.example.bookmatch.ui.main.reviews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.bookmatch.adapter.ReviewsRecyclerViewAdapter;
import com.example.bookmatch.databinding.FragmentReviewsBinding;
import com.example.bookmatch.model.Book;
import com.example.bookmatch.ui.main.BookViewModel;
import com.example.bookmatch.ui.main.BookViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class ReviewsFragment extends Fragment {

    private FragmentReviewsBinding binding;
    ReviewsRecyclerViewAdapter recyclerViewAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentReviewsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BookViewModelFactory factory = new BookViewModelFactory(requireActivity().getApplication());
        BookViewModel bookViewModel = new ViewModelProvider(this, factory).get(BookViewModel.class);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        recyclerViewAdapter = new ReviewsRecyclerViewAdapter(new ArrayList<>());
        binding.recyclerViewReviews.setLayoutManager(linearLayoutManager);
        binding.recyclerViewReviews.setAdapter(recyclerViewAdapter);

        bookViewModel.getReviewedBooksLiveData().observe(getViewLifecycleOwner(), this::updateReviewedBooksList);
    }

    private void updateReviewedBooksList(List<Book> savedBooks) {
        recyclerViewAdapter.setBooks(savedBooks);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
