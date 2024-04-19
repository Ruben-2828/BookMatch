package com.example.bookmatch.ui.welcome;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookmatch.data.repository.user.IUserRepository;
import com.example.bookmatch.model.User;
public class UserViewModel extends ViewModel {
    private final IUserRepository userRepository;
    private MutableLiveData<User> userMutableLiveData;

    private boolean authenticationError;

    //Mutable live data

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
}
