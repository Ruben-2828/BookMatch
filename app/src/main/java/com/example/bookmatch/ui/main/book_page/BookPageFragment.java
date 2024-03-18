package com.example.bookmatch.ui.main.book_page;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.bookmatch.R;
import com.example.bookmatch.databinding.FragmentBookPageBinding;
import com.example.bookmatch.model.Book;

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

            binding.projectTitle.setText(book.getTitle());
            binding.authorTextView.setText(book.getAuthors().toString());
            binding.pubblicationYearTextView.setText(book.getPublicationYear());

            if(book.getFirstSentence() != null)
                binding.plotTextView.setText(book.getFirstSentence().get(0));

            String authors = "";
            for(String a: book.getAuthors())
                authors += a + ", ";
            authors = authors.substring(0, authors.length() - 2);
            binding.authorTextView.setText(authors);

            if (!book.getCoverID().isEmpty()) {
                Glide.with(this).load(book.getCoverID()).into(binding.coverBook);
            } else {
                Toast.makeText(getContext(), (R.string.book_cover_not_available_toast), Toast.LENGTH_SHORT).show();
            }
            binding.coverBook.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), FullScreenImageActivity.class);
                intent.putExtra("image uri", book.getCoverID());
                startActivity(intent);
            });

        } else {
            Toast.makeText(getContext(), (R.string.book_details_not_available_toast), Toast.LENGTH_SHORT).show();
        }

        binding.goBackButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigateUp();
        });


    }
}
