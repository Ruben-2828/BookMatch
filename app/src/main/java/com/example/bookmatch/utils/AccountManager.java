package com.example.bookmatch.utils;

import static com.example.bookmatch.utils.Constants.SHARED_PREF_NAME;
import static com.example.bookmatch.utils.Constants.USER_PREFERENCES_EMAIL;
import static com.example.bookmatch.utils.Constants.USER_PREFERENCES_PASSWORD;
import static com.example.bookmatch.utils.Constants.USER_REMEMBER_ME_SP;

import android.content.Context;
import android.content.SharedPreferences;

public class AccountManager {

    public void setRememberMe(Boolean rememberMe, Context context) {
        SharedPreferences sp = context.getSharedPreferences(
                SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(USER_REMEMBER_ME_SP, rememberMe);
        editor.apply();
    }

    public boolean getRememberMe(Context context){
        SharedPreferences sp = context.getSharedPreferences(
                SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(USER_REMEMBER_ME_SP, false);
    }

    public void saveUserInfo(String email, String password, Context context){
        SharedPreferences sp = context.getSharedPreferences(
                SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(USER_PREFERENCES_PASSWORD, password);
        editor.putString(USER_PREFERENCES_EMAIL, email);
        editor.apply();
    }

    public UserCredentials getCredentials(Context context){
        String password, email;
        SharedPreferences sp = context.getSharedPreferences(
                SHARED_PREF_NAME, Context.MODE_PRIVATE);

        password = sp.getString(USER_PREFERENCES_PASSWORD, "");
        email = sp.getString(USER_PREFERENCES_EMAIL, "");

        return new UserCredentials(email, password);
    }

    public class UserCredentials{
        private String email, password;

        public UserCredentials(String email, String password){
            this.email = email;
            this.password = password;
        }

        public String getEmail(){
            return email;
        }

        public String getPassword(){
            return password;
        }
    }
}
