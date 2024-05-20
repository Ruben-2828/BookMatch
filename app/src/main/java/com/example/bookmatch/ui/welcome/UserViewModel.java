package com.example.bookmatch.ui.welcome;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookmatch.data.repository.user.IUserRepository;
import com.example.bookmatch.model.Result;
import com.example.bookmatch.model.User;
import com.example.bookmatch.model.UserPreferences;

public class UserViewModel extends ViewModel {
    private final IUserRepository userRepository;
    private MutableLiveData<User> userMutableLiveData;
    private MutableLiveData<Result> preferencesMutableLiveData;

    private boolean authenticationError;


    public UserViewModel(IUserRepository userRepository){
        this.userRepository = userRepository;
        this.authenticationError = false;
    }

    public MutableLiveData<User> getUserMutableLiveData(String email, String password) {
        if(authenticationError == true || userMutableLiveData == null){
            getUserData(email, password);
        }
        return userMutableLiveData;
    }

    public MutableLiveData<User> getUserMutableLiveData(String email, String password, String username, String fullName) {
        if(authenticationError == true || userMutableLiveData == null){
            getUserData(email, password, username, fullName);
        }
        return userMutableLiveData;
    }

    public MutableLiveData<User> getUserInfo(String tokenId){
        if(userMutableLiveData == null)
            return userRepository.getUserInfo(tokenId);

        return userMutableLiveData;
    }

    private void getUserData(String email, String password) {
        userMutableLiveData = userRepository.getUser(email, password);
    }

    private void getUserData(String email, String password, String username, String fullName){
        userMutableLiveData = userRepository.getUser(email, password, username, fullName);
    }

    public void setAuthenticationError(boolean authenticationError){
        this.authenticationError = authenticationError;
    }

    public MutableLiveData<User> logout(){
        userMutableLiveData = userRepository.logout();
        return userMutableLiveData;
    }

    public User getLoggedUser(){
        return userRepository.getLoggedUser();
    }

    public void setUserInfo(String username, String fullName){
        User currentUser = getLoggedUser();
        User user = new User(username, currentUser.getEmail(), getLoggedUser().getTokenId(), fullName);
        userRepository.setUserInfo(user);
    }

    public MutableLiveData<User> getGoogleUserMutableLiveData(String token) {
        if (userMutableLiveData == null) {
            getUserData(token);
        }
        return userMutableLiveData;
    }

    private void getUserData(String token) {
        userMutableLiveData = userRepository.getGoogleUser(token);
    }

    public MutableLiveData<Result> getPreferences() {
        if (preferencesMutableLiveData == null) {
            getPreferencesData();
        }
        return preferencesMutableLiveData;
    }

    public MutableLiveData<Result> setPreferences(UserPreferences preferences) {
        if (preferencesMutableLiveData == null) {
            setPreferencesData(preferences);
        }
        return preferencesMutableLiveData;
    }

    private void getPreferencesData() {
        User currentUser = getLoggedUser();
        preferencesMutableLiveData = userRepository.getPreferences(currentUser.getTokenId());
    }

    private void setPreferencesData(UserPreferences userPreferences) {
        User currentUser = getLoggedUser();
        preferencesMutableLiveData = userRepository.setPreferences(currentUser.getTokenId(), userPreferences);
    }
}
