package com.example.bookmatch.utils;

import static com.example.bookmatch.utils.Constants.SHARED_PREF_NAME;
import static com.example.bookmatch.utils.Constants.USER_PREFERENCES_EMAIL;
import static com.example.bookmatch.utils.Constants.USER_PREFERENCES_GOOGLE_ACCESS_METHOD;
import static com.example.bookmatch.utils.Constants.USER_PREFERENCES_GOOGLE_ID_TOKEN;
import static com.example.bookmatch.utils.Constants.USER_PREFERENCES_PASSWORD;
import static com.example.bookmatch.utils.Constants.USER_REMEMBER_ME_SP;

import android.accounts.Account;
import android.content.Context;
import android.content.SharedPreferences;

public class AccountManager {

    private Context context;

    public AccountManager(Context context){
        this.context = context;
    }
    public void setRememberMe(Boolean rememberMe) {
        SharedPreferences sp = context.getSharedPreferences(
                SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(USER_REMEMBER_ME_SP, rememberMe);
        editor.apply();
    }

    public boolean getRememberMe(){
        SharedPreferences sp = context.getSharedPreferences(
                SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(USER_REMEMBER_ME_SP, false);
    }

    public void saveUserInfo(String email, String password){
        SharedPreferences sp = context.getSharedPreferences(
                SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(USER_PREFERENCES_PASSWORD, password);
        editor.putString(USER_PREFERENCES_EMAIL, email);
        editor.apply();
    }

    public void saveUserGoogleIdToken(String idToken){
        SharedPreferences sp = context.getSharedPreferences(
                SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(USER_PREFERENCES_GOOGLE_ID_TOKEN, idToken);
        editor.putBoolean(USER_PREFERENCES_GOOGLE_ACCESS_METHOD, true);
        editor.apply();
    }

    public boolean getIsGoogleAccount(){
        SharedPreferences sp = context.getSharedPreferences(
                SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(USER_PREFERENCES_GOOGLE_ACCESS_METHOD, false);
    }

    public String getGoogleIdToken(){
        SharedPreferences sp = context.getSharedPreferences(
                SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sp.getString(USER_PREFERENCES_GOOGLE_ID_TOKEN, "");
    }

    public UserCredentials getCredentials(){
        String password, email;
        SharedPreferences sp = context.getSharedPreferences(
                SHARED_PREF_NAME, Context.MODE_PRIVATE);

        password = sp.getString(USER_PREFERENCES_PASSWORD, "");
        email = sp.getString(USER_PREFERENCES_EMAIL, "");

        return new UserCredentials(email, password);
    }

    public void setIsGoogleAccount(boolean isGoogleAccount) {
        SharedPreferences sp = context.getSharedPreferences(
                SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(USER_PREFERENCES_GOOGLE_ACCESS_METHOD, isGoogleAccount);
        editor.apply();
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
