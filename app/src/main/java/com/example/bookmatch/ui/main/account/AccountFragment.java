package com.example.bookmatch.ui.main.account;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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

        AppUser user = new AppUser("id",
                "nicknamez",
                "Quackez",
                "Pakez",
                "quackez@pakez.ez",
                "https://i.pinimg.com/736x/c6/25/90/c62590c1756680060e7c38011cd704b5.jpg");

        // Set user data
        binding.userNickname.setText(user.getNickname());
        binding.userFirstName.setText(user.getFirstName());
        binding.userLastName.setText(user.getLastName());
        binding.userEmail.setText(user.getEmail());
        Glide.with(this).load(user.getPic()).into(binding.profileImage);

        // Set user statistics
        //TODO: set the right statistics
        binding.userSavedBooks.setText("10");
        binding.userBooksRead.setText("5");
        binding.userFavoriteGenre.setText("ezezez");

        binding.editAccountButton.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(getContext(), v);
            popup.getMenuInflater().inflate(R.menu.account_option_menu, popup.getMenu());

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    int id = item.getItemId();
                    if (id == R.id.edit_profile_item) {
                        Bundle args = new Bundle();
                        args.putParcelable("user", user);

                        NavController navC = Navigation.findNavController(view);
                        navC.navigate(R.id.action_navigation_account_to_accountEditFragment, args);
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
                }
            });

            popup.show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}