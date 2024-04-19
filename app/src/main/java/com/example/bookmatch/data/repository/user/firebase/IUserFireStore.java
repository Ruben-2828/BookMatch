package com.example.bookmatch.data.repository.user.firebase;

import com.example.bookmatch.data.repository.user.UserResponseCallback;
import com.example.bookmatch.model.User;

public abstract class IUserFireStore {
    protected UserResponseCallback responseCallback;

    public void setUserResponseCallback(UserResponseCallback responseCallback) {
        this.responseCallback = responseCallback;
    }

    public abstract void saveUserData(User user);
    public abstract void getUserData(String userId);
}
