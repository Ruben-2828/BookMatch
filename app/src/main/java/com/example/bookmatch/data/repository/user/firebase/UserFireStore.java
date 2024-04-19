package com.example.bookmatch.data.repository.user.firebase;

import com.example.bookmatch.model.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserFireStore extends IUserFireStore{
    private FirebaseFirestore dbIstance;

    @Override
    public void saveUserData(User user) {
        //Map<String, Object> userToSave = user.toHashMap();
    }

    @Override
    public void getUserData(String userId) {

    }
}
