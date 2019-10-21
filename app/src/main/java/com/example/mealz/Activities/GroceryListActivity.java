package com.example.mealz.Activities;

import android.os.Bundle;

import com.example.mealz.Adapters.SectionsPageAdapter;
import com.example.mealz.Dialogs.AddGroceryDialog;
import com.example.mealz.Fragments.PersonalGrocerylistFragment;
import com.example.mealz.Fragments.SharedGrocerylistFragment;
import com.example.mealz.Models.GroceryItem;
import com.example.mealz.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class GroceryListActivity extends AppCompatActivity implements AddGroceryDialog.AddGroceryDialogListener {

    private static final String TAG = "GroceryListActivity";

    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager viewPager;

    // firebase objects
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database;
    private DatabaseReference current_user_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        viewPager = findViewById(R.id.container);
        setUpViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String currentUID = currentUser.getUid();
            current_user_db = database.getReference().child("Users").child(currentUID);
        }
    }

    private void setUpViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new PersonalGrocerylistFragment(), "Personal List");
        adapter.addFragment(new SharedGrocerylistFragment(), "Shared List");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void addGrocery(String groceryName, int amount, String unit) {
        // once user hits add, make a query to database to retrieve the grocery item by name (API??)
        // once got the data for the grocery item, save it in current user grocery list
        GroceryItem newGroceryEntry = new GroceryItem();
        newGroceryEntry.setName(groceryName);
        newGroceryEntry.setAmount(amount);
        newGroceryEntry.setUnit(unit);
        if (currentUser != null) {
            String currentUID = currentUser.getUid();
            current_user_db = database.getReference().child("Users").child(currentUID);
            DatabaseReference currentUserGroceryList = current_user_db.child("grocery_list").child("personal");
            currentUserGroceryList.push().setValue(newGroceryEntry);
        }
    }
}