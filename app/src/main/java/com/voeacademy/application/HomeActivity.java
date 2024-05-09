package com.voeacademy.application;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.voeacademy.application.R;
import com.voeacademy.application.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    BottomNavigationView bottomNavigationView;

    HomeMenuFragment homeMenuFragment = new HomeMenuFragment();
    NotificationMenuFragment notificationMenuFragment = new NotificationMenuFragment();
    AccountMenuFragment accountMenuFragment = new AccountMenuFragment();
    HelpFragment helpFragment = new HelpFragment();

    FirebaseFirestore voe_db_firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        voe_db_firestore=FirebaseFirestore.getInstance();

//        checkValidUser(signedInAccount.getId());

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, homeMenuFragment).commit();

        if (getIntent().hasExtra("NotificationMenuFragment")) {
            String fragmentTag = getIntent().getStringExtra("NotificationMenuFragment");

            // Launch the desired fragment
            if (fragmentTag.equals("NotificationMenuFragment")) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, notificationMenuFragment);
                fragmentTransaction.commit();
            }
        }

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_menu:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, homeMenuFragment).commit();
                        return true;
                    case R.id.notification_menu:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, notificationMenuFragment).commit();
                        return true;
                    case R.id.account_menu:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, accountMenuFragment).commit();
                        return true;
                    case R.id.help_menu:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, helpFragment).commit();
                        return true;

                }

                return false;
            }
        });


    }



}