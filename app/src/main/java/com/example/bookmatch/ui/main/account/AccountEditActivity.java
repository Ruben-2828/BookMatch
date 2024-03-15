package com.example.bookmatch.ui.main.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.bookmatch.R;
import com.example.bookmatch.databinding.ActivityAccountEditBinding;

import java.util.Objects;

public class AccountEditActivity extends AppCompatActivity {

    private ActivityAccountEditBinding binding;
    private ActivityResultLauncher<String> galleryLauncher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccountEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        galleryLauncher();
        retrieveInfos();
        saveChanges();
        undoEditProfile();
        changePic();
    }

    private void galleryLauncher() {
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                result -> {
                    if (result != null) {
                        String selectedImagePath = result.toString();
                        loadImageIntoProfile(selectedImagePath);
                    }
                });
    }

    private void retrieveInfos() {
        Bundle args = getIntent().getExtras();
        if (args != null) {
            String userNickname = args.getString("userNickname");
            String userFirstName = args.getString("userFirstName");
            String userLastName = args.getString("userLastName");
            String userPic = args.getString("userPic");

            binding.tiNicknameInput.setText(userNickname);
            binding.tiFirstNameInput.setText(userFirstName);
            binding.tiLastNameInput.setText(userLastName);
            loadImageIntoProfile(userPic);
        }
    }

    private void loadImageIntoProfile(String imagePath) {
        Glide.with(this).load(imagePath).into(binding.profileImage);
    }

    private void saveChanges() {
        binding.saveButton.setOnClickListener(v -> {
            String nickname = Objects.requireNonNull(binding.tiNicknameInput.getText()).toString().trim();
            String firstName = Objects.requireNonNull(binding.tiFirstNameInput.getText()).toString().trim();
            String lastName = Objects.requireNonNull(binding.tiLastNameInput.getText()).toString().trim();

            if (validateInput(nickname, firstName, lastName)) {
                setResultAndFinish(nickname, firstName, lastName);
            }
        });
    }

    private boolean validateInput(String nickname, String firstName, String lastName) {
        boolean isValid = true;
        if (nickname.isEmpty()) {
            binding.tilNickname.setError(getString(R.string.error_nickname_required));
            isValid = false;
        } else {
            binding.tilNickname.setError(null);
        }
        if (firstName.isEmpty()) {
            binding.tilFirstName.setError(getString(R.string.error_first_name_required));
            isValid = false;
        } else {
            binding.tilFirstName.setError(null);
        }
        if (lastName.isEmpty()) {
            binding.tilLastName.setError(getString(R.string.error_last_name_required));
            isValid = false;
        } else {
            binding.tilLastName.setError(null);
        }
        return isValid;
    }

    private void setResultAndFinish(String nickname, String firstName, String lastName) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("userNickname", nickname);
        resultIntent.putExtra("userFirstName", firstName);
        resultIntent.putExtra("userLastName", lastName);

        //TODO: implementare salvataggio immagine profilo

        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    private void undoEditProfile() {
        binding.goBackButton.setOnClickListener(v -> finish());
    }

    private void changePic() {
        binding.editPicButton.setOnClickListener(v -> galleryLauncher.launch("image/*"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
