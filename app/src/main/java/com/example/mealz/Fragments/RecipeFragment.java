package com.example.mealz.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.mealz.Activities.UserActivity;
import com.example.mealz.Adapters.RecyclerMealplanAdapter;
import com.example.mealz.Models.IngredientModel;
import com.example.mealz.Models.MealPlanModel;
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

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecipeFragment extends Fragment implements RecyclerMealplanAdapter.MealPlanClickListener {

    private static final String TAG = "RecipeFragment";

    private ImageButton searchBtn;
    private EditText searchField;
    //
    private RecyclerView commonLikeRecyclerView;
    private RecyclerView roommateLikeRecyclerView;
    private RecyclerView recommendRecyclerView;
    RecyclerMealplanAdapter commonLikeAdapter;
    RecyclerMealplanAdapter roommateLikeAdapter;
    RecyclerMealplanAdapter recommendAdapter;
    // firebase objects
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database;
    private DatabaseReference current_user_db;
    private DatabaseReference curUserGroup;
    //
    List<MealPlanModel> commonLikeList = new ArrayList<>();
    List<String> commonLikeImages = new ArrayList<>();
    List<String> commonLikeNames = new ArrayList<>();
    List<List<IngredientModel>> commonLikeIngredients = new ArrayList<>();
    //
    List<MealPlanModel> roommateLikeList = new ArrayList<>();
    List<String> roommateLikeImages = new ArrayList<>();
    List<String> roommateLikeNames = new ArrayList<>();
    List<List<IngredientModel>> roommateLikeIngredients = new ArrayList<>();
    //
    List<MealPlanModel> recommendList = new ArrayList<>();
    List<String> recommendImages = new ArrayList<>();
    List<String> recommendNames = new ArrayList<>();
    List<List<IngredientModel>> recommendIngredients = new ArrayList<>();
    //
    ObjectMapper mapper = new ObjectMapper();
    //
    List<String> members = new ArrayList<>();
    String roommateID = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_recipe_main, container, false);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        currentUser = mAuth.getCurrentUser();

        recommendImages.add("https://www.edamam.com/web-img/41d/41d94cac765e8081f0d05d6725593385.jpg");
        recommendNames.add("Dagwood Sandwich");
        //
        recommendImages.add("https://www.edamam.com/web-img/610/6109e1318e6dc7a02b0f86b398a1485f.jpg");
        recommendNames.add("Tuna Sashimi");

        if (UserActivity.groupID != null) {
            Log.i(TAG, "onCreate: user group id " + UserActivity.groupID);
            // get list of members
            DatabaseReference curUserGroup = database.getReference().child("Groups").child(UserActivity.groupID).child("members");
            curUserGroup.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                        Log.i(TAG, "onDataChange: "+ds.getKey());
                        members.add(ds.getKey());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        searchBtn = view.findViewById(R.id.searchButton3);
        searchField = view.findViewById(R.id.recipeSearch2);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new RecipeSearchFragment()).commit();
            }
        });

        searchField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new RecipeSearchFragment()).commit();
            }
        });

        // recycler views
        commonLikeRecyclerView = view.findViewById(R.id.commonLikeRecyclerview);
        commonLikeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        commonLikeRecyclerView.setNestedScrollingEnabled(true);
        //
        roommateLikeRecyclerView = view.findViewById(R.id.roommateLikeRecyclerview);
        roommateLikeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        roommateLikeRecyclerView.setNestedScrollingEnabled(true);
        //
        recommendRecyclerView = view.findViewById(R.id.recommendRecyclerview);
        recommendRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        recommendRecyclerView.setNestedScrollingEnabled(true);
        // adapters
        commonLikeAdapter = new RecyclerMealplanAdapter(this, null, "", getActivity(), commonLikeImages, commonLikeNames);
        commonLikeRecyclerView.setAdapter(commonLikeAdapter);
        //
        roommateLikeAdapter = new RecyclerMealplanAdapter(this, null, "", getActivity(), roommateLikeImages, roommateLikeNames);
        roommateLikeRecyclerView.setAdapter(roommateLikeAdapter);
        //
        recommendAdapter = new RecyclerMealplanAdapter(this, null, "", getActivity(), recommendImages, recommendNames);
        recommendRecyclerView.setAdapter(recommendAdapter);
        recommendAdapter.notifyDataSetChanged();

        // firebase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (UserActivity.groupID != null) {
            curUserGroup = database.getReference().child("Groups").child(UserActivity.groupID);
            DatabaseReference curUserCommonLikeMealplans = curUserGroup.child("meal_plans").child("common_like");
            if (curUserCommonLikeMealplans != null) {
                curUserCommonLikeMealplans.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        commonLikeList.clear();
                        commonLikeImages.clear();
                        commonLikeNames.clear();
                        commonLikeIngredients.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            MealPlanModel mealplan = ds.getValue(MealPlanModel.class);
                            mealplan.setmId(ds.getKey());
                            List<IngredientModel> ingredients = mapper.convertValue(mealplan.getIngredients(), new TypeReference<List<IngredientModel>>() {
                            });
                            commonLikeList.add(mealplan);
                            commonLikeImages.add(mealplan.getImageUrl());
                            commonLikeNames.add(mealplan.getName());
                            commonLikeIngredients.add(ingredients);
                        }
                        commonLikeAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            Log.i(TAG, "onCreateView: " + UserActivity.groupID);
            // get list of members
            DatabaseReference curUserGroup = database.getReference().child("Groups").child(UserActivity.groupID).child("members");
            curUserGroup.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Log.i(TAG, "onDataChange: " + ds.getKey());
                        members.add(ds.getKey());
                        if (!(ds.getKey()).equals(currentUser.getUid())) {
                            roommateID = ds.getKey();
                            Log.i(TAG, "onDataChange: " + ds.getKey());
                            DatabaseReference roommatePersonal = database.getReference().child("Users").child(roommateID).child("meal_plans").child("current").child("personal");
                            roommatePersonal.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    roommateLikeList.clear();
                                    roommateLikeImages.clear();
                                    roommateLikeNames.clear();
                                    roommateLikeIngredients.clear();
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        MealPlanModel mealplan = ds.getValue(MealPlanModel.class);
                                        mealplan.setmId(ds.getKey());
                                        List<IngredientModel> ingredients = mapper.convertValue(mealplan.getIngredients(), new TypeReference<List<IngredientModel>>() {
                                        });
                                        roommateLikeList.add(mealplan);
                                        roommateLikeImages.add(mealplan.getImageUrl());
                                        roommateLikeNames.add(mealplan.getName());
                                        roommateLikeIngredients.add(ingredients);
                                    }
                                    roommateLikeAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });
            Log.i(TAG, "onCreateView: " + roommateID);
        }

        return view;
    }

    @Override
    public void onMealplanClick(String tag, int position) {
        Log.d(TAG, "onMealplanClick: ");
    }
}
