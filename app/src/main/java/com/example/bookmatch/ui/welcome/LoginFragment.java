package com.example.bookmatch.ui.welcome;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.Objects;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private UserViewModel userViewModel;
    private AccountManager accountManager;
    private ActivityResultLauncher<IntentSenderRequest> activityResultLauncher;
    private ActivityResultContracts.StartIntentSenderForResult startIntentSenderForResult;
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository(requireActivity().getApplication());
        userViewModel = new ViewModelProvider(requireActivity(), new UserViewModelFactory(userRepository)).get(UserViewModel.class);
        accountManager = new AccountManager(getContext());

        FirebaseApp.initializeApp(getActivity().getApplication());

        oneTapClient = Identity.getSignInClient(requireActivity());
        signInRequest = BeginSignInRequest.builder()
                .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                        .setSupported(true)
                        .build())
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.default_web_client_id))
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                // Automatically sign in when exactly one credential is retrieved.
                .setAutoSelectEnabled(true)
                .build();

        startIntentSenderForResult = new ActivityResultContracts.StartIntentSenderForResult();

        activityResultLauncher = registerForActivityResult(startIntentSenderForResult, activityResult -> {
            if (activityResult.getResultCode() == Activity.RESULT_OK) {
                Log.d("WELCOME", "result.getResultCode() == Activity.RESULT_OK");
                try {
                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(activityResult.getData());
                    String idToken = credential.getGoogleIdToken();
                    Log.d("WELCOME", idToken);
                    if (idToken !=  null) {
                        // Got an ID token from Google. Use it to authenticate with Firebase.
                        loginWithGoogle(idToken);
                    }
                } catch (ApiException e) {
                    Snackbar.make(requireActivity().findViewById(android.R.id.content),
                            "error",
                            Snackbar.LENGTH_SHORT).show();
                }
            }
        });

    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater);

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        boolean isUserLogged = accountManager.getRememberMe();

        if(isUserLogged){
            boolean isAccessMethodGoogle = accountManager.getIsGoogleAccount();

            if(isAccessMethodGoogle){
                String googleAccessToken = accountManager.getGoogleIdToken();
                loginWithGoogle(googleAccessToken);
            }else{
                AccountManager.UserCredentials credentials = accountManager.getCredentials();
                loginWithEmail(credentials.getEmail(), credentials.getPassword());
            }
        }

        binding.buttonLogin.setOnClickListener(v -> {
            String email = Objects.requireNonNull(binding.textInputLayoutEmail.
                    getEditText()).getText().toString();
            String password = Objects.requireNonNull(binding.textInputLayoutPassword.
                    getEditText()).getText().toString();

            if(isEmailOk(email) && isPasswordOk(password)){
                userViewModel.getUserMutableLiveData(email, password).observe(
                        getViewLifecycleOwner(), result -> {
                            if(result.getTokenId() != null) {
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
        });

        binding.buttonRegistration.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registrationFragment);
        });

        binding.buttonGoogleLogin.setOnClickListener(v -> oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(requireActivity(), new OnSuccessListener<BeginSignInResult>() {
                    @Override
                    public void onSuccess(BeginSignInResult result) {
                        IntentSenderRequest intentSenderRequest =
                                new IntentSenderRequest.Builder(result.getPendingIntent()).build();
                        activityResultLauncher.launch(intentSenderRequest);
                    }
                })
                .addOnFailureListener(requireActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // No saved credentials found. Launch the One Tap sign-up flow, or
                        // do nothing and continue presenting the signed-out UI.
                        Log.d("WELCOME", e.getLocalizedMessage());

                        Snackbar.make(requireActivity().findViewById(android.R.id.content),
                                "no google account found",
                                Snackbar.LENGTH_SHORT).show();
                    }
                }));

    }

    private void loginWithGoogle(String googleAccessToken) {
        userViewModel.getGoogleUserMutableLiveData(googleAccessToken).observe(getViewLifecycleOwner(), user -> {
            if (user.getTokenId()!=null) {
                userViewModel.setAuthenticationError(false);
                saveGoogleUserInfo(googleAccessToken);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            } else {
                userViewModel.setAuthenticationError(true);
                Snackbar.make(requireActivity().findViewById(android.R.id.content),
                        "error",
                        Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void loginWithEmail(String email, String password){
        userViewModel.getUserMutableLiveData(email, password).observe(
                getViewLifecycleOwner(), result -> {
                    if(result.getTokenId() != null) {
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
    }

    private void saveUserInfo(String email, String password){
        if(binding.checkboxRememberMe.isChecked()){
            accountManager.setRememberMe(binding.checkboxRememberMe.isChecked());
            accountManager.saveUserInfo(email, password);
        }
    }

    private void saveGoogleUserInfo(String idToken){
        if(binding.checkboxRememberMe.isChecked()){
            accountManager.setRememberMe(binding.checkboxRememberMe.isChecked());
            accountManager.saveUserGoogleIdToken(idToken);
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
