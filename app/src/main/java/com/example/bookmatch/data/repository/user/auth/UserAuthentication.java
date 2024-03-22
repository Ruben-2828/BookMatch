package com.example.bookmatch.data.repository.user.auth;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.bookmatch.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

    }

    @Override
    public void signUp(String email, String password) {

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
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                        }
                    }
                });
    }
}
