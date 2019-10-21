package com.example.mealz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.mealz.Activities.LoginActivity;
import com.example.mealz.Activities.UserActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent loginActivity = new Intent(getApplicationContext(), UserActivity.class);
        startActivity(loginActivity);
        finish();
    }
}