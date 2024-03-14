package com.example.bookmatch.ui.main.account;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.bookmatch.R;
import com.example.bookmatch.databinding.FragmentAccountBinding;
import com.example.bookmatch.databinding.FragmentAccountEditBinding;
import com.example.bookmatch.model.AppUser;
import com.google.android.material.snackbar.Snackbar;

public class AccountEditFragment extends Fragment {

    private FragmentAccountEditBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAccountEditBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        assert args != null;
        AppUser user = args.getParcelable("user");

        assert user != null;
        binding.tiNicknameInput.setText(user.getNickname());
        binding.tiFirstNameInput.setText(user.getFirstName());
        binding.tiLastNameInput.setText(user.getLastName());
        binding.tiEmailInput.setText(user.getEmail());
        Glide.with(this).load(user.getPic()).into(binding.profileImage);

        binding.goBackButton.setOnClickListener(v -> {
            NavController navC = Navigation.findNavController(view);
            navC.popBackStack();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}