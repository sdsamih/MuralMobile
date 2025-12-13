package com.example.muralmobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.muralmobile.HomeFragment;
import com.example.muralmobile.ProfileFragment;
import com.example.muralmobile.R;
import com.example.muralmobile.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ProfileFragment profileFragment;
    private HomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SessionManager sessionManager = new SessionManager(this);


        if (!sessionManager.isLoggedIn()) {
            Toast.makeText(this, "Faça login", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        new Thread(() -> {
            if (sessionManager.isUserInvalid()) {
                runOnUiThread(() -> {
                    sessionManager.clearSession();
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                });
            }
        }).start();

        profileFragment = new ProfileFragment();
        homeFragment = new HomeFragment();


        setCurrentFragment(homeFragment);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                setCurrentFragment(new HomeFragment()); // sempre recarrega posts
                return true;
            }

            if (id == R.id.nav_post) {
                startActivity(new Intent(MainActivity.this, CreatePostActivity.class));
                return true;
            }

            if (id == R.id.nav_profile) {
                setCurrentFragment(new ProfileFragment());
                return true;
            }

            return false;
        });
    }

    private void setCurrentFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFragment, fragment)
                .commit();
    }
}
