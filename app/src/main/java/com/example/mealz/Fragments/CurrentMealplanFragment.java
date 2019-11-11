package com.example.mealz.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mealz.Adapters.RecyclerMealplanAdapter;
import com.example.mealz.Models.MealPlanModel;
import com.example.mealz.R;
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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CurrentMealplanFragment extends Fragment {
    private static final String TAG = "CurrentMealplanFragment";

    private RecyclerView pendingMealplanRecyclerView;
    private RecyclerView agreedMealplanRecyclerView;
    private RecyclerView personalMealplanRecyclerView;
    RecyclerMealplanAdapter pendingAdapter;
    RecyclerMealplanAdapter agreedAdapter;
    RecyclerMealplanAdapter personalAdapter;

    // firebase objects
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database;
    private DatabaseReference current_user_db;

    // get meal plans as a list from firebase
    // pending
    ArrayList<MealPlanModel> pendingMealPlans = new ArrayList<>();
    ArrayList<String> pendingImages = new ArrayList<>();
    ArrayList<String> pendingNames = new ArrayList<>();
    // agreed
    ArrayList<MealPlanModel> agreedMealPlans = new ArrayList<>();
    ArrayList<String> agreedImages = new ArrayList<>();
    ArrayList<String> agreedNames = new ArrayList<>();
    // personal
    ArrayList<MealPlanModel> personalMealPlans = new ArrayList<>();
    ArrayList<String> personalImages = new ArrayList<>();
    ArrayList<String> personalNames = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mealplan_recyclerview, container, false);

        pendingMealplanRecyclerView = view.findViewById(R.id.pendingMealplanRecyclerView);
        pendingMealplanRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL, false));
        //
        agreedMealplanRecyclerView = view.findViewById(R.id.agreedMealplanRecyclerView);
        agreedMealplanRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL, false));
        //
        personalMealplanRecyclerView = view.findViewById(R.id.personalMealplanRecyclerView);
        personalMealplanRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL, false));
        // adapter
        // pending
        pendingAdapter = new RecyclerMealplanAdapter(getActivity(), pendingImages, pendingNames);
        pendingMealplanRecyclerView.setAdapter(pendingAdapter);
        // agreed
        agreedAdapter = new RecyclerMealplanAdapter(getActivity(), agreedImages, agreedNames);
        agreedMealplanRecyclerView.setAdapter(agreedAdapter);
        // personal
        personalAdapter = new RecyclerMealplanAdapter(getActivity(), personalImages, personalNames);
        personalMealplanRecyclerView.setAdapter(personalAdapter);

        // firebase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        currentUser = mAuth.getCurrentUser();

        if(currentUser!=null){
            String currentUID = currentUser.getUid();
            current_user_db = database.getReference().child("Users").child(currentUID);
            // if current user has pending meal plans
            DatabaseReference curUserFuturePendingMealplans = current_user_db.child("meal_plans").child("current").child("pending");
            if(curUserFuturePendingMealplans!=null){
                curUserFuturePendingMealplans.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        pendingMealPlans.clear();
                        pendingImages.clear();
                        pendingNames.clear();
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            MealPlanModel mealplan = ds.getValue(MealPlanModel.class);
                            mealplan.setmId(ds.getKey());
                            pendingMealPlans.add(mealplan);
                            pendingImages.add(mealplan.getImageUrl());
                            pendingNames.add(mealplan.getName());
                        }
                        pendingAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            // if current user has agreed meal plans
            DatabaseReference curUserFutureAgreedMealplans = current_user_db.child("meal_plans").child("current").child("agreed");
            if(curUserFutureAgreedMealplans!=null){
                curUserFutureAgreedMealplans.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        agreedMealPlans.clear();
                        agreedImages.clear();
                        agreedNames.clear();
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            MealPlanModel mealplan = ds.getValue(MealPlanModel.class);
                            mealplan.setmId(ds.getKey());
                            agreedMealPlans.add(mealplan);
                            agreedImages.add(mealplan.getImageUrl());
                            agreedNames.add(mealplan.getName());
                        }
                        agreedAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            // if current user has personal meal plans
            DatabaseReference curUserFuturePersonalMealplans = current_user_db.child("meal_plans").child("current").child("personal");
            if(curUserFuturePersonalMealplans!=null){
                curUserFuturePersonalMealplans.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        personalMealPlans.clear();
                        personalImages.clear();
                        personalNames.clear();
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            MealPlanModel mealplan = ds.getValue(MealPlanModel.class);
                            mealplan.setmId(ds.getKey());
                            personalMealPlans.add(mealplan);
                            personalImages.add(mealplan.getImageUrl());
                            personalNames.add(mealplan.getName());
                        }
                        personalAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
        return view;
    }
}
