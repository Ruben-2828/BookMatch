package com.example.bookmatch.data.repository.user;

import com.example.bookmatch.model.User;

//TODO: Ciao sono quack, le callback non andrebbero messe in utils? Il prof fa cosi. Baci
public interface UserResponseCallback {
    void onSuccessFromAuthentication(User user);
    void onFailureFromAuthentication();
    void onSuccessLogout();
}
