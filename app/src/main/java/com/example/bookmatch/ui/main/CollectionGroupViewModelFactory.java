package com.example.bookmatch.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class CollectionGroupViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;

    public CollectionGroupViewModelFactory(Application application) {
        mApplication = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CollectionGroupViewModel.class)) {
            return (T) new CollectionGroupViewModel(mApplication);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}