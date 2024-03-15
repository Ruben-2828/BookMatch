package com.example.bookmatch.utils;

import android.app.Application;

import com.example.bookmatch.data.repository.user.IUserRepository;
import com.example.bookmatch.data.repository.user.UserRepository;

public class ServiceLocator {

    private static volatile ServiceLocator INSTANCE = null;

    private ServiceLocator(){

    }

    public static ServiceLocator getInstance(){
        if(INSTANCE == null){
            synchronized (ServiceLocator.class){
                if(INSTANCE == null){
                    INSTANCE = new ServiceLocator();
                }
            }
        }

        return INSTANCE;
    }

    public IUserRepository getUserRepository(Application application){
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(application);

        return new UserRepository();
    }
}
