package com.example.mealz.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mealz.Activities.HomeActivity;
import com.example.mealz.Models.User;
import com.example.mealz.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    private EditText userEmail, userPassword, userName;
//    private ProgressBar loadingProgress;
    private Button signupBtn;
    TextView signUp;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        userEmail = findViewById(R.id.edtEmail);
        userPassword = findViewById(R.id.edtPassword);
        userName = findViewById(R.id.edtUsername);
        signupBtn = findViewById(R.id.signupBtn);

        mFirebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

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
                            // save user info after signing up
                        }
                        else{
                            showMessage("Account creation failed!" + task.getException().getMessage());
                        }
                    }
                });
    }

    private void saveUserInfo(String name, FirebaseUser currentUser) {
        String uid = currentUser.getUid();
        DatabaseReference current_user_db = database.getReference().child("Users").child(uid);
        // create a new user object
        User newUser = new User(uid,currentUser.getDisplayName(),currentUser.getEmail(),null);
        // create a map that maps user info
        Map userInfo = new HashMap();
        System.out.println(newUser.getName());
        userInfo.put("username",name);
        userInfo.put("email",newUser.getEmail());
        System.out.println(newUser.getGroceryList());
        userInfo.put("groceryList",newUser.getGroceryList());
        current_user_db.setValue(userInfo);
        updateUI();

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
                        }
                    }
                });
        // save user info
        saveUserInfo(name, mFirebaseAuth.getCurrentUser());
    }

    private void updateUI() {
        Intent homeActivity = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(homeActivity);
        finish();
    }

    // simple method to show toast message
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
