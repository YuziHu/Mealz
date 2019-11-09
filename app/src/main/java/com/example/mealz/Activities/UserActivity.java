package com.example.mealz.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.mealz.Dialogs.AddGroceryDialog;
import com.example.mealz.Dialogs.EditGroceryDialog;
import com.example.mealz.Fragments.GrocerylistFragment;
import com.example.mealz.Fragments.MealPlanFragment;
import com.example.mealz.Fragments.PersonalGrocerylistFragment;
import com.example.mealz.Fragments.RecipeDetailFragment;
import com.example.mealz.Fragments.RecipeFragment;
import com.example.mealz.Fragments.UserProfileFragment;
import com.example.mealz.Models.GroceryItem;
import com.example.mealz.Models.RecipeModel;
import com.example.mealz.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserActivity extends AppCompatActivity implements AddGroceryDialog.AddGroceryDialogListener, RecipeFragment.RecipeClickedListener, EditGroceryDialog.EditGroceryDialogListener {

    private static final String TAG = "UserActivity";

    // firebase objects
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database;
    private DatabaseReference current_user_db;

    // Fragments
    private RecipeDetailFragment recipeDetailFragment;
    private FragmentTransaction ft;
    private Fragment selectedFragment = new RecipeFragment();

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener(){
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item){
//                    Fragment selectedFragment = null;

                    switch (item.getItemId()){
                        case R.id.nav_recipe:
                            selectedFragment = new RecipeFragment();
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

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).addToBackStack(null).commit();
                    return true;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).addToBackStack(null).commit();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);

        bottomNav.setOnNavigationItemSelectedListener(navListener);

        if(getIntent().getStringExtra("grocerylistFragment")!=null && getIntent().getStringExtra("grocerylistFragment").equals("OpenPersonalGroceryList")){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new GrocerylistFragment()).addToBackStack(null).commit();
        }

        recipeDetailFragment = new RecipeDetailFragment();
        ft = getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, recipeDetailFragment);

        // firebase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String currentUID = currentUser.getUid();
            current_user_db = database.getReference().child("Users").child(currentUID);
        }

        recipeDetailFragment = new RecipeDetailFragment();
    }

    @Override
    public void addGrocery(String groceryName, int amount, String unit) {
        // once user hits add, make a query to database to retrieve the grocery item by name (API??)
        // once got the data for the grocery item, save it in current user grocery list
        GroceryItem newGroceryEntry = new GroceryItem();
        newGroceryEntry.setName(groceryName);
        newGroceryEntry.setAmount(amount);
        newGroceryEntry.setUnit(unit);
        DatabaseReference currentUserGroceryList = current_user_db.child("grocery_list");
        if(selectedFragment instanceof GrocerylistFragment){
            System.out.println("current tab is: "+((GrocerylistFragment) selectedFragment).currentTab);
            if(((GrocerylistFragment) selectedFragment).currentTab==0) currentUserGroceryList = currentUserGroceryList.child("personal");
            if(((GrocerylistFragment) selectedFragment).currentTab==1) currentUserGroceryList = currentUserGroceryList.child("shared");
            currentUserGroceryList.push().setValue(newGroceryEntry);
        }
    }

    @Override
    public void onRecipeSent(RecipeModel rm) {
//        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, recipeDetailFragment).commit();
        ft.commit();
        recipeDetailFragment.getRecipeDetail(rm);
    }

    @Override
    public void setShared(String groceryName, int amount, String unit, String sharedWith) {

    }

    @Override
    public void updateGrocery(String groceryName, int amount, String unit) {

    }

    @Override
    public void deleteGrocery(String groceryID) {
        Log.d(TAG, "deleteGrocery: "+groceryID);
        //
        DatabaseReference currentUserGroceryList = current_user_db.child("grocery_list");
        if(selectedFragment instanceof GrocerylistFragment){
            System.out.println("current tab is: "+((GrocerylistFragment) selectedFragment).currentTab);
            if(((GrocerylistFragment) selectedFragment).currentTab==0) currentUserGroceryList = currentUserGroceryList.child("personal");
            if(((GrocerylistFragment) selectedFragment).currentTab==1) currentUserGroceryList = currentUserGroceryList.child("shared");
            DatabaseReference item = currentUserGroceryList.child(groceryID);
            item.setValue(null);
        }
    }
}
