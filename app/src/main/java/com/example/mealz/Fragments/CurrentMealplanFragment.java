package com.example.mealz.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mealz.Activities.RecipeDetailActivity;
import com.example.mealz.Activities.UserActivity;
import com.example.mealz.Adapters.RecyclerMealplanAdapter;
import com.example.mealz.Models.IngredientModel;
import com.example.mealz.Models.MealPlanModel;
import com.example.mealz.Models.RecipeModel;
import com.example.mealz.R;
import com.example.mealz.ViewModels.MealplanVMs.PendingMealplanViewModel;
import com.example.mealz.ViewModels.MealplanViewModel;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CurrentMealplanFragment extends Fragment implements RecyclerMealplanAdapter.MealPlanClickListener, RecyclerMealplanAdapter.PendingMealplanButtonsListener {
    private static final String TAG = "CurrentMealplanFragment";

    private RecyclerView pendingMealplanRecyclerView;
    private RecyclerView agreedMealplanRecyclerView;
    private RecyclerView personalMealplanRecyclerView;
    RecyclerMealplanAdapter pendingAdapter;
    RecyclerMealplanAdapter agreedAdapter;
    RecyclerMealplanAdapter personalAdapter;

    // View Models
    private MealplanViewModel mPendingMealplanViewModel;

    // firebase objects
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database;
    private DatabaseReference current_user_db;
    private DatabaseReference curUserGroup;

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
        LinearLayoutManager pendingLayout = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, true);
        pendingLayout.setStackFromEnd(true);
        pendingMealplanRecyclerView.setLayoutManager(pendingLayout);
        pendingMealplanRecyclerView.setNestedScrollingEnabled(true);
        //
        agreedMealplanRecyclerView = view.findViewById(R.id.agreedMealplanRecyclerView);
        agreedMealplanRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL, false));
        agreedMealplanRecyclerView.setNestedScrollingEnabled(false);
        //
        personalMealplanRecyclerView = view.findViewById(R.id.personalMealplanRecyclerView);
        personalMealplanRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL, false));
        personalMealplanRecyclerView.setNestedScrollingEnabled(false);
        // adapter
//        // pending
//        pendingAdapter = new RecyclerMealplanAdapter(this, this,"PENDING", getActivity(), pendingImages, pendingNames);
//        pendingMealplanRecyclerView.setAdapter(pendingAdapter);
        // agreed
        agreedAdapter = new RecyclerMealplanAdapter(this, this, "AGREED", getActivity(), agreedImages, agreedNames);
        agreedMealplanRecyclerView.setAdapter(agreedAdapter);
        // personal
        personalAdapter = new RecyclerMealplanAdapter(this, this, "PERSONAL", getActivity(), personalImages, personalNames);
        personalMealplanRecyclerView.setAdapter(personalAdapter);

        // View Models
        mPendingMealplanViewModel = ViewModelProviders.of(this).get(MealplanViewModel.class);
        // observe changes
        mPendingMealplanViewModel.getMealplans().observe(this, new Observer<List<MealPlanModel>>() {
            @Override
            public void onChanged(List<MealPlanModel> mealPlanModels) {
                Log.d(TAG, "onChanged: "+mealPlanModels);
                pendingAdapter.notifyDataSetChanged(mealPlanModels);
                pendingAdapter.notifyDataSetChanged();
            }
        });

        // pending
        pendingAdapter = new RecyclerMealplanAdapter(this, this,"PENDING", getActivity(), mPendingMealplanViewModel.getMealplans().getValue());
        pendingMealplanRecyclerView.setAdapter(pendingAdapter);

        // firebase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        currentUser = mAuth.getCurrentUser();

        if(currentUser!=null){
            String currentUID = currentUser.getUid();
            current_user_db = database.getReference().child("Users").child(currentUID);
            if(UserActivity.groupID!=null){
                curUserGroup = database.getReference().child("Groups").child(UserActivity.groupID);
            }
            // if current user has pending meal plans
//            DatabaseReference curUserCurrentPendingMealplans = curUserGroup.child("meal_plans").child("current").child("pending");
//            if(curUserCurrentPendingMealplans!=null){
//                curUserCurrentPendingMealplans.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        pendingMealPlans.clear();
//                        pendingImages.clear();
//                        pendingNames.clear();
//                        pendingIngredients.clear();
//                        for(DataSnapshot ds : dataSnapshot.getChildren()){
//                            MealPlanModel mealplan = ds.getValue(MealPlanModel.class);
//                            mealplan.setmId(ds.getKey());
//                            List<IngredientModel> ingredients = mapper.convertValue(mealplan.getIngredients(), new TypeReference<List<IngredientModel>>(){});
//                            pendingMealPlans.add(mealplan);
//                            pendingImages.add(mealplan.getImageUrl());
//                            pendingNames.add(mealplan.getName());
//                            pendingIngredients.add(ingredients);
//
//                        }
//                        pendingAdapter.notifyDataSetChanged();
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//            }
            // if current user has agreed meal plans
            if(UserActivity.groupID!=null){
                curUserGroup = database.getReference().child("Groups").child(UserActivity.groupID);
            }
            // if current user has pending meal plans
            DatabaseReference curUserCurrentAgreedMealplans = curUserGroup.child("meal_plans").child("current").child("agreed");
            if(curUserCurrentAgreedMealplans!=null){
                curUserCurrentAgreedMealplans.addValueEventListener(new ValueEventListener() {
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
            DatabaseReference curUserCurrentPersonalMealplans = current_user_db.child("meal_plans").child("current").child("personal");
            if(curUserCurrentPersonalMealplans!=null){
                curUserCurrentPersonalMealplans.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        personalMealPlans.clear();
                        personalImages.clear();
                        personalNames.clear();
                        personalIngredients.clear();
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            MealPlanModel mealplan = ds.getValue(MealPlanModel.class);
                            List<IngredientModel> ingredients = mapper.convertValue(mealplan.getIngredients(), new TypeReference<List<IngredientModel>>(){});
//                            Log.d(TAG, "onDataChange: "+ingredients);
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

    private void initRecyclerView(){}

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
        final RecipeModel recipe = new RecipeModel();
        recipe.setImage(pendingImages.get(position));
        recipe.setLabel(pendingNames.get(position));
        recipe.setIngredients((pendingIngredients.get(position)));
        if (currentUser != null) {
            String currentUID = currentUser.getUid();
            current_user_db = database.getReference().child("Users").child(currentUID);
            MealPlanModel newMealPlanEntry = new MealPlanModel();
            newMealPlanEntry.setName(recipe.getLabel());
            newMealPlanEntry.setImageUrl(recipe.getImage());
            newMealPlanEntry.setIngredients(recipe.getIngredients());
            DatabaseReference currUserMealPlansAgreed = curUserGroup.child("meal_plans").child("current").child("agreed");
            currUserMealPlansAgreed.push().setValue(newMealPlanEntry);

            DatabaseReference curUserFuturePendingMealplans = curUserGroup.child("meal_plans").child("current").child("pending");
            if (curUserFuturePendingMealplans != null) {
                curUserFuturePendingMealplans.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        pendingMealPlans.clear();
                        pendingImages.clear();
                        pendingNames.clear();
                        pendingIngredients.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            MealPlanModel mealplan = ds.getValue(MealPlanModel.class);

                            if (mealplan.getName() == recipe.getLabel()) {
                                ds.getRef().removeValue();
                            } else {
                                mealplan.setmId(ds.getKey());
                                List<IngredientModel> ingredients = mapper.convertValue(mealplan.getIngredients(), new TypeReference<List<IngredientModel>>() {
                                });
                                pendingMealPlans.add(mealplan);
                                pendingImages.add(mealplan.getImageUrl());
                                pendingNames.add(mealplan.getName());
                                pendingIngredients.add(ingredients);
                            }
                        }
                        pendingAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
        Log.d(TAG, "onLikeClick: called");
        // sending to agreed meals
    }

    @Override
    public void onDislikeClick(String tag, int position) {
        final RecipeModel recipe = new RecipeModel();
        recipe.setImage(pendingImages.get(position));
        recipe.setLabel(pendingNames.get(position));
        recipe.setIngredients((pendingIngredients.get(position)));
        if (currentUser != null) {
            String currentUID = currentUser.getUid();
            current_user_db = database.getReference().child("Users").child(currentUID);
            MealPlanModel newMealPlanEntry = new MealPlanModel();
            newMealPlanEntry.setName(recipe.getLabel());
            newMealPlanEntry.setImageUrl(recipe.getImage());
            newMealPlanEntry.setIngredients(recipe.getIngredients());
            DatabaseReference currUserMealPlansPersonal = current_user_db.child("meal_plans").child("current").child("personal");
            currUserMealPlansPersonal.push().setValue(newMealPlanEntry);
            DatabaseReference curUserFuturePendingMealplans = curUserGroup.child("meal_plans").child("current").child("pending");
            if (curUserFuturePendingMealplans != null) {
                curUserFuturePendingMealplans.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        pendingMealPlans.clear();
                        pendingImages.clear();
                        pendingNames.clear();
                        pendingIngredients.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            MealPlanModel mealplan = ds.getValue(MealPlanModel.class);
                            if (mealplan.getName() == recipe.getLabel()) {
                                ds.getRef().removeValue();
                            } else {
                                mealplan.setmId(ds.getKey());
                                List<IngredientModel> ingredients = mapper.convertValue(mealplan.getIngredients(), new TypeReference<List<IngredientModel>>() {
                                });
                                pendingMealPlans.add(mealplan);
                                pendingImages.add(mealplan.getImageUrl());
                                pendingNames.add(mealplan.getName());
                                pendingIngredients.add(ingredients);
                            }
                        }
                        pendingAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }

        Log.d(TAG, "onDislikeClick: called");
        // sending to personal meals
    }
}
