package com.example.bookmatch.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {
    private Context context;

    public SharedPreferencesUtil(Context context){
        this.context = context;
    }

    public void writeStringDataSharedPreferences(String name, String key, String value){
        SharedPreferences sp = context.getSharedPreferences(
                name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void writeBooleanDataSharedPreferences(String name, String key, boolean value){
        SharedPreferences sp = context.getSharedPreferences(
                name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean readBooleanDataSharedPreferences(String name, String key){
        SharedPreferences sp = context.getSharedPreferences(
                name, Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    public String readStringDataSharedPreferences(String name, String key){
        SharedPreferences sharedPref = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sharedPref.getString(key, "");
    }

    public void writeLongDataSharedPreferences(String name, String key, long value){
        SharedPreferences sp = context.getSharedPreferences(
                name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public Long readLongDataSharedPreferences(String name, String key){
        SharedPreferences sharedPref = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sharedPref.getLong(key, 0);
    }
}
