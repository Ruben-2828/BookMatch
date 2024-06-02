package com.example.bookmatch.data.repository.user.firebase;

import static com.example.bookmatch.utils.Constants.ERROR_WRITING_FIRESTORE;
import static com.example.bookmatch.utils.Constants.NO_SUCH_DOCUMENT_ERROR;
import static com.example.bookmatch.utils.Constants.USERS_COLLECTION_NAME;
import static com.example.bookmatch.utils.Constants.USERS_PREFERENCES_NAME;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.bookmatch.model.User;
import com.example.bookmatch.model.UserPreferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class UserFireStore extends IUserFireStore{
    private static final String TAG = "WELCOME";
    private FirebaseFirestore dbIstance;

    public UserFireStore(){
        dbIstance = FirebaseFirestore.getInstance();
    }

    @Override
    public void saveUserData(User user, Boolean override) {
        Map<String, Object> userToSave = user.toHashMap();
        dbIstance.collection(USERS_COLLECTION_NAME).document(user.getTokenId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (!override && document.exists()) {
                                //document exists
                                responseCallback.onSuccessFromFirestore(user);
                            } else {
                                //document does not exist
                                writeUserData(user, userToSave);
                            }
                        } else {
                            Log.d(TAG, "Failed with: ", task.getException());
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d("WELCOME", e.getMessage());
                    responseCallback.onFailureFromFireStore(e.getMessage());
                });
    }

    @Override
    public void getUserData(String userId) {
        DocumentReference docRef = dbIstance.collection(USERS_COLLECTION_NAME).document(userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        User user = buildUserFromDocument(document, userId);
                        responseCallback.onSuccessFromFirestore(user);
                    } else {
                        responseCallback.onFailureFromFireStore(NO_SUCH_DOCUMENT_ERROR);
                    }
                } else {
                    responseCallback.onFailureFromFireStore(task.getException().toString());
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    @Override
    public void saveUserPreferences(String tokenId, UserPreferences userPreferences) {
        Map<String, Object> preferencesToSave = userPreferences.toHashMap();
        dbIstance.collection(USERS_PREFERENCES_NAME).document(tokenId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            writePreferencesData(tokenId, userPreferences, preferencesToSave);
                        } else {
                            Log.d(TAG, "Failed with: ", task.getException());
                            responseCallback.onFailureFromFireStore("Failed with: " + task.getException().toString());
                        }
                    }
                });
    }

    @Override
    public void getUserPreferences(String tokenId) {
        DocumentReference docRef = dbIstance.collection(USERS_PREFERENCES_NAME).document(tokenId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        UserPreferences userPreferences = buildPreferencesFromDocument(document);
                        responseCallback.onSuccessFromFirestore(userPreferences);
                    } else {
                        responseCallback.onFailureFromFireStore(NO_SUCH_DOCUMENT_ERROR);
                    }
                } else {
                    responseCallback.onFailureFromFireStore(task.getException().toString());
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void writeUserData(User user, Map<String, Object> userToSave){
        dbIstance.collection(USERS_COLLECTION_NAME).document(user.getTokenId())
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
                        responseCallback.onFailureFromFireStore(e.toString());
                    }
                });
    }

    private void writePreferencesData(String tokenId, UserPreferences userPreferences, Map<String, Object> preferencesToSave){
        dbIstance.collection(USERS_PREFERENCES_NAME).document(tokenId)
                .set(preferencesToSave)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        responseCallback.onSuccessFromFirestore(userPreferences);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        responseCallback.onFailureFromFireStore(ERROR_WRITING_FIRESTORE);
                    }
                });
    }

    private User buildUserFromDocument(DocumentSnapshot document, String tokenId) {
        return new User(
                "" + document.get("username"),
                "" + document.get("email"),
                tokenId,
                "" +document.get("fullName")
                );
    }

    private UserPreferences buildPreferencesFromDocument(DocumentSnapshot document) {
        return new UserPreferences(
                "" + document.get("genre"),
                "" + document.get("author"),
                "" + document.get("book")
        );
    }
}
