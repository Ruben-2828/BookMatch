package com.example.bookmatch.ui.main.account;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.bookmatch.R;
import com.example.bookmatch.data.repository.user.IUserRepository;
import com.example.bookmatch.databinding.ActivityAccountEditBinding;
import com.example.bookmatch.ui.welcome.UserViewModel;
import com.example.bookmatch.ui.welcome.UserViewModelFactory;
import com.example.bookmatch.utils.ServiceLocator;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.Objects;
import java.util.UUID;

public class AccountEditActivity extends AppCompatActivity {

    private ActivityAccountEditBinding binding;
    private ActivityResultLauncher<String> galleryLauncher;

    private UserViewModel userViewModel;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccountEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository(getApplication());
        userViewModel = new ViewModelProvider(this, new UserViewModelFactory(userRepository)).get(UserViewModel.class);

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
            String userFullName = args.getString("userFullName");
            String userPic = args.getString("userPic");

            binding.tiNicknameInput.setText(userNickname);
            binding.tiFullNameInput.setText(userFullName);
            loadImageIntoProfile(userPic);
        }
    }

    private void loadImageIntoProfile(String imagePath) {
        Glide.with(this).load(imagePath).into(binding.profileImage);
    }

    private void saveChanges() {
        binding.saveButton.setOnClickListener(v -> {
            String nickname = Objects.requireNonNull(binding.tiNicknameInput.getText()).toString().trim();
            String fullName = Objects.requireNonNull(binding.tiFullNameInput.getText()).toString().trim();



            if (validateInput(nickname, fullName)) {
                setResultAndFinish(nickname, fullName);
            }
        });
    }

    private boolean validateInput(String nickname, String fullName) {
        boolean isValid = true;
        if (nickname.isEmpty()) {
            binding.tilNickname.setError(getString(R.string.error_nickname_required));
            isValid = false;
        } else {
            binding.tilNickname.setError(null);
        }
        if (fullName.isEmpty()) {
            binding.tilFullName.setError(getString(R.string.error_full_name_required));
            isValid = false;
        } else {
            binding.tilFullName.setError(null);
        }

        return isValid;
    }

    private void setResultAndFinish(String nickname, String fullName) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("userNickname", nickname);
        resultIntent.putExtra("userFullName", fullName);

        //
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference ref = storage.getReference().child("images/" + UUID.randomUUID().toString());
        Bitmap bitmap = ((BitmapDrawable) binding.profileImage.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageInByte = baos.toByteArray();
        ref.putBytes(imageInByte);
        Log.d("WELCOME", "inserito");

        userViewModel.setUserInfo(nickname, fullName);
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
