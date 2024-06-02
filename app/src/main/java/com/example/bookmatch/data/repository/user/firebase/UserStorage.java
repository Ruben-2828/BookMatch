package com.example.bookmatch.data.repository.user.firebase;

import static com.example.bookmatch.utils.Constants.BASE_URL_IMAGES_STORAGE;
import static com.example.bookmatch.utils.Constants.ERROR_WHILE_UPLOADING_IMAGE;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class UserStorage extends IUserStorage{
    private FirebaseStorage storage;

    public UserStorage(){
        this.storage = FirebaseStorage.getInstance();
    }
    @Override
    public void saveImageInStorage(byte[] data) {
        StorageReference ref = storage.getReference().child(BASE_URL_IMAGES_STORAGE + UUID.randomUUID().toString());
        UploadTask uploadTask = ref.putBytes(data);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    responseCallback.onFailureFromStorage(ERROR_WHILE_UPLOADING_IMAGE);
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    Log.d("WELCOME", task.getResult().toString());
                    responseCallback.onSuccessFromStorage(task.getResult().toString());
                } else {
                    responseCallback.onFailureFromStorage(ERROR_WHILE_UPLOADING_IMAGE);
                }
            }
        });
    }
}
