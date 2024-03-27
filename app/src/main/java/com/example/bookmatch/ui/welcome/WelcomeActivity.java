package com.example.bookmatch.ui.welcome;

import static com.example.bookmatch.utils.Constants.SHARED_PREF_NAME;
import static com.example.bookmatch.utils.Constants.USER_REMEMBER_ME_SP;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookmatch.R;
import com.example.bookmatch.ui.main.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * Activity that contains Fragments to allow user to login or to create an account.
 */
public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        if (!sp.getBoolean(USER_REMEMBER_ME_SP, false))
            mAuth.signOut();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            Log.d("CIAO", "utente loggato");
            currentUser.reload();

            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.activity_welcome);
            // NavHostFragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            // NavController navController = navHostFragment.getNavController();
        }

    }


}
