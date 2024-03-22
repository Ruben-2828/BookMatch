package com.example.bookmatch.ui.main.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookmatch.databinding.ActivityPreferencesEditBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;


public class AccountPreferencesActivity extends AppCompatActivity {

    private ActivityPreferencesEditBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPreferencesEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        undoEditPreferences();
        setupSaveButton();

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

        Snackbar.make(binding.getRoot(), "Preferences Saved!", Snackbar.LENGTH_SHORT).show();

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
            binding.menuGenre.setError("Please, make some changes to save preferences");
            binding.menuAuthor.setError("Please, make some changes to save preferences");
            binding.menuBook.setError("Please, make some changes to save preferences");
        }
        if (!fieldsNotEmpty) {
            if (TextUtils.isEmpty(binding.genre.getText())) {
                binding.menuGenre.setError("Genre cannot be empty");
            }
            if (TextUtils.isEmpty(binding.author.getText())) {
                binding.menuAuthor.setError("Author cannot be empty");
            }
            if (TextUtils.isEmpty(binding.book.getText())) {
                binding.menuBook.setError("Book cannot be empty");
            }
        }
    }

    private void clearErrors() {
        binding.menuGenre.setError(null);
        binding.menuAuthor.setError(null);
        binding.menuBook.setError(null);
    }



}
