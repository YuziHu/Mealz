package com.example.mealz.Repositories;

import android.util.Log;

import com.example.mealz.Activities.UserActivity;
import com.example.mealz.Models.IngredientModel;
import com.example.mealz.Models.MealPlanModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

/**
 * Use Singleton Pattern to avoid multiple open connection to database and/or web service (APIs)
 */
public class PendingMealplanRepo {

    private static final String TAG = "PendingMealplanRepo";
    
    private static PendingMealplanRepo instance;
    private List<MealPlanModel> pendingMealplans = new ArrayList<>();

    // firebase objects
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference current_user_db;
    private DatabaseReference curUserGroup;

    // Object Mapper
    ObjectMapper mapper = new ObjectMapper();

    public static PendingMealplanRepo getInstance(){
        if(instance == null){
            instance = new PendingMealplanRepo();
        }
        return instance;
    }
    public MutableLiveData<List<MealPlanModel>> getPendingMealPlans(){
        setPendingMealplans();

        MutableLiveData<List<MealPlanModel>> data = new MutableLiveData<>();
        data.setValue(pendingMealplans);
        return data;
    }

    private void setPendingMealplans(){
//        Log.d(TAG, "setPendingMealplans: "+currentUser.getUid());
        if(currentUser!=null){
            String currentUID = currentUser.getUid();
            current_user_db = database.getReference().child("Users").child(currentUID);
            if(UserActivity.groupID!=null){
                curUserGroup = database.getReference().child("Groups").child(UserActivity.groupID);
            }
            // if current user has pending meal plans
            DatabaseReference curUserCurrentPendingMealplans = curUserGroup.child("meal_plans").child("current").child("pending");
            if(curUserCurrentPendingMealplans!=null){
//                Log.d(TAG, "setPendingMealplans: "+currentUser.getUid());
                curUserCurrentPendingMealplans.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        Log.d(TAG, "onDataChange: "+dataSnapshot.getValue().toString());
                        pendingMealplans.clear();
//                        pendingImages.clear();
//                        pendingNames.clear();
//                        pendingIngredients.clear();
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            MealPlanModel mealplan = ds.getValue(MealPlanModel.class);
                            mealplan.setmId(ds.getKey());
                            List<IngredientModel> ingredients = mapper.convertValue(mealplan.getIngredients(), new TypeReference<List<IngredientModel>>(){});
                            mealplan.setIngredients(ingredients);
                            pendingMealplans.add(mealplan);
//                            pendingImages.add(mealplan.getImageUrl());
//                            pendingNames.add(mealplan.getName());
//                            pendingIngredients.add(ingredients);

                        }
                        Log.d(TAG, "onDataChange: "+pendingMealplans.toString());
//                        pendingAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
    }
}
