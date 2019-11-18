package com.example.mealz.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mealz.Activities.LoginActivity;
import com.example.mealz.Models.GroceryItem;
import com.example.mealz.Models.MealPlanModel;
import com.example.mealz.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class UserProfileFragment extends Fragment {

    // firebase objects
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database;
    private DatabaseReference current_user_db;

    //List<GroceryItem> groceryList = new ArrayList<>();
    ArrayList<MealPlanModel> agreedMealPlans = new ArrayList<>();
    ArrayList<MealPlanModel> personalMealPlans = new ArrayList<>();

    // sign out user
    private Button signout;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private TextView nameText;
    private TextView groceryItems;
    private TextView agreedMeals;
    private TextView personalMeals;
    //private TextView numMeals;
    //private TextView numItems;
    private TextView roommateName;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        nameText = view.findViewById(R.id.nameText);
        nameText.setText("");

        agreedMeals = view.findViewById(R.id.agreedMeals);
        agreedMeals.setText("");

        personalMeals = view.findViewById(R.id.personalMeals);
        personalMeals.setText("");

        roommateName = view.findViewById(R.id.roommateName);
        //roommateName.setText("");


        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String currentUID = currentUser.getUid();
            current_user_db = database.getReference().child("Users").child(currentUID);

            current_user_db.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Get user value
                            String name = dataSnapshot.child("username").getValue(String.class);
                            nameText.setText(name);
                            System.out.println(name);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

            /*DatabaseReference currentUserGroceryList = current_user_db.child("grocery_list").child("personal");
            if (currentUserGroceryList != null) {
                currentUserGroceryList.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        groceryList.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            GroceryItem item = ds.getValue(GroceryItem.class);
                            groceryList.add(item);
                        }
                        System.out.println(groceryList.size());
                        String size = "" + groceryList.size();
                        groceryItems.setText(size);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }*/

            DatabaseReference curUserFutureAgreedMealplans = current_user_db.child("meal_plans").child("current").child("agreed");
            if(curUserFutureAgreedMealplans!=null){
                curUserFutureAgreedMealplans.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        agreedMealPlans.clear();
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            MealPlanModel mealplan = ds.getValue(MealPlanModel.class);
                            mealplan.setmId(ds.getKey());
                            agreedMealPlans.add(mealplan);
                        }
                        System.out.println(agreedMealPlans.size());
                        String size = "" + agreedMealPlans.size();
                        agreedMeals.setText(size);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            DatabaseReference curUserFuturePersonalMealplans = current_user_db.child("meal_plans").child("current").child("personal");
            if(curUserFuturePersonalMealplans!=null){
                curUserFuturePersonalMealplans.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        personalMealPlans.clear();
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            MealPlanModel mealplan = ds.getValue(MealPlanModel.class);
                            mealplan.setmId(ds.getKey());
                            personalMealPlans.add(mealplan);
                        }
                        System.out.println(personalMealPlans.size());
                        String size = "" + personalMealPlans.size();
                        personalMeals.setText(size);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

//            current_user_db.child("grocery_list").child("personal");
//            if (currentUserGroceryList != null) {
//                currentUserGroceryList.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        groceryList.clear();
//                        groceryNames.clear();
//                        groceryAmount.clear();
//                        groceryUnits.clear();
//                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                            GroceryItem item = ds.getValue(GroceryItem.class);
//                            item.setGid(ds.getKey());
//                            groceryList.add(item);
//                            groceryNames.add(item.getName());
//                            groceryAmount.add(item.getAmount());
//                            groceryUnits.add(item.getUnit());
//                        }
//                        rAdapter.notifyDataSetChanged();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//            }
        }


//      signout

        signout = view.findViewById(R.id.signoutBtn);
        setUpFirebaseListener();
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
            }
        });

        return view;
    }
//
    private void setUpFirebaseListener() {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {

                } else {
                    Toast.makeText(getActivity(), "Signed out", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthStateListener);
        }
    }

}