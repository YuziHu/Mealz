package com.example.mealz.Repositories;

import com.example.mealz.Mapper.FirebaseMapper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class BaseValueEventListener<Model, Entity> implements ValueEventListener {
    private FirebaseMapper<Entity, Model> mapper;
    private FirebaseDatabaseRepo.FirebaseDatabaseRepoCallback<Model> callback;

    public BaseValueEventListener(FirebaseMapper<Entity, Model> mapper,
                                  FirebaseDatabaseRepo.FirebaseDatabaseRepoCallback<Model> callback){
        this.mapper = mapper;
        this.callback = callback;
    }

    @Override
    public void onDataChange(DataSnapshot ds){
        List<Model> data = mapper.mapList(ds);
        callback.onSuccess(data);
    }

    @Override
    public void onCancelled(DatabaseError de){
        callback.onError(de.toException());
    }

}
