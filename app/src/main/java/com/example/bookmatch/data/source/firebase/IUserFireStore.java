package com.example.bookmatch.data.source.firebase;

import com.example.bookmatch.model.UserPreferences;
import com.example.bookmatch.utils.callbacks.UserResponseCallback;
import com.example.bookmatch.model.User;

public abstract class IUserFireStore {
    protected UserResponseCallback responseCallback;

    public void setUserResponseCallback(UserResponseCallback responseCallback) {
        this.responseCallback = responseCallback;
    }

    public abstract void saveUserData(User user, Boolean override);
    public abstract void getUserData(String userId);

    public abstract void saveUserPreferences(String tokenId, UserPreferences userPreferences);
    public abstract void getUserPreferences(String tokenId);
}
