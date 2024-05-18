package com.example.bookmatch.ui.welcome;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.bookmatch.R;
import com.example.bookmatch.data.repository.user.IUserRepository;
import com.example.bookmatch.databinding.FragmentLoginBinding;
import com.example.bookmatch.ui.main.MainActivity;
import com.example.bookmatch.utils.AccountManager;
import com.example.bookmatch.utils.ServiceLocator;
import com.google.firebase.FirebaseApp;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.Objects;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private UserViewModel userViewModel;
    private AccountManager accountManager;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository(requireActivity().getApplication());
        userViewModel = new ViewModelProvider(requireActivity(), new UserViewModelFactory(userRepository)).get(UserViewModel.class);
        accountManager = new AccountManager();

        FirebaseApp.initializeApp(getActivity().getApplication());
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater);

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        boolean isUserLogged = accountManager.getRememberMe(getContext());

        if(isUserLogged){
            AccountManager.UserCredentials credentials = accountManager.getCredentials(getContext());
            Log.d("WELCOME", credentials.getEmail());

            userViewModel.getUserMutableLiveData(credentials.getEmail(), credentials.getPassword()).observe(
                    getViewLifecycleOwner(), result -> {
                        if(result.getTokenId() != null) {
                            Log.d("WELCOME", result.getTokenId());
                            userViewModel.setAuthenticationError(false);
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                        }else{
                            Log.d("WELCOME", "login failed");
                            userViewModel.setAuthenticationError(true);
                        }
                    }
            );
        }

        binding.buttonLogin.setOnClickListener(v -> {

            accountManager.setRememberMe(binding.checkboxRememberMe.isChecked(), getContext());

            String email = Objects.requireNonNull(binding.textInputLayoutEmail.
                    getEditText()).getText().toString();
            String password = Objects.requireNonNull(binding.textInputLayoutPassword.
                    getEditText()).getText().toString();

            if(isEmailOk(email) && isPasswordOk(password)){
                userViewModel.getUserMutableLiveData(email, password).observe(
                        getViewLifecycleOwner(), result -> {
                            if(result.getTokenId() != null) {
                                Log.d("WELCOME", result.getTokenId());
                                userViewModel.setAuthenticationError(false);
                                saveUserInfo(email, password);

                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                            }else{
                                Log.d("WELCOME", "login failed");
                                userViewModel.setAuthenticationError(true);
                            }
                        }
                );
            }else {

            }
            Log.d("WELCOME", email + password);
        });

        binding.buttonRegistration.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registrationFragment);
        });

    }

    private void saveUserInfo(String email, String password){
        if(accountManager.getRememberMe(getContext())){
            accountManager.saveUserInfo(email, password, getContext());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private boolean isEmailOk(String email) {
        if (!EmailValidator.getInstance().isValid((email))) {
            binding.textInputLayoutEmail.setError(getString(R.string.error_email));
            return false;
        } else {
            binding.textInputLayoutEmail.setError(null);
            return true;
        }
    }

    private boolean isPasswordOk(String password) {
        if (password.length() < 8) {
            binding.textInputLayoutPassword.setError(getString(R.string.error_password));
            return false;
        } else {
            binding.textInputLayoutPassword.setError(null);
            return true;
        }
    }

    private void showExitConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(getString(R.string.quit_title));
        builder.setMessage(getString(R.string.quit));
        builder.setPositiveButton(getString(R.string.yes), (dialog, which) -> requireActivity().finish());
        builder.setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
