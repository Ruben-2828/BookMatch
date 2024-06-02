package com.example.bookmatch.data.repository.user;

import androidx.lifecycle.MutableLiveData;

import com.example.bookmatch.model.Result;
import com.example.bookmatch.model.User;
import com.example.bookmatch.model.UserPreferences;

public interface IUserRepository {

    void signUp(String email, String password, String username, String fullName);
    void signIn(String email, String password);
    User getLoggedUser();
    MutableLiveData<Result> getUser(String email, String password);
    MutableLiveData<Result> getUser(String email, String password, String username, String fullName);
    MutableLiveData<Result> getUserInfo(String tokenId);
    MutableLiveData<Result> getGoogleUser(String idToken);
    MutableLiveData<Result> logout();
    MutableLiveData<Result> getPreferences(String idToken);
    MutableLiveData<Result> setPreferences(String tokenId, UserPreferences userPreferences);
    void signInWithGoogle(String token);
    MutableLiveData<Result> setUserInfo(User user);
    MutableLiveData<Result> uploadImage(byte[] byteArray);
}
