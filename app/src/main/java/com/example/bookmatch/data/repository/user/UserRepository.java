package com.example.bookmatch.data.repository.user;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.bookmatch.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserRepository implements IUserRepository{
    private final MutableLiveData<User> userMutableLiveData;
    private FirebaseAuth mAuth;

    public UserRepository(){
        this.userMutableLiveData = new MutableLiveData<>();
        this.mAuth = FirebaseAuth.getInstance();
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
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                        }
                    }
                });
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
    public MutableLiveData<User> getUser(String email, String password, boolean isUserRegistered) {
        if (isUserRegistered) {
            signIn(email, password);
        } else {
            signUp(email, password);
        }
        return userMutableLiveData;
    }
}
