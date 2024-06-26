package com.example.bookmatch.data.repository.user;

import androidx.lifecycle.MutableLiveData;

import com.example.bookmatch.data.source.auth.IUserAuthentication;
import com.example.bookmatch.data.source.auth.UserAuthentication;
import com.example.bookmatch.data.source.firebase.IUserFireStore;
import com.example.bookmatch.data.source.firebase.IUserStorage;
import com.example.bookmatch.data.source.firebase.UserFireStore;
import com.example.bookmatch.data.source.firebase.UserStorage;
import com.example.bookmatch.model.Result;
import com.example.bookmatch.model.User;
import com.example.bookmatch.model.UserPreferences;
import com.example.bookmatch.utils.callbacks.UserResponseCallback;


public class UserRepository implements IUserRepository, UserResponseCallback {
    private final MutableLiveData<Result> userMutableLiveData;
    private final MutableLiveData<Result> userPreferencesMutableLiveData;
    private final MutableLiveData<Result> userStorageMutableLiveData;
    private final IUserAuthentication userAuthentication;
    private final IUserFireStore userFireStore;
    private final IUserStorage userStorage;

    public UserRepository(){
        this.userMutableLiveData = new MutableLiveData<>();
        this.userPreferencesMutableLiveData = new MutableLiveData<>();
        this.userStorageMutableLiveData = new MutableLiveData<>();
        this.userAuthentication = new UserAuthentication();
        this.userFireStore = new UserFireStore();;
        this.userStorage = new UserStorage();
        this.userAuthentication.setUserResponseCallback(this);
        this.userFireStore.setUserResponseCallback(this);
        this.userStorage.setUserResponseCallback(this);
    }
    @Override
    public void signUp(String email, String password, String username, String fullName) {
        userAuthentication.signUp(email, password, username, fullName);
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
    public MutableLiveData<Result> getUser(String email, String password) {
        signIn(email, password);

        return userMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> getUser(String email, String password, String username, String fullName) {
        signUp(email, password, username, fullName);
        return userMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> getUserInfo(String tokenId) {
        userFireStore.getUserData(tokenId);

        return userMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> getGoogleUser(String idToken) {
        signInWithGoogle(idToken);
        return userMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> logout() {
        userAuthentication.logout();
        return userMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> getPreferences(String idToken) {
        userFireStore.getUserPreferences(idToken);
        return userPreferencesMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> setPreferences(String tokenId, UserPreferences userPreferences) {
        userFireStore.saveUserPreferences(tokenId, userPreferences);
        return userPreferencesMutableLiveData;
    }

    @Override
    public void signInWithGoogle(String token) {
        userAuthentication.signInWithGoogle(token);
    }

    @Override
    public MutableLiveData<Result> setUserInfo(User user) {
        userFireStore.saveUserData(user, true);
        return userMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> uploadImage(byte[] byteArray) {
        userStorage.saveImageInStorage(byteArray);
        return userStorageMutableLiveData;
    }

    @Override
    public void onSuccessFromAuthentication(User user) {
        userFireStore.saveUserData(user, false);
    }
    @Override
    public void onSuccessFromFirestore(User user) {
        Result.UserResponseSuccess result = new Result.UserResponseSuccess(user);
        userMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessFromFirestore(UserPreferences preferences) {
        Result.PreferencesResponseSuccess result = new Result.PreferencesResponseSuccess(preferences);
        userPreferencesMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessFromStorage(String url) {
        Result.StorageResponseSuccess result = new Result.StorageResponseSuccess(url);
        userStorageMutableLiveData.postValue(result);
    }

    @Override
    public void onFailureFromAuthentication(String errorMessage) {
        Result.Error result = new Result.Error(errorMessage);
        userMutableLiveData.postValue(result);
    }

    @Override
    public void onFailureFromFireStore(String error) {
        Result.Error result = new Result.Error(error);
        userPreferencesMutableLiveData.postValue(result);
    }

    @Override
    public void onFailureFromStorage(String error) {
        Result.Error result = new Result.Error(error);
        userMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessLogout() {
        Result.LogoutResponseSuccess result = new Result.LogoutResponseSuccess();
        userMutableLiveData.postValue(result);
    }
}
