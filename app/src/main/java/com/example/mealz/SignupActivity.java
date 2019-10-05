package com.example.mealz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignupActivity extends AppCompatActivity {
    private EditText userEmail, userPassword, userName;
//    private ProgressBar loadingProgress;
    private Button signupBtn;
    TextView signUp;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        userEmail = findViewById(R.id.edtEmail);
        userPassword = findViewById(R.id.edtPassword);
        userName = findViewById(R.id.edtUsername);
        signupBtn = findViewById(R.id.signupBtn);

        mFirebaseAuth = FirebaseAuth.getInstance();

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String email = userEmail.getText().toString();
                final String password = userPassword.getText().toString();
                final String name = userName.getText().toString();

                if(email.isEmpty() || name.isEmpty() || password.isEmpty()){
                    // display error message
                    showMessage("Please Complete All Fields");

                }
                else{
                    // CreateUserAccount will try to create the user if the email is valid
                    CreateUserAccount(email,name,password);
                }
            }
        });


    }

    private void CreateUserAccount(String email, final String name, String password) {
        mFirebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            // user account create successfully
                            showMessage("Account created!");
                            // after we created user account, we need to update his name
                            updateUserInfo(name, mFirebaseAuth.getCurrentUser());
                        }
                        else{
                            showMessage("Account creation failed!" + task.getException().getMessage());
                        }
                    }
                });
    }

    private void updateUserInfo(String name, FirebaseUser currentUser) {
//        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child()
        UserProfileChangeRequest profileUpate = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();
        currentUser.updateProfile(profileUpate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            showMessage("Thank you signing up!");
                            updateUI();
                        }

                    }
                });
    }

    private void updateUI() {
        Intent homeActivity = new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(homeActivity);
        finish();

    }

    // simple method to show toast message
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
