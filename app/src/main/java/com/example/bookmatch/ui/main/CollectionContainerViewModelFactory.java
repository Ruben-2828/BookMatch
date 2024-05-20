package com.example.bookmatch.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class CollectionContainerViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;

    public CollectionContainerViewModelFactory(Application application) {
        mApplication = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CollectionContainerViewModel.class)) {
            return (T) new CollectionContainerViewModel(mApplication);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
