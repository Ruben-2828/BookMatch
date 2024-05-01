package com.example.bookmatch.data.repository.user;

import androidx.lifecycle.MutableLiveData;

import com.example.bookmatch.model.User;

public interface IUserRepository {

    void signUp(String email, String password, String username, String fullName);
    void signIn(String email, String password);
    User getLoggedUser();
    MutableLiveData<User> getUser(String email, String password);
    MutableLiveData<User> getUser(String email, String password, String username, String fullName);
    MutableLiveData<User> getUserInfo(String tokenId);
    MutableLiveData<User> logout();
}
