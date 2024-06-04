package com.example.bookmatch.ui.main.account;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.bookmatch.R;
import com.example.bookmatch.data.repository.user.IUserRepository;
import com.example.bookmatch.databinding.ActivityAccountEditBinding;
import com.example.bookmatch.model.Result;
import com.example.bookmatch.model.User;
import com.example.bookmatch.ui.welcome.UserViewModel;
import com.example.bookmatch.ui.welcome.UserViewModelFactory;
import com.example.bookmatch.utils.ServiceLocator;
import java.io.ByteArrayOutputStream;
import java.util.Objects;

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

            if(userPic != null){
                loadImageIntoProfile(userPic);
            }
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

        binding.profileImage.setDrawingCacheEnabled(true);
        binding.profileImage.buildDrawingCache();

        if(binding.profileImage.getDrawable() instanceof VectorDrawable){
            User user = new User(nickname,null, null, fullName, null);
            userViewModel.setUserInfo(user).observe(this,
                    res -> {
                        if(res.isSuccess()){
                            resultIntent.putExtra("userNickname", nickname);
                            resultIntent.putExtra("userFullName", fullName);
                            setResult(Activity.RESULT_OK, resultIntent);
                            finish();
                        }else {
                            setResult(Activity.RESULT_CANCELED, resultIntent);
                            finish();
                        }
                    });
        }else{
            Bitmap bitmap = ((BitmapDrawable) binding.profileImage.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            userViewModel.uploadImage(baos.toByteArray()).observe(this, result -> {
                if(result.isSuccess()){
                    String url = ((Result.StorageResponseSuccess)result).getUrl();
                    User user = new User(nickname,null, null, fullName, url);
                    userViewModel.setUserInfo(user).observe(this,
                            res -> {
                                if(res.isSuccess()){
                                    resultIntent.putExtra("userNickname", nickname);
                                    resultIntent.putExtra("userFullName", fullName);
                                    resultIntent.putExtra("profileImage", url);
                                    setResult(Activity.RESULT_OK, resultIntent);
                                    finish();
                                }else {
                                    setResult(Activity.RESULT_CANCELED, resultIntent);
                                    finish();
                                }
                            });
                }else{
                    String message = ((Result.Error)result).getMessage();
                    resultIntent.putExtra("error", message);
                    setResult(Activity.RESULT_CANCELED, resultIntent);
                    finish();
                }
            });
        }
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
