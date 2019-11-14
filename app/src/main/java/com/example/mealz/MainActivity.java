package com.example.mealz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.mealz.Activities.LoginActivity;
import com.example.mealz.Activities.UserActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private String FCMtoken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // test firebase instance token /////////////////////////////////////////////////////////////
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if(task.isSuccessful()){
                            FCMtoken = task.getResult().getToken();
                            Log.d(TAG, "onCreate: Firebase message instance token:"+FCMtoken);

                        }else{
                            FCMtoken = " Error: "+task.getException().getMessage();
                            Log.d(TAG, "onCreate: Firebase message instance token:"+FCMtoken);

                        }
                    }
                });
        //////////////////////////////////////////////////////////////////////////////////////////////
        Intent loginActivity = new Intent(getApplicationContext(), UserActivity.class);
        startActivity(loginActivity);
        finish();
    }
}