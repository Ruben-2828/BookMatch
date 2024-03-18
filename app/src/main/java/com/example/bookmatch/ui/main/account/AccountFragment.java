package com.example.bookmatch.ui.main.account;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.bookmatch.R;
import com.example.bookmatch.databinding.FragmentAccountBinding;
import com.example.bookmatch.model.AppUser;
import com.google.android.material.snackbar.Snackbar;

public class AccountFragment extends Fragment {

    private FragmentAccountBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateUserData();

        PopupMenu.OnMenuItemClickListener menuItemListener = item -> {
            int id = item.getItemId();
            if (id == R.id.edit_profile_item) {
                Bundle args = new Bundle();
                args.putString("userNickname", binding.userNickname.getText().toString());
                args.putString("userFirstName", binding.userFirstName.getText().toString());
                args.putString("userLastName", binding.userLastName.getText().toString());
                args.putString("userEmail", binding.userEmail.getText().toString());
                args.putString("userPic", "https://i.pinimg.com/736x/c6/25/90/c62590c1756680060e7c38011cd704b5.jpg");

                Intent intent = new Intent(getContext(), AccountEditActivity.class);
                intent.putExtras(args);
                editProfileLauncher.launch(intent);
                return true;
            }
            if (id == R.id.about_us_item) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Ruben-2828/BookMatch/tree/main"));
                startActivity(browserIntent);
                return true;
            }
            if (id == R.id.logout_item) {
                //TODO: implement logout
                Snackbar.make(view, "Logout", Snackbar.LENGTH_LONG).show();
                return true;
            }
            return false;
        };

        binding.editAccountButton.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(getContext(), v);
            popup.getMenuInflater().inflate(R.menu.account_option_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(menuItemListener);
            popup.show();
        });
    }

    private final ActivityResultLauncher<Intent> editProfileLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        String userNickname = data.getStringExtra("userNickname");
                        String userFirstName = data.getStringExtra("userFirstName");
                        String userLastName = data.getStringExtra("userLastName");

                        binding.userNickname.setText(userNickname);
                        binding.userFirstName.setText(userFirstName);
                        binding.userLastName.setText(userLastName);
                    }
                }
            }
    );

    private void updateUserData() {
        if (getArguments() != null) {
            String userNickname = getArguments().getString("userNickname");
            String userFirstName = getArguments().getString("userFirstName");
            String userLastName = getArguments().getString("userLastName");

            binding.userNickname.setText(userNickname);
            binding.userFirstName.setText(userFirstName);
            binding.userLastName.setText(userLastName);

            getArguments().clear();
        } else {
            AppUser user = new AppUser("id",
                    "nicknamez",
                    "Quackez",
                    "Pakez",
                    "quackez@pakez.ez",
                    "https://i.pinimg.com/736x/c6/25/90/c62590c1756680060e7c38011cd704b5.jpg");

            binding.userNickname.setText(user.getNickname());
            binding.userFirstName.setText(user.getFirstName());
            binding.userLastName.setText(user.getLastName());
            binding.userEmail.setText(user.getEmail());
            Glide.with(this).load(user.getCover()).into(binding.profileImage);
        }

        binding.userSavedBooks.setText("10");
        binding.userBooksRead.setText("5");
        binding.userFavoriteGenre.setText("ezezez");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
