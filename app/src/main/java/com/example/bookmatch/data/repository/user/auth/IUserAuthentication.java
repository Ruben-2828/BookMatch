package com.example.bookmatch.data.repository.user.auth;

import com.example.bookmatch.data.repository.user.UserResponseCallback;
import com.example.bookmatch.model.User;

public abstract class IUserAuthentication {
    protected UserResponseCallback responseCallback;

    public void setUserResponseCallback(UserResponseCallback responseCallback) {
        this.responseCallback = responseCallback;
    }

    public abstract User getLoggedUser();
    public abstract void logout();
    public abstract void signUp(String email, String password, String username, String fullName);
    public abstract void signIn(String email, String password);
    public abstract void signInWithGoogle(String idToken);

}
