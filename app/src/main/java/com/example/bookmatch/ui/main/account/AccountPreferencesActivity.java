package com.example.bookmatch.ui.main.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.bookmatch.R;
import com.example.bookmatch.databinding.ActivityPreferencesEditBinding;
import com.example.bookmatch.model.Book;
import com.example.bookmatch.ui.main.BookViewModel;
import com.example.bookmatch.ui.main.BookViewModelFactory;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class AccountPreferencesActivity extends AppCompatActivity {

    private ActivityPreferencesEditBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPreferencesEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getDataFromSavedFragment();
        undoEditPreferences();
        setupSaveButton();

    }

    private void getDataFromSavedFragment() {

        BookViewModelFactory factory = new BookViewModelFactory(getApplication());
        BookViewModel bookViewModel = new ViewModelProvider(this, factory).get(BookViewModel.class);

        bookViewModel.getSavedBooksLiveData().observe(this, savedBooks -> {
            if (savedBooks != null && savedBooks.size() > 0) {
                List<String> titles = new ArrayList<>();
                List<String> authors = new ArrayList<>();

                for (Book savedBook : savedBooks) {
                    titles.add(savedBook.getTitle());
                    authors.addAll(savedBook.getAuthors());
                }

                ArrayAdapter<String> titleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, titles);
                ArrayAdapter<String> authorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, authors);

                binding.book.setAdapter(titleAdapter);
                binding.author.setAdapter(authorAdapter);
            } else {
                binding.menuAuthor.setError(getString(R.string.please_add_some_books_to_your_saved_list));
                binding.menuBook.setError(getString(R.string.please_add_some_books_to_your_saved_list));
            }
        });
    }


    private void undoEditPreferences() {
        binding.goBackButton.setOnClickListener(v -> finish());
    }

    private void setupSaveButton() {
        binding.saveButton.setOnClickListener(v -> savePreferences());
    }

    private void savePreferences() {
        boolean changesDetected = checkForChanges();
        boolean fieldsNotEmpty = checkFieldsNotEmpty();

        if (checkForChanges() && checkFieldsNotEmpty()) {
            performSave();
        } else {
            setErrorAndShowMessage(changesDetected, fieldsNotEmpty);
        }
    }

    private boolean checkFieldsNotEmpty() {
        return !TextUtils.isEmpty(binding.genre.getText()) &&
                !TextUtils.isEmpty(binding.author.getText()) &&
                !TextUtils.isEmpty(binding.book.getText());
    }

    private boolean checkForChanges() {
        String currentGenre = binding.genre.getText().toString();
        String currentAuthor = binding.author.getText().toString();
        String currentBook = binding.book.getText().toString();

        Bundle extras = getIntent().getExtras();
        String passedGenre = extras != null ? extras.getString("genre") : null;
        String passedAuthor = extras != null ? extras.getString("author") : null;
        String passedBook = extras != null ? extras.getString("book") : null;

        return !Objects.equals(currentGenre, passedGenre) ||
                !Objects.equals(currentAuthor, passedAuthor) ||
                !Objects.equals(currentBook, passedBook);
    }

    private void performSave() {
        String genre = binding.genre.getText().toString();
        String author = binding.author.getText().toString();
        String book = binding.book.getText().toString();

        Snackbar.make(binding.getRoot(), getString(R.string.preferences_saved), Snackbar.LENGTH_SHORT).show();

        Intent resultIntent = new Intent();
        resultIntent.putExtra("genre", genre);
        resultIntent.putExtra("author", author);
        resultIntent.putExtra("book", book);

        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    private void setErrorAndShowMessage(boolean changesDetected, boolean fieldsNotEmpty) {
        clearErrors();
        if (!changesDetected) {
            binding.menuGenre.setError(getString(R.string.please_make_some_changes_to_save_preferences));
            binding.menuAuthor.setError(getString(R.string.please_make_some_changes_to_save_preferences));
            binding.menuBook.setError(getString(R.string.please_make_some_changes_to_save_preferences));
        }
        if (!fieldsNotEmpty) {
            if (TextUtils.isEmpty(binding.genre.getText())) {
                binding.menuGenre.setError(getString(R.string.genre_cannot_be_empty));
            }
            if (TextUtils.isEmpty(binding.author.getText())) {
                binding.menuAuthor.setError(getString(R.string.author_cannot_be_empty));
            }
            if (TextUtils.isEmpty(binding.book.getText())) {
                binding.menuBook.setError(getString(R.string.book_cannot_be_empty));
            }
        }
    }

    private void clearErrors() {
        binding.menuGenre.setError(null);
        binding.menuAuthor.setError(null);
        binding.menuBook.setError(null);
    }



}
