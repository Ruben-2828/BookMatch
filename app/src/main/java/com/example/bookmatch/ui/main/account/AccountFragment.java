package com.example.bookmatch.ui.main.account;

import android.accounts.Account;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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

import com.bumptech.glide.Glide;
import com.example.bookmatch.R;
import com.example.bookmatch.data.repository.user.IUserRepository;
import com.example.bookmatch.databinding.FragmentAccountBinding;
import com.example.bookmatch.model.Result;
import com.example.bookmatch.model.User;
import com.example.bookmatch.model.UserPreferences;
import com.example.bookmatch.ui.main.BookViewModel;
import com.example.bookmatch.ui.main.BookViewModelFactory;
import com.example.bookmatch.ui.main.CollectionContainerViewModel;
import com.example.bookmatch.ui.main.CollectionContainerViewModelFactory;
import com.example.bookmatch.ui.main.reviews.AddReviewActivity;
import com.example.bookmatch.ui.welcome.UserViewModel;
import com.example.bookmatch.ui.welcome.UserViewModelFactory;
import com.example.bookmatch.ui.welcome.WelcomeActivity;
import com.example.bookmatch.utils.AccountManager;
import com.example.bookmatch.utils.ServiceLocator;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class AccountFragment extends Fragment {

    private FragmentAccountBinding binding;
    private BookViewModel bookViewModel;
    private CollectionContainerViewModel collectionViewModel;
    private UserViewModel userViewModel;
    private String profileImage;
    private static final String TAG = AccountFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository(requireActivity().getApplication());
        userViewModel = new ViewModelProvider(requireActivity(), new UserViewModelFactory(userRepository)).get(UserViewModel.class);
    }

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
        updateUserPreferences();
        setButtonClickListeners();
    }

    private void initializeViewModel() {
        BookViewModelFactory factoryBook = new BookViewModelFactory(requireActivity().getApplication());
        bookViewModel = new ViewModelProvider(this, factoryBook).get(BookViewModel.class);
        CollectionContainerViewModelFactory factoryCollection = new CollectionContainerViewModelFactory(requireActivity().getApplication());
        collectionViewModel = new ViewModelProvider(this, factoryCollection).get(CollectionContainerViewModel.class);
    }

    private void updateUserData() {
        userViewModel.getUserInfo(userViewModel.getLoggedUser().getTokenId()).observe(getViewLifecycleOwner(), result -> {
            if(result.isSuccess()){
                if(!(result instanceof Result.UserResponseSuccess))
                    return;

                User user = ((Result.UserResponseSuccess)result).getData();
                if(!user.getUsername().equals("null")){
                    binding.userNickname.setText(user.getUsername());
                }
                if(!user.getEmail().equals("null")) {
                    binding.userEmail.setText(user.getEmail());
                }
                if(!user.getFullName().equals("null")) {
                    binding.userFullName.setText(user.getFullName());
                }
                if(!user.getProfileImage().equals("null")) {
                    profileImage = user.getProfileImage();
                    Glide.with(this).load(user.getProfileImage()).into(binding.profileImage);
                }
            }else{
                String error = ((Result.Error)result).getMessage();
                Log.d(TAG, error);
            }

        });

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

        bookViewModel.getReviewedBooksCountLiveData().observe(getViewLifecycleOwner(), count -> {
            if (count != null) {
                binding.userReviewedBooks.setText(String.valueOf(count));
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
        if (id == R.id.logout_item) {
            userViewModel.logout().observe(getViewLifecycleOwner(), r -> {
                if(r.isSuccess() && r instanceof Result.LogoutResponseSuccess){
                    AccountManager accountManager = new AccountManager(getContext());
                    accountManager.setRememberMe(false);
                    accountManager.setIsGoogleAccount(false);
                    Intent intent = new Intent(requireContext(), WelcomeActivity.class);
                    startActivity(intent);
                    requireActivity().finish();
                }
            });
            return true;
        }
        return false;
    }

    private void launchEditProfileActivity() {
        Bundle args = new Bundle();
        String userNickname = binding.userNickname.getText().toString().equals(getString(R.string.default_nickname))
                ? "" : binding.userNickname.getText().toString();

        String userFullname = binding.userFullName.getText().toString().equals(getString(R.string.default_full_name))
                ? "" : binding.userFullName.getText().toString();

        args.putString("userNickname", userNickname);
        args.putString("userFullName", userFullname);
        args.putString("userEmail", binding.userEmail.getText().toString());
        args.putString("userPic", profileImage);

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
                        String userFullName = data.getStringExtra("userFullName");
                        String imageProfile = data.getStringExtra("profileImage");

                        binding.userNickname.setText(userNickname);
                        binding.userFullName.setText(userFullName);
                        Glide.with(this).load(imageProfile).into(binding.profileImage);
                    }
                } else {
                    showSnackBar(getString(R.string.error_not_saved));
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

                        UserPreferences userPreferences = new UserPreferences(genre, author, book);

                        setUserPreferences(userPreferences);

                        showSnackBar(getString(R.string.preferences_saved));
                    }
                }else{
                    showSnackBar(getString(R.string.preferences_not_saved));
                }
            }
    );

    private void openAboutUsPage() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://github.com/Ruben-2828/BookMatch/tree/main"));
        startActivity(browserIntent);
    }

    private void updateUserPreferences() {
        userViewModel.getPreferences().observe(
                getViewLifecycleOwner(),
                result -> {
                    if(result.isSuccess()){
                        UserPreferences preferences = ((Result.PreferencesResponseSuccess) result).getUserPreference();
                        setUserPreferences(preferences);
                    }
                }
        );
    }

    private void setUserPreferences(UserPreferences preferences){
        binding.userFavoriteGenre.setText(preferences.getGenre());
        binding.userFavoriteAuthor.setText(preferences.getAuthor());
        binding.userFavoriteBook.setText(preferences.getBook());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showSnackBar(String message){
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.nav_view);
        Snackbar snackbar = Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT);
        snackbar.setAnchorView(bottomNavigationView);
        snackbar.show();
    }
}
