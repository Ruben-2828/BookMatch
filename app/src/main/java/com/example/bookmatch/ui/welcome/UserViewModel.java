package com.example.bookmatch.ui.welcome;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookmatch.data.repository.user.IUserRepository;
import com.example.bookmatch.model.User;
public class UserViewModel extends ViewModel {
    private final IUserRepository userRepository;
    private MutableLiveData<User> userMutableLiveData;

    private boolean loginError;

    //Mutable live data

    public UserViewModel(IUserRepository userRepository){
        this.userRepository = userRepository;
        this.loginError = false;
    }

    public MutableLiveData<User> getUserMutableLiveData(String email, String password, boolean isUserRegistered) {
        if(loginError == true || userMutableLiveData == null){
            getUserData(email, password, isUserRegistered);
        }
        return userMutableLiveData;
    }

    private void getUserData(String email, String password, boolean isUserRegistered) {
        userMutableLiveData = userRepository.getUser(email, password, isUserRegistered);
    }

    public void setLoginError(boolean loginError){
        this.loginError = loginError;
    }
}
