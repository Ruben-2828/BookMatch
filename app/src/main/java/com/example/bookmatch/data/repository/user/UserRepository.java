package com.example.bookmatch.data.repository.user;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.bookmatch.data.repository.user.auth.IUserAuthentication;
import com.example.bookmatch.model.User;


public class UserRepository implements IUserRepository, UserResponseCallback{
    private final MutableLiveData<User> userMutableLiveData;
    private final IUserAuthentication userAuthentication;

    public UserRepository(IUserAuthentication userAuthentication){
        this.userMutableLiveData = new MutableLiveData<>();
        this.userAuthentication = userAuthentication;
        this.userAuthentication.setUserResponseCallback(this);
    }
    @Override
    public void signUp(String email, String password) {
        userAuthentication.signUp(email, password);
    }

    @Override
    public void signIn(String email, String password) {
       userAuthentication.signIn(email, password);
    }

    @Override
    public User getLoggedUser() {
        return userAuthentication.getLoggedUser();
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

    @Override
    public MutableLiveData<User> logout() {
        userAuthentication.logout();
        return userMutableLiveData;
    }

    @Override
    public void onSuccessFromAuthentication(User user) {
        //TODO: salvare i dati dell'utente
        Log.d("WELCOME", "login callback authentication");
        userMutableLiveData.postValue(user);
    }

    @Override
    public void onFailureFromAuthentication() {
        User user = new User(null, null, null);
        userMutableLiveData.postValue(user);
    }

    @Override
    public void onSuccessLogout() {
        User user = new User(null, null, null);
        userMutableLiveData.postValue(user);
    }
}
