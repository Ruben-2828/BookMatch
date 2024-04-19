package com.example.bookmatch.data.repository.user.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.bookmatch.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserFireStore extends IUserFireStore{
    private static final String TAG = "WELCOME";
    private FirebaseFirestore dbIstance;

    public UserFireStore(){
        dbIstance = FirebaseFirestore.getInstance();
    }

    @Override
    public void saveUserData(User user) {
        Map<String, Object> userToSave = user.toHashMap();
        dbIstance.collection("users").document(user.getTokenId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                //document exists
                                responseCallback.onSuccessFromFirestore(user);
                            } else {
                                //document does not exist
                                writeData(user, userToSave);
                            }
                        } else {
                            Log.d(TAG, "Failed with: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public void getUserData(String userId) {

    }

    private void writeData(User user, Map<String, Object> userToSave){
        dbIstance.collection("users").document(user.getTokenId())
                .set(userToSave)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        responseCallback.onSuccessFromFirestore(user);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        responseCallback.onFailureFromAuthentication();
                    }
                });
    }
}
