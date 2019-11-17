package com.example.mealz.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mealz.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText userEmail, userPassword;
    private Button loginBtn;
    private Button toSignupBtn;
    private FirebaseAuth mAuth;
    private Intent homeActivity, signupActivity, userActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userEmail = findViewById(R.id.loginEmail);
        userPassword = findViewById(R.id.loginPassword);
        loginBtn = findViewById(R.id.loginBtn);
        toSignupBtn = findViewById(R.id.toSignUpBtn);
        mAuth = FirebaseAuth.getInstance();
//        homeActivity = new Intent(this, UserActivity.class);
        signupActivity = new Intent(this, SignupActivity.class);
        userActivity = new Intent(this, UserActivity.class);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = userEmail.getText().toString();
                final String password = userPassword.getText().toString();

                if(email.isEmpty()){
                    showMessage("Please enter your email");
                }
                else if(password.isEmpty()){
                    showMessage("Please enter your password.");
                }
                else{
                    login(email, password);
                }
            }
        });
        // to signup page
        toSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(signupActivity);
                finish();
            }
        });
    }

    private void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    updateUI();
                }
                else{
                    showMessage(task.getException().getMessage());
                }
            }
        });
    }

    private void updateUI() {
        startActivity(userActivity);
        finish();
    }

    // simple method to show toast message
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();

        if(user!=null){
            // user is already connected, redirect him to his home page
            System.out.println("cur user uid: "+user.getUid());
            updateUI();
        }
    }
}
