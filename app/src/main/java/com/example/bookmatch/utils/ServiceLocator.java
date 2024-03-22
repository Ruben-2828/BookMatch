package com.example.bookmatch.utils;

import android.app.Application;

import com.example.bookmatch.data.repository.user.IUserRepository;
import com.example.bookmatch.data.repository.user.UserRepository;

public class ServiceLocator {

    private static ServiceLocator instance = null;

    private ServiceLocator() {}

    public static ServiceLocator getInstance() {
        if (instance == null) {
            synchronized(ServiceLocator.class) {
                instance = new ServiceLocator();
            }
        }
        return instance;
    }

    public IUserRepository getUserRepository(Application application){
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(application);

        return new UserRepository();
    }
}
