package com.example.bookmatch.data.repository.user;

import com.example.bookmatch.model.User;

public interface UserResponseCallback {
    void onSuccessFromAuthentication(User user);
    void onFailureFromAuthentication(String message);
    void onSuccessLogout();
}
