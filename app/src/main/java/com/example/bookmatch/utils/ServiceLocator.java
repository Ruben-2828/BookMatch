package com.example.bookmatch.utils;

import android.app.Application;

import com.example.bookmatch.data.database.BookRoomDatabase;
import com.example.bookmatch.data.repository.user.IUserRepository;
import com.example.bookmatch.data.repository.user.UserRepository;
import com.example.bookmatch.data.service.BookAPIService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

    public BookAPIService getBooksApiService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.API_BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit.create(BookAPIService.class);
    }

    public BookRoomDatabase getBookDao(Application application) {
        return BookRoomDatabase.getDatabase(application);
    }
}
