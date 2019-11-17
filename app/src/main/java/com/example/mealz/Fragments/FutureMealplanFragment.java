package com.example.mealz.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mealz.Activities.RecipeDetailActivity;
import com.example.mealz.Adapters.RecyclerMealplanAdapter;
import com.example.mealz.Models.IngredientModel;
import com.example.mealz.Models.MealPlanModel;
import com.example.mealz.Models.RecipeModel;
import com.example.mealz.R;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FutureMealplanFragment extends Fragment implements RecyclerMealplanAdapter.MealPlanClickListener, RecyclerMealplanAdapter.PendingMealplanButtonsListener {
    private static final String TAG = "FutureMealplanFragment";

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
    List<MealPlanModel> pendingMealPlans = new ArrayList<>();
    List<String> pendingImages = new ArrayList<>();
    List<String> pendingNames = new ArrayList<>();
    List<List<IngredientModel>> pendingIngredients = new ArrayList<>();

    // agreed
    List<MealPlanModel> agreedMealPlans = new ArrayList<>();
    List<String> agreedImages = new ArrayList<>();
    List<String> agreedNames = new ArrayList<>();
    List<List<IngredientModel>> agreedIngredients = new ArrayList<>();
    // personal
    List<MealPlanModel> personalMealPlans = new ArrayList<>();
    List<String> personalImages = new ArrayList<>();
    List<String> personalNames = new ArrayList<>();
    List<List<IngredientModel>> personalIngredients = new ArrayList<>();
    // Object Mapper
    ObjectMapper mapper = new ObjectMapper();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mealplan_recyclerview, container, false);

        pendingMealplanRecyclerView = view.findViewById(R.id.pendingMealplanRecyclerView);
        LinearLayoutManager pendingLayout = new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL, false);
        pendingLayout.setReverseLayout(true);
        pendingLayout.setStackFromEnd(true);
        pendingMealplanRecyclerView.setLayoutManager(pendingLayout);
        pendingMealplanRecyclerView.setNestedScrollingEnabled(false);
        //
        agreedMealplanRecyclerView = view.findViewById(R.id.agreedMealplanRecyclerView);
        agreedMealplanRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL, false));
        agreedMealplanRecyclerView.setNestedScrollingEnabled(false);
        //
        personalMealplanRecyclerView = view.findViewById(R.id.personalMealplanRecyclerView);
        personalMealplanRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL, false));
        personalMealplanRecyclerView.setNestedScrollingEnabled(false);
        // adapter
        // pending
        pendingAdapter = new RecyclerMealplanAdapter(this, this, "PENDING", getActivity(), pendingImages, pendingNames);
        pendingMealplanRecyclerView.setAdapter(pendingAdapter);
        // agreed
        agreedAdapter = new RecyclerMealplanAdapter(this, this, "AGREED", getActivity(), agreedImages, agreedNames);
        agreedMealplanRecyclerView.setAdapter(agreedAdapter);
        // personal
        personalAdapter = new RecyclerMealplanAdapter(this, this, "PERSONAL", getActivity(), personalImages, personalNames);
        personalMealplanRecyclerView.setAdapter(personalAdapter);

        // firebase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        currentUser = mAuth.getCurrentUser();

        if(currentUser!=null){
            String currentUID = currentUser.getUid();
            current_user_db = database.getReference().child("Users").child(currentUID);
            // if current user has pending meal plans
            DatabaseReference curUserFuturePendingMealplans = current_user_db.child("meal_plans").child("future").child("pending");
            if(curUserFuturePendingMealplans!=null){
                curUserFuturePendingMealplans.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        pendingMealPlans.clear();
                        pendingImages.clear();
                        pendingNames.clear();
                        pendingIngredients.clear();
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            MealPlanModel mealplan = ds.getValue(MealPlanModel.class);
                            mealplan.setmId(ds.getKey());
                            List<IngredientModel> ingredients = mapper.convertValue(mealplan.getIngredients(), new TypeReference<List<IngredientModel>>(){});
                            pendingMealPlans.add(mealplan);
                            pendingImages.add(mealplan.getImageUrl());
                            pendingNames.add(mealplan.getName());
                            pendingIngredients.add(ingredients);

                        }
                        pendingAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            // if current user has agreed meal plans
            DatabaseReference curUserFutureAgreedMealplans = current_user_db.child("meal_plans").child("future").child("agreed");
            if(curUserFutureAgreedMealplans!=null){
                curUserFutureAgreedMealplans.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        agreedMealPlans.clear();
                        agreedImages.clear();
                        agreedNames.clear();
                        agreedIngredients.clear();
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            MealPlanModel mealplan = ds.getValue(MealPlanModel.class);
                            mealplan.setmId(ds.getKey());
                            List<IngredientModel> ingredients = mapper.convertValue(mealplan.getIngredients(), new TypeReference<List<IngredientModel>>(){});
                            agreedMealPlans.add(mealplan);
                            agreedImages.add(mealplan.getImageUrl());
                            agreedNames.add(mealplan.getName());
                            agreedIngredients.add(ingredients);
                        }
                        agreedAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            // if current user has personal meal plans
            DatabaseReference curUserFuturePersonalMealplans = current_user_db.child("meal_plans").child("future").child("personal");
            if(curUserFuturePersonalMealplans!=null){
                curUserFuturePersonalMealplans.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        personalMealPlans.clear();
                        personalImages.clear();
                        personalNames.clear();
                        personalIngredients.clear();
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            MealPlanModel mealplan = ds.getValue(MealPlanModel.class);
                            List<IngredientModel> ingredients = mapper.convertValue(mealplan.getIngredients(), new TypeReference<List<IngredientModel>>(){});
                            Log.d(TAG, "onDataChange: "+ingredients);
                            mealplan.setmId(ds.getKey());
                            personalMealPlans.add(mealplan);
                            personalImages.add(mealplan.getImageUrl());
                            personalNames.add(mealplan.getName());
                            personalIngredients.add(ingredients);
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

    @Override
    public void onMealplanClick(String tag, int position) {
        Log.d(TAG, "onMealplanClick: "+tag);
        RecipeModel recipe = new RecipeModel();
        switch (tag){
            case "PENDING":
                recipe.setImage(pendingImages.get(position));
                recipe.setLabel(pendingNames.get(position));
                recipe.setIngredients(pendingIngredients.get(position));
                break;
            case "AGREED":
                recipe.setImage(agreedImages.get(position));
                recipe.setLabel(agreedNames.get(position));
                recipe.setIngredients(agreedIngredients.get(position));
                break;
            case "PERSONAL":
                recipe.setImage(personalImages.get(position));
                recipe.setLabel(personalNames.get(position));
                recipe.setIngredients(personalIngredients.get(position));
                break;
            default:
                break;
        }
        Intent intent = new Intent(getActivity(), RecipeDetailActivity.class);
        intent.putExtra("recipe",(Serializable)recipe);
        startActivity(intent);
    }

    @Override
    public void onLikeClick(String tag, int position) {

    }

    @Override
    public void onDislikeClick(String tag, int position) {

    }
}
