package com.example.bookmatch.ui.welcome;

import androidx.lifecycle.ViewModel;

import com.example.bookmatch.data.repository.user.IUserRepository;

public class UserViewModel extends ViewModel {

    private final IUserRepository userRepository;

    public UserViewModel(IUserRepository userRepository){
        this.userRepository = userRepository;
    }

}
