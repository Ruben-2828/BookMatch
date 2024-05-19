package com.example.bookmatch.data.repository.user.auth;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.bookmatch.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
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
                            User user = new User(username, createdUser.getEmail(), createdUser.getUid(), fullName);
                            responseCallback.onSuccessFromAuthentication(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("WELCOME", "createUserWithEmail:failure", task.getException());
                            responseCallback.onFailureFromAuthentication();
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
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("WELCOME", "signInWithEmail:failure", task.getException());
                            responseCallback.onFailureFromAuthentication();
                        }
                    }
                });
    }

    @Override
    public void signInWithGoogle(String idToken) {
        if (idToken !=  null) {
            // Got an ID token from Google. Use it to authenticate with Firebase.
            AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
            mAuth.signInWithCredential(firebaseCredential).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("WELCOME", "signInWithCredential:success");
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    if (firebaseUser != null) {
                        responseCallback.onSuccessFromAuthentication(
                                new User(firebaseUser.getDisplayName(),
                                        firebaseUser.getEmail(),
                                        firebaseUser.getUid()
                                )
                        );
                    } else {
                        responseCallback.onFailureFromAuthentication();
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("WELCOME", "signInWithCredential:failure", task.getException());
                    responseCallback.onFailureFromAuthentication();
                }
            });
        }
    }
}
