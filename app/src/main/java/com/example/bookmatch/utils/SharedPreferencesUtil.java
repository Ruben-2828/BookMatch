package com.example.bookmatch.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {
    private final Application application;

    public SharedPreferencesUtil(Application application){
        this.application = application;
    }

    public void writeStringDataSharedPreferences(String name, String key, String value){
        SharedPreferences sharedPref = application.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String readStringDataSharedPreferences(String name, String key){
        SharedPreferences sharedPref = application.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sharedPref.getString(key, null);
    }
}
