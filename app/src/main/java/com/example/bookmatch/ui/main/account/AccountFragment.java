package com.example.bookmatch.ui.main.account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.bookmatch.R;
import com.example.bookmatch.databinding.FragmentAccountBinding;
import com.example.bookmatch.ui.main.BookViewModel;
import com.example.bookmatch.ui.main.BookViewModelFactory;
import com.example.bookmatch.ui.main.CollectionViewModel;
import com.example.bookmatch.ui.main.CollectionViewModelFactory;
import com.google.android.material.snackbar.Snackbar;

public class AccountFragment extends Fragment {

    private FragmentAccountBinding binding;
    private BookViewModel bookViewModel;
    private CollectionViewModel collectionViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViewModel();
        updateUserData();
        setButtonClickListeners();
    }

    private void initializeViewModel() {
        BookViewModelFactory factoryBook = new BookViewModelFactory(requireActivity().getApplication());
        bookViewModel = new ViewModelProvider(this, factoryBook).get(BookViewModel.class);
        CollectionViewModelFactory factoryCollection = new CollectionViewModelFactory(requireActivity().getApplication());
        collectionViewModel = new ViewModelProvider(this, factoryCollection).get(CollectionViewModel.class);
    }

    private void updateUserData() {
        bookViewModel.getSavedBooksCountLiveData().observe(getViewLifecycleOwner(), count -> {
            if (count != null) {
                binding.userSavedBooks.setText(String.valueOf(count));
            }
        });

        collectionViewModel.getCountCollectionLiveData().observe(getViewLifecycleOwner(), count -> {
            if (count != null) {
                binding.userCollectionCreated.setText(String.valueOf(count));
            }
        });

    }

    private void setButtonClickListeners() {
        binding.editAccountButton.setOnClickListener(this::showPopupMenu);
    }

    private void showPopupMenu(View v) {
        PopupMenu popup = new PopupMenu(getContext(), v);
        popup.getMenuInflater().inflate(R.menu.account_option_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(this::onMenuItemClicked);
        popup.show();
    }

    private boolean onMenuItemClicked(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.edit_profile_item) {
            launchEditProfileActivity();
            return true;
        }
        if (id == R.id.edit_preferences_item) {
            launchEditPreferencesActivity();
            return true;
        }
        if (id == R.id.about_us_item) {
            openAboutUsPage();
            return true;
        }
        if (id == R.id.see_intro){
            seeIntro();
            return true;
        }
        if (id == R.id.logout_item) {
            showLogoutSnackbar();
            return true;
        }
        return false;
    }

    private void launchEditProfileActivity() {
        Bundle args = new Bundle();
        args.putString("userNickname", binding.userNickname.getText().toString());
        args.putString("userFirstName", binding.userFirstName.getText().toString());
        args.putString("userLastName", binding.userLastName.getText().toString());
        args.putString("userEmail", binding.userEmail.getText().toString());
        args.putString("userPic", "https://i.pinimg.com/736x/c6/25/90/c62590c1756680060e7c38011cd704b5.jpg");

        Intent intent = new Intent(getContext(), AccountEditActivity.class);
        intent.putExtras(args);
        editProfileLauncher.launch(intent);
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

    private void launchEditPreferencesActivity() {
        Bundle args = new Bundle();
        args.putString("genre", binding.userFavoriteGenre.getText().toString());
        args.putString("author", binding.userFavoriteAuthor.getText().toString());
        args.putString("book", binding.userFavoriteBook.getText().toString());

        Intent intent = new Intent(getContext(), AccountPreferencesActivity.class);
        intent.putExtras(args);
        editPreferencesLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> editPreferencesLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        String genre = data.getStringExtra("genre");
                        String author = data.getStringExtra("author");
                        String book = data.getStringExtra("book");

                        binding.userFavoriteGenre.setText(genre);
                        binding.userFavoriteAuthor.setText(author);
                        binding.userFavoriteBook.setText(book);
                    }
                }
            }
    );

    private void openAboutUsPage() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Ruben-2828/BookMatch/tree/main"));
        startActivity(browserIntent);
    }

    private void showLogoutSnackbar() {
        Snackbar.make(requireView(), "Logout", Snackbar.LENGTH_LONG).show();
    }

    private void seeIntro(){
        resetOnboardPref();
        openIntro();
    }

    private void resetOnboardPref(){
        SharedPreferences pref = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpened", false);
        editor.apply();
    }

    private void openIntro(){
        Bundle args = new Bundle();
        args.putBoolean("isFromProfile", true);
        Navigation.findNavController(requireView()).navigate(R.id.action_navigation_account_to_onboardFragment, args);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
