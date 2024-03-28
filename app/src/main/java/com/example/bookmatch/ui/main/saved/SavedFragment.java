package com.example.bookmatch.ui.main.saved;

import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

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
import com.example.bookmatch.ui.main.BookViewModel;
import com.example.bookmatch.ui.main.BookViewModelFactory;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;


public class SavedFragment extends Fragment {

    private FragmentSavedBinding binding;
    private SavedRecyclerViewAdapter recyclerViewAdapter;
    private BookViewModel bookViewModel;

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
        bookViewModel = new ViewModelProvider(this, factory).get(BookViewModel.class);

        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.nav_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        recyclerViewAdapter = new SavedRecyclerViewAdapter(createOnItemClickListener(view, bottomNavigationView));

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

    private SavedRecyclerViewAdapter.OnItemClickListener createOnItemClickListener(View view, BottomNavigationView bottomNavigationView) {

        return new SavedRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Book book) {
                Bundle args = new Bundle();
                args.putParcelable("book", book);

                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_navigation_saved_to_navigation_book, args);
            }

            @Override
            public void onDeleteButtonClick(int position) {
                final Book removedBook = recyclerViewAdapter.removeBook(position);
                recyclerViewAdapter.notifyItemRemoved(position);
                Snackbar snackbar = Snackbar.make(view, removedBook.getTitle() + " removed from saved!", Snackbar.LENGTH_SHORT).setAnchorView(bottomNavigationView);
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
                            bookViewModel.deleteBook(removedBook);
                        }
                    }
                });
                snackbar.show();
            }

            @Override
            public void onReviewButtonClick(int position) {
                final Book book = recyclerViewAdapter.getBook(position);

                if (book.isReviewed()) {
                    book.setReviewed(false);
                    Snackbar.make(view, book.getTitle() + " removed from reviewed!", Snackbar.LENGTH_SHORT)
                            .setAnchorView(bottomNavigationView)
                            .show();
                } else {
                    book.setReviewed(true);
                    Snackbar.make(view, book.getTitle() + " added to reviewed!", Snackbar.LENGTH_SHORT)
                            .setAnchorView(bottomNavigationView)
                            .show();
                }

                recyclerViewAdapter.notifyItemChanged(position);
                bookViewModel.updateBook(book);
            }
        };
    }
}


