package com.example.bookmatch.ui.main.reviews;

import android.app.Application;
import android.content.Intent;
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
import com.example.bookmatch.adapter.ReviewsRecyclerViewAdapter;
import com.example.bookmatch.adapter.SavedRecyclerViewAdapter;
import com.example.bookmatch.databinding.FragmentReviewsBinding;
import com.example.bookmatch.model.Book;
import com.example.bookmatch.ui.main.BookViewModel;
import com.example.bookmatch.ui.main.BookViewModelFactory;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ReviewsFragment extends Fragment {

    private FragmentReviewsBinding binding;
    private ReviewsRecyclerViewAdapter recyclerViewAdapter;
    private BookViewModel bookViewModel;

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
        bookViewModel = new ViewModelProvider(this, factory).get(BookViewModel.class);

        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.nav_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        recyclerViewAdapter = new ReviewsRecyclerViewAdapter(createOnItemClickListener(view, bottomNavigationView));
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

    private ReviewsRecyclerViewAdapter.OnItemClickListener createOnItemClickListener(View view, BottomNavigationView bottomNavigationView) {

        return new ReviewsRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onDeleteButtonClick(int position) {
                final Book removedBook = recyclerViewAdapter.removeBook(position);
                recyclerViewAdapter.notifyItemRemoved(position);

                Snackbar snackbar = Snackbar.make(view,
                        removedBook.getTitle() + getString(R.string.removed_from_reviews),
                        Snackbar.LENGTH_SHORT).setAnchorView(bottomNavigationView);

                snackbar.setAnchorView(bottomNavigationView);

                snackbar.setAction(R.string.undo, v -> {
                    recyclerViewAdapter.addBook(position, removedBook);
                    recyclerViewAdapter.notifyItemInserted(position);
                });
                snackbar.addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                            BookViewModel bookViewModel = new BookViewModel(requireActivity().getApplication());
                            bookViewModel.updateBook(removedBook);
                        }
                    }
                });
                snackbar.show();
            }

            @Override
            public void onEditButtonClick(int position) {
                final Book book = recyclerViewAdapter.getBook(position);
                Intent intent = new Intent(view.getContext(), AddReviewActivity.class);
                intent.putExtra("book", book);
                view.getContext().startActivity(intent);
            }
        };
    }
}
