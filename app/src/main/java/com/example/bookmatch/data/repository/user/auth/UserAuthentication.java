package com.example.bookmatch.data.repository.user.auth;

import static com.example.bookmatch.utils.Constants.INVALID_CREDENTIALS_ERROR;
import static com.example.bookmatch.utils.Constants.INVALID_USER_ERROR;
import static com.example.bookmatch.utils.Constants.NO_CONNECTION;
import static com.example.bookmatch.utils.Constants.UNEXPECTED_ERROR;
import static com.example.bookmatch.utils.Constants.USER_COLLISION_ERROR;
import static com.example.bookmatch.utils.Constants.WEAK_PASSWORD_ERROR;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.bookmatch.model.Result;
import com.example.bookmatch.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class UserAuthentication extends IUserAuthentication {
    private FirebaseAuth mAuth;

    public UserAuthentication(){
        this.mAuth = FirebaseAuth.getInstance();
    }
    @Override
    public User getLoggedUser() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            return null;
        }

        return new User(currentUser.getDisplayName(), currentUser.getEmail(), currentUser.getUid());
    }

    @Override
    public void logout() {
        mAuth.signOut();
        responseCallback.onSuccessLogout();
    }

    @Override
    public void signUp(String email, String password, String username, String fullName) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("WELCOME", "createUserWithEmail:success");
                            FirebaseUser createdUser = mAuth.getCurrentUser();
                            if(createdUser != null){
                                User user = new User(username, createdUser.getEmail(), createdUser.getUid(), fullName);
                                responseCallback.onSuccessFromAuthentication(user);
                            }else{
                                responseCallback.onFailureFromAuthentication(getErrorMessage(task.getException()));
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("WELCOME", "createUserWithEmail:failure", task.getException());
                            responseCallback.onFailureFromAuthentication(getErrorMessage(task.getException()));
                        }
                    }
                });
    }

    @Override
    public void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user!=null){
                                User loggedUser = new User(user.getDisplayName(), user.getEmail(), user.getUid());
                                responseCallback.onSuccessFromAuthentication(loggedUser);
                            }else{
                                responseCallback.onFailureFromAuthentication(getErrorMessage(task.getException()));
                            }
                        } else {
                            Log.w("WELCOME", "signInWithEmail:failure", task.getException());
                            responseCallback.onFailureFromAuthentication(getErrorMessage(task.getException()));
                        }
                    }
                });
    }

    @Override
    public void signInWithGoogle(String idToken) {
        if (idToken !=  null) {
            AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
            mAuth.signInWithCredential(firebaseCredential).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("WELCOME", "signInWithCredential:success");
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    if (firebaseUser != null) {
                        User user =  new User(firebaseUser.getDisplayName(),
                                firebaseUser.getEmail(),
                                firebaseUser.getUid()
                        );

                        responseCallback.onSuccessFromAuthentication(user);
                    } else {
                        responseCallback.onFailureFromAuthentication(getErrorMessage(task.getException()));
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("WELCOME", "signInWithCredential:failure", task.getException());
                    responseCallback.onFailureFromAuthentication(getErrorMessage(task.getException()));
                }
            });
        }else{
            responseCallback.onFailureFromAuthentication(UNEXPECTED_ERROR);
        }
    }

    private String getErrorMessage(Exception exception) {
        if (exception instanceof FirebaseAuthWeakPasswordException) {
            return WEAK_PASSWORD_ERROR;
        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            return INVALID_CREDENTIALS_ERROR;
        } else if (exception instanceof FirebaseAuthInvalidUserException) {
            return INVALID_USER_ERROR;
        } else if (exception instanceof FirebaseAuthUserCollisionException) {
            return USER_COLLISION_ERROR;
        } else if (exception instanceof FirebaseNetworkException) {
            return NO_CONNECTION;
        }
        return UNEXPECTED_ERROR;
    }
}
