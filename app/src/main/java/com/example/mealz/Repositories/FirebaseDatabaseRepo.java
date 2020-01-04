package com.example.mealz.Repositories;

import android.util.Log;

import com.example.mealz.Activities.UserActivity;
import com.example.mealz.Mapper.FirebaseMapper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


public abstract class FirebaseDatabaseRepo<Model> {
    private static final String TAG = "FirebaseDatabaseRepo";

    protected DatabaseReference databaseReference;
    protected FirebaseDatabaseRepoCallback<Model> firebaseCallback;
    private BaseValueEventListener listener;
    private FirebaseMapper mapper;

    protected abstract List<String> getPath(String uid);

    public FirebaseDatabaseRepo(FirebaseMapper mapper){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        for(String path : getPath(UserActivity.currentUID)){
            databaseReference = databaseReference.child(path);
        }
        Log.d(TAG, "FirebaseDatabaseRepo: "+databaseReference);
//        databaseReference = FirebaseDatabase.getInstance().getReference(getPath());
        this.mapper = mapper;
    }

    public void addListener(FirebaseDatabaseRepoCallback<Model> callback){
        this.firebaseCallback = callback;
        listener = new BaseValueEventListener(mapper, firebaseCallback);
        databaseReference.addValueEventListener(listener);
    }

    public void removeListener(){
        databaseReference.removeEventListener(listener);
    }


    public interface FirebaseDatabaseRepoCallback<T> {
        void onSuccess(List<T> result);

        void onError(Exception e);
    }
}
