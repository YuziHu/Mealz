package com.example.mealz.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mealz.Adapters.SectionsPageAdapter;
import com.example.mealz.Dialogs.AddGroceryDialog;
import com.example.mealz.Models.GroceryItem;
import com.example.mealz.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class GrocerylistFragment extends Fragment implements AddGroceryDialog.AddGroceryDialogListener {

    private static final String TAG = "GroceryListFragment";

    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager viewPager;

    // firebase objects
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database;
    private DatabaseReference current_user_db;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_grocerylist, container, false);

        mSectionsPageAdapter = new SectionsPageAdapter(getChildFragmentManager());

        viewPager = view.findViewById(R.id.grocerylist_container);
        setUpViewPager(viewPager);

        TabLayout tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String currentUID = currentUser.getUid();
            current_user_db = database.getReference().child("Users").child(currentUID);
        }

        return view;
    }

    private void setUpViewPager(ViewPager viewPager){
        mSectionsPageAdapter.addFragment(new PersonalGrocerylistFragment(), "Personal List");
        mSectionsPageAdapter.addFragment(new SharedGrocerylistFragment(), "Shared List");
        viewPager.setAdapter(mSectionsPageAdapter);
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
            DatabaseReference currentUserGroceryList = current_user_db.child("grocery_list");
            currentUserGroceryList.push().setValue(newGroceryEntry);
        }
    }
}
