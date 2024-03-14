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

        String coverUrl;

        Bundle arguments = getArguments();
        if (arguments != null) {
            String title = arguments.getString("title", getString(R.string.book_title_default));
            String author = arguments.getString("author", getString(R.string.default_book_author));
            String plot = arguments.getString("plot", getString(R.string.default_book_plot));
            String genre = arguments.getString("genre", getString(R.string.genre_book_default));
            String year = arguments.getString("year", getString(R.string.pubblication_year_default));
            coverUrl = arguments.getString("cover", "");

            binding.projectTitle.setText(title);
            binding.authorTextView.setText(author);
            binding.plotTextView.setText(plot);
            binding.genreTextView.setText(genre);
            binding.pubblicationYearTextView.setText(year);

            if (!coverUrl.isEmpty()) {
                Glide.with(this).load(coverUrl).into(binding.coverBook);
            } else {
                Toast.makeText(getContext(), "Book cover not available", Toast.LENGTH_SHORT).show();
            }
        } else {
            coverUrl = "";
            Toast.makeText(getContext(), "Book details not available", Toast.LENGTH_SHORT).show();
        }

        binding.goBackButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigateUp();
        });

        binding.coverBook.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), FullScreenImageActivity.class);
            intent.putExtra("image uri", coverUrl);
            startActivity(intent);
        });
    }
}
