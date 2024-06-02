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
    private MutableLiveData<Result> userMutableLiveData1;
    private MutableLiveData<Result> preferencesMutableLiveData;

    private boolean authenticationError;


    public UserViewModel(IUserRepository userRepository){
        this.userRepository = userRepository;
        this.authenticationError = false;
    }

    public MutableLiveData<Result> getUserMutableLiveData(String email, String password) {
        if(authenticationError == true || userMutableLiveData1 == null){
            getUserData(email, password);
        }
        return userMutableLiveData1;
    }

    public MutableLiveData<Result> getUserMutableLiveData(String email, String password, String username, String fullName) {
        if(authenticationError == true || userMutableLiveData1 == null){
            getUserData(email, password, username, fullName);
        }
        return userMutableLiveData1;
    }

    public MutableLiveData<Result> getUserInfo(String tokenId){
        if(userMutableLiveData1 == null)
            return userRepository.getUserInfo(tokenId);

        return userMutableLiveData1;
    }

    private void getUserData(String email, String password) {
        userMutableLiveData1 = userRepository.getUser(email, password);
    }

    private void getUserData(String email, String password, String username, String fullName){
        userMutableLiveData1 = userRepository.getUser(email, password, username, fullName);
    }

    public void setAuthenticationError(boolean authenticationError){
        this.authenticationError = authenticationError;
    }

    public MutableLiveData<Result> logout(){
        userMutableLiveData1 = userRepository.logout();
        return userMutableLiveData1;
    }

    public User getLoggedUser(){
        return userRepository.getLoggedUser();
    }

    public MutableLiveData<Result> setUserInfo(String username, String fullName){
        if(userMutableLiveData1 == null){
            User currentUser = getLoggedUser();
            User user = new User(username, currentUser.getEmail(), getLoggedUser().getTokenId(), fullName);
            return userRepository.setUserInfo(user);
        }

        return userMutableLiveData1;

    }

    public MutableLiveData<Result> getGoogleUserMutableLiveData(String token) {
        if (userMutableLiveData1 == null) {
            getUserData(token);
        }
        return userMutableLiveData1;
    }

    private void getUserData(String token) {
        userMutableLiveData1 = userRepository.getGoogleUser(token);
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
