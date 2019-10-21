package com.example.mealz.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.mealz.Fragments.GrocerylistFragment;
import com.example.mealz.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener(){
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item){
                    Fragment selectedFragment = null;

                    switch (item.getItemId()){
                        case R.id.nav_todo:
                            selectedFragment = new GrocerylistFragment();
                            break;
                        case R.id.nav_mealplan:
                            selectedFragment = new GrocerylistFragment();
                            break;
                        case R.id.nav_grocery:
                            selectedFragment = new GrocerylistFragment();
                            break;
                        case R.id.nav_profile:
                            selectedFragment = new GrocerylistFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
            };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);

        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }

}
