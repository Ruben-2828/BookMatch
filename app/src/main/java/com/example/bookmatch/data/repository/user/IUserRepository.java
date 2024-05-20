package com.example.bookmatch.data.repository.user;

import androidx.lifecycle.MutableLiveData;

import com.example.bookmatch.model.Result;
import com.example.bookmatch.model.User;
import com.example.bookmatch.model.UserPreferences;

public interface IUserRepository {

    void signUp(String email, String password, String username, String fullName);
    void signIn(String email, String password);
    User getLoggedUser();
    MutableLiveData<User> getUser(String email, String password);
    MutableLiveData<User> getUser(String email, String password, String username, String fullName);
    MutableLiveData<User> getUserInfo(String tokenId);
    MutableLiveData<User> getGoogleUser(String idToken);
    MutableLiveData<User> logout();
    MutableLiveData<Result> getPreferences(String idToken);
    MutableLiveData<Result> setPreferences(String tokenId, UserPreferences userPreferences);
    void signInWithGoogle(String token);
    void setUserInfo(User user);
}
