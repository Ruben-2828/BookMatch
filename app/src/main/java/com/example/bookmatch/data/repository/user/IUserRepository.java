package com.example.bookmatch.data.repository.user;

import androidx.lifecycle.MutableLiveData;

import com.example.bookmatch.model.User;

public interface IUserRepository {

    void signUp(String email, String password);
    void signIn(String email, String password);
    User getLoggedUser();
    MutableLiveData<User> getUser(String email, String password, boolean isUserRegistered);
    MutableLiveData<User> logout();
}
