package com.example.mealz.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.mealz.Dialogs.AddGroceryDialog;
import com.example.mealz.Fragments.GrocerylistFragment;
import com.example.mealz.Fragments.MealPlanFragment;
import com.example.mealz.Fragments.UserProfileFragment;
import com.example.mealz.Models.GroceryItem;
import com.example.mealz.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserActivity extends AppCompatActivity implements AddGroceryDialog.AddGroceryDialogListener {

    // firebase objects
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database;
    private DatabaseReference current_user_db;

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener(){
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item){
                    Fragment selectedFragment = null;

                    switch (item.getItemId()){
                        case R.id.nav_todo:
                            selectedFragment = new UserProfileFragment();
                            break;
                        case R.id.nav_mealplan:
                            selectedFragment = new MealPlanFragment();
                            break;
                        case R.id.nav_grocery:
                            selectedFragment = new GrocerylistFragment();
                            break;
                        case R.id.nav_profile:
                            selectedFragment = new UserProfileFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
            };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);

        bottomNav.setOnNavigationItemSelectedListener(navListener);

        // firebase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String currentUID = currentUser.getUid();
            current_user_db = database.getReference().child("Users").child(currentUID);
        }
    }

    @Override
    public void addGrocery(String groceryName, int amount, String unit) {
        // once user hits add, make a query to database to retrieve the grocery item by name (API??)
        // once got the data for the grocery item, save it in current user grocery list
        GroceryItem newGroceryEntry = new GroceryItem();
        newGroceryEntry.setName(groceryName);
        newGroceryEntry.setAmount(amount);
        newGroceryEntry.setUnit(unit);
        DatabaseReference currentUserGroceryList = current_user_db.child("grocery_list").child("personal");
        currentUserGroceryList.push().setValue(newGroceryEntry);
    }
}
