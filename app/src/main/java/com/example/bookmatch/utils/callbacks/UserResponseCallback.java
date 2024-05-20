package com.example.bookmatch.utils.callbacks;

import com.example.bookmatch.model.User;
import com.example.bookmatch.model.UserPreferences;

public interface UserResponseCallback {
    void onSuccessFromAuthentication(User user);
    void onSuccessFromFirestore(User user);
    void onSuccessFromFirestore(UserPreferences preferences);
    void onFailureFromAuthentication();
    void onFailureFromFireStore(String error);
    void onSuccessLogout();
}
