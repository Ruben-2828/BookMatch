package com.example.bookmatch.ui.welcome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bookmatch.R;
import com.example.bookmatch.databinding.FragmentRegistrationBinding;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.Objects;

public class RegistrationFragment extends Fragment {

    private FragmentRegistrationBinding binding;

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
            String username = Objects.requireNonNull(binding.textInputLayoutPassword.
                    getEditText()).getText().toString();
            String email = Objects.requireNonNull(binding.textInputLayoutEmail.
                    getEditText()).getText().toString();
            String password = binding.textInputLayoutPassword.
                    getEditText().getText().toString();
            String repeatPassword = Objects.requireNonNull(binding.textInputLayoutCheckPassword.
                    getEditText()).getText().toString();

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
}
