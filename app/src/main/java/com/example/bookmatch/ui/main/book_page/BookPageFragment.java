package com.example.bookmatch.ui.main.book_page;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.bookmatch.R;

import com.example.bookmatch.databinding.FragmentBookPageBinding;
import com.example.bookmatch.model.Book;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;


public class BookPageFragment extends Fragment {

    private FragmentBookPageBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBookPageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            Book book = arguments.getParcelable("book");

            binding.bookTitleAppbar.setText(Objects.requireNonNull(book).getTitle());
            binding.bookTitle.setText(book.getTitle());
            binding.authorTextView.setText(book.getAuthors().toString());
            binding.pubblicationYearTextView.setText(book.getPublicationYear());

            if(book.getDescription() != null)
                binding.plotTextView.setText(book.getDescription());

            String authors = "";
            for(String a: book.getAuthors())
                authors += a + ", ";
            authors = authors.substring(0, authors.length() - 2);
            binding.authorTextView.setText(authors);

            if (!book.getCoverURI().isEmpty()) {
                Glide.with(this)
                        .load(book.getCoverURI())
                        .into(binding.coverBook);

                binding.coverBook.setOnClickListener(v -> {
                    Intent intent = new Intent(getContext(), FullscreenImageActivity.class);
                    intent.putExtra("image uri", book.getCoverURI());
                    intent.putExtra("book title", book.getTitle());
                    intent.putExtra("book status", book.isSaved());
                    startActivity(intent);
                });
            } else {
                Snackbar.make(view, getString(R.string.book_cover_not_available), Snackbar.LENGTH_SHORT).show();
            }

            if(book.isSaved()){
                binding.savedButton.setVisibility(View.VISIBLE);
                binding.notSavedButton.setVisibility(View.INVISIBLE);
            } else {
                binding.savedButton.setVisibility(View.INVISIBLE);
                binding.notSavedButton.setVisibility(View.VISIBLE);
            }

        } else {
            Snackbar.make(view, getString(R.string.book_details_not_available), Snackbar.LENGTH_SHORT).show();
        }

        binding.goBackButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigateUp();
        });

    }
}
