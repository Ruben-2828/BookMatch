package com.example.bookmatch.utils;

import static com.example.bookmatch.utils.Constants.SHARED_PREF_NAME;
import static com.example.bookmatch.utils.Constants.USER_PREFERENCES_EMAIL;
import static com.example.bookmatch.utils.Constants.USER_PREFERENCES_GOOGLE_ACCESS_METHOD;
import static com.example.bookmatch.utils.Constants.USER_PREFERENCES_GOOGLE_ID_TOKEN;
import static com.example.bookmatch.utils.Constants.USER_PREFERENCES_PASSWORD;
import static com.example.bookmatch.utils.Constants.USER_REMEMBER_ME_SP;

import android.content.Context;

public class AccountManager {
    SharedPreferencesUtil sp;

    public AccountManager(Context context){
        this.sp = new SharedPreferencesUtil(context);
    }
    public void setRememberMe(Boolean rememberMe) {
        sp.writeBooleanDataSharedPreferences(SHARED_PREF_NAME, USER_REMEMBER_ME_SP, rememberMe);
    }

    public boolean getRememberMe(){
        return sp.readBooleanDataSharedPreferences(SHARED_PREF_NAME, USER_REMEMBER_ME_SP);
    }

    public void saveUserInfo(String email, String password){
        sp.writeStringDataSharedPreferences(SHARED_PREF_NAME, USER_PREFERENCES_PASSWORD, password);
        sp.writeStringDataSharedPreferences(SHARED_PREF_NAME, USER_PREFERENCES_EMAIL, email);
    }

    public void saveUserGoogleIdToken(String idToken){
        sp.writeStringDataSharedPreferences(SHARED_PREF_NAME, USER_PREFERENCES_GOOGLE_ID_TOKEN, idToken);
        setIsGoogleAccount(true);
    }

    public boolean getIsGoogleAccount(){
        return sp.readBooleanDataSharedPreferences(SHARED_PREF_NAME, USER_PREFERENCES_GOOGLE_ACCESS_METHOD);
    }

    public String getGoogleIdToken(){
        return sp.readStringDataSharedPreferences(SHARED_PREF_NAME, USER_PREFERENCES_GOOGLE_ID_TOKEN);
    }

    public UserCredentials getCredentials(){
        String password, email;

        password = sp.readStringDataSharedPreferences(SHARED_PREF_NAME, USER_PREFERENCES_PASSWORD);
        email = sp.readStringDataSharedPreferences(SHARED_PREF_NAME, USER_PREFERENCES_EMAIL);

        return new UserCredentials(email, password);
    }

    public void setIsGoogleAccount(boolean isGoogleAccount) {
        sp.writeBooleanDataSharedPreferences(SHARED_PREF_NAME, USER_PREFERENCES_GOOGLE_ACCESS_METHOD, isGoogleAccount);
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
