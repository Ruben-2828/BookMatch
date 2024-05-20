package com.example.bookmatch.ui.main.account;

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

import com.example.bookmatch.R;
import com.example.bookmatch.data.repository.user.IUserRepository;
import com.example.bookmatch.databinding.FragmentAccountBinding;
import com.example.bookmatch.model.Result;
import com.example.bookmatch.model.UserPreferences;
import com.example.bookmatch.ui.main.BookViewModel;
import com.example.bookmatch.ui.main.BookViewModelFactory;
import com.example.bookmatch.ui.main.CollectionContainerViewModel;
import com.example.bookmatch.ui.main.CollectionContainerViewModelFactory;
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
        userViewModel.getUserInfo(userViewModel.getLoggedUser().getTokenId()).observe(getViewLifecycleOwner(), user -> {
            if(user.getUsername() != null){
                Log.d("WELCOME", user.getUsername());
                binding.userNickname.setText(user.getUsername());
            }
            if(user.getEmail() != null) {
                binding.userEmail.setText(user.getEmail());
            }
            if(user.getFullName() != null) {
                binding.userFullName.setText(user.getFullName());
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
                AccountManager accountManager = new AccountManager(getContext());
                accountManager.setRememberMe(false);
                accountManager.setIsGoogleAccount(false);
                Intent intent = new Intent(requireContext(), WelcomeActivity.class);
                startActivity(intent);
                requireActivity().finish();
            });
            return true;
        }
        return false;
    }

    private void launchEditProfileActivity() {
        Bundle args = new Bundle();
        args.putString("userNickname", binding.userNickname.getText().toString());
        args.putString("userFullName", binding.userFullName.getText().toString());
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
                        String userFullName = data.getStringExtra("userFullName");

                        binding.userNickname.setText(userNickname);
                        binding.userFullName.setText(userFullName);
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

                        UserPreferences userPreferences = new UserPreferences(genre, author, book);

                        setUserPreferences(userPreferences);

                        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.nav_view);
                        Snackbar snackbar = Snackbar.make(binding.getRoot(), getString(R.string.preferences_saved), Snackbar.LENGTH_SHORT);
                        snackbar.setAnchorView(bottomNavigationView);
                        snackbar.show();
                    }
                }else{
                    BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.nav_view);
                    Snackbar snackbar = Snackbar.make(binding.getRoot(), getString(R.string.preferences_not_saved), Snackbar.LENGTH_SHORT);
                    snackbar.setAnchorView(bottomNavigationView);
                    snackbar.show();
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
                    }else{
                        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.nav_view);
                        Snackbar snackbar = Snackbar.make(binding.getRoot(), getString(R.string.preferences_fetch_error), Snackbar.LENGTH_SHORT);
                        snackbar.setAnchorView(bottomNavigationView);
                        snackbar.show();
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
}
