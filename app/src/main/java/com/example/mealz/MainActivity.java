package com.example.mealz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent signupActivity = new Intent(getApplicationContext(),SignupActivity.class);
        startActivity(signupActivity);
        finish();
    }
}