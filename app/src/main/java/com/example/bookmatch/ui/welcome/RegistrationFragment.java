package com.example.bookmatch.ui.welcome;

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

import com.example.bookmatch.R;
import com.example.bookmatch.data.repository.user.IUserRepository;
import com.example.bookmatch.databinding.FragmentRegistrationBinding;
import com.example.bookmatch.model.Result;
import com.example.bookmatch.ui.main.MainActivity;
import com.example.bookmatch.utils.ServiceLocator;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.Objects;

public class RegistrationFragment extends Fragment {

    private FragmentRegistrationBinding binding;
    private UserViewModel userViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository(requireActivity().getApplication());
        userViewModel = new ViewModelProvider(requireActivity(), new UserViewModelFactory(userRepository)).get(UserViewModel.class);
    }

    public RegistrationFragment() {
        // Required empty public constructor
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRegistrationBinding.inflate(inflater);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonRegister.setOnClickListener(v -> {
            clearErrors();

            String fullName = Objects.requireNonNull(binding.textInputLayoutName.
                    getEditText()).getText().toString();
            String username = Objects.requireNonNull(binding.textInputLayoutUsername.
                    getEditText()).getText().toString();
            String email = Objects.requireNonNull(binding.textInputLayoutEmail.
                    getEditText()).getText().toString();
            String password = binding.textInputLayoutPassword.
                    getEditText().getText().toString();

            if(!isEmpty() & isValidEmail(email) & isPasswordOk(password)){
                userViewModel.getUserMutableLiveData(email, password, username, fullName).observe(
                        getViewLifecycleOwner(), result -> {
                            if(result.isSuccess()) {
                                userViewModel.setAuthenticationError(false);
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                            }else{
                                String errorMessage = ((Result.Error) result).getMessage();
                                showSnackbar(errorMessage);
                                userViewModel.setAuthenticationError(true);
                            }
                        }
                );
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private boolean isValidEmail(String email) {
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

    private boolean isEmpty() {
        boolean isEmpty = false;

        if (Objects.requireNonNull(binding.textInputLayoutName.getEditText()).getText().toString().isEmpty()) {
            binding.textInputLayoutName.setError(getString(R.string.error_empty_fields));
            isEmpty = true;
        } else {
            binding.textInputLayoutName.setError(null);
        }

        if (Objects.requireNonNull(binding.textInputLayoutUsername.getEditText()).getText().toString().isEmpty()) {
            binding.textInputLayoutUsername.setError(getString(R.string.error_empty_fields));
            isEmpty = true;
        } else {
            binding.textInputLayoutUsername.setError(null);
        }

        if (Objects.requireNonNull(binding.textInputLayoutEmail.getEditText()).getText().toString().isEmpty()) {
            binding.textInputLayoutEmail.setError(getString(R.string.error_empty_fields));
            isEmpty = true;
        } else {
            binding.textInputLayoutEmail.setError(null);
        }

        if (Objects.requireNonNull(binding.textInputLayoutPassword.getEditText()).getText().toString().isEmpty()) {
            binding.textInputLayoutPassword.setError(getString(R.string.error_empty_fields));
            isEmpty = true;
        } else {
            binding.textInputLayoutPassword.setError(null);
        }

        if (Objects.requireNonNull(binding.textInputLayoutCheckPassword.getEditText()).getText().toString().isEmpty()) {
            binding.textInputLayoutCheckPassword.setError(getString(R.string.error_empty_fields));
            isEmpty = true;
        } else {
            binding.textInputLayoutCheckPassword.setError(null);
        }
        return isEmpty;
    }

    private boolean passwordEquals(){
        if (Objects.requireNonNull(binding.textInputLayoutPassword.getEditText())
                .getText().toString().equals(Objects.requireNonNull(
                        binding.textInputLayoutCheckPassword.getEditText()).getText().toString())) {
            return true;
        } else {
            binding.textInputLayoutPassword.setError(getString(R.string.error_match_password));
            binding.textInputLayoutCheckPassword.setError(getString(R.string.error_match_password));
            return false;
        }
    }

    private void clearErrors() {
        binding.textInputLayoutPassword.setError(null);
        binding.textInputLayoutCheckPassword.setError(null);
        binding.textInputLayoutEmail.setError(null);
        binding.textInputLayoutName.setError(null);
        binding.textInputLayoutUsername.setError(null);
    }

    private void showSnackbar(String message) {
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.nav_view);
        Snackbar snackbar = Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT);
        snackbar.setAnchorView(bottomNavigationView);
        snackbar.show();
    }
}
