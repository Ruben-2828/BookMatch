package com.example.bookmatch.ui.main.reviews;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.bookmatch.R;
import com.example.bookmatch.databinding.ActivityAddReviewBinding;
import com.example.bookmatch.model.Book;
import com.example.bookmatch.ui.main.BookViewModel;
import com.example.bookmatch.ui.main.BookViewModelFactory;
import com.example.bookmatch.ui.main.book_page.FullscreenImageActivity;

import java.util.Objects;

public class AddReviewActivity extends AppCompatActivity {

    private static final String TAG = AddReviewActivity.class.getSimpleName();
    private ActivityAddReviewBinding binding;
    private Book book;

    private BookViewModel bookViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddReviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getViewModel();
        retrieveInfo();
        setupSaveButton();
    }

    private void getViewModel(){
        BookViewModelFactory factory = new BookViewModelFactory(this.getApplication());
         bookViewModel = new ViewModelProvider(this, factory).get(BookViewModel.class);
    }

    private void retrieveInfo() {
        book = getIntent().getParcelableExtra("book");
        Log.d(TAG, "Book: " + book);
        if (book != null) {
            binding.bookTitleAppbar.setText(book.getTitle());
            binding.bookTitle.setText(book.getTitle());

            String authors = String.join(", ", book.getAuthors());
            binding.authorTextView.setText(authors);

            binding.pubblicationYearTextView.setText(book.getPublicationYear());

            if (!book.getCoverURI().isEmpty()) {
                Glide.with(this)
                        .load(book.getCoverURI())
                        .into(binding.coverBook);

                binding.coverBook.setOnClickListener(v -> {
                    Intent intent = new Intent(this, FullscreenImageActivity.class);
                    intent.putExtra("image uri", book.getCoverURI());
                    intent.putExtra("book title", book.getTitle());
                    intent.putExtra("book status", book.isSaved());
                    startActivity(intent);
                });
            } else {
                Toast.makeText(this, R.string.book_cover_not_available_toast, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.book_details_not_available_toast, Toast.LENGTH_SHORT).show();
        }

        binding.goBackButton.setOnClickListener(v -> finish());
    }

    private void setupSaveButton() {
        binding.addReviewButton.setOnClickListener(v -> {
            String reviewText = Objects.requireNonNull(binding.tiReviewInput.getText()).toString();
            float rating = binding.ratingBar.getRating();

            book.setReview(reviewText);
            book.setRating(rating);


            bookViewModel.updateBook(book);

            Toast.makeText(this, "Review Saved!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
