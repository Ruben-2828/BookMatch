package com.example.bookmatch.data.repository.user;

import androidx.lifecycle.MutableLiveData;

public interface IUserRepository {

    void signUp(String email, String password);
    void signIn(String email, String password);
}
