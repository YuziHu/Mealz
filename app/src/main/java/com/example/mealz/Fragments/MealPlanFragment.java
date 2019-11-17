package com.example.mealz.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mealz.Activities.LoginActivity;
import com.example.mealz.Activities.SearchRecipeActivity;
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

public class MealPlanFragment extends Fragment {

    private static final String TAG = "MealPlanFragment";

    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    public int currentTab;

    // buttons
    private Button searchRecipeBtn;
    private Button addGroceryBtn;

    // firebase objects
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database;
    private DatabaseReference current_user_db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_mealplan, container, false);

        mSectionsPageAdapter = new SectionsPageAdapter(getChildFragmentManager());

        viewPager = view.findViewById(R.id.mealplan_container);
        setUpViewPager(viewPager);

        tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        currentTab = 0;

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                System.out.println("current tab index: "+tab.getPosition());
                currentTab = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

//        searchRecipeBtn = view.findViewById(R.id.toSearchRecipeBtn);
//        addGroceryBtn = view.findViewById(R.id.addGroceryItemBtn);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String currentUID = currentUser.getUid();
            current_user_db = database.getReference().child("Users").child(currentUID);
        }


//        searchRecipeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent toSearchActivity = new Intent(getActivity(), SearchRecipeActivity.class);
//                startActivity(toSearchActivity);
//            }
//        });
//        addGroceryBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // pops up a window and lets user to add a grocery by name
//                openDialog();
//            }
//        });

        return view;
    }



    private void setUpViewPager(ViewPager viewPager){
//        SectionsPageAdapter adapter = new SectionsPageAdapter(getFragmentManager());
        mSectionsPageAdapter.addFragment(new CurrentMealplanFragment(), "UNDERGOING");
        mSectionsPageAdapter.addFragment(new FutureMealplanFragment(), "NEXT WEEK");
        viewPager.setAdapter(mSectionsPageAdapter);
    }

//    private void openDialog() {
//        AddGroceryDialog addGroceryDialog = new AddGroceryDialog();
//        addGroceryDialog.show(getFragmentManager(), "Add grocery item");
//    }

//    @Override
//    public void addGrocery(String groceryName, int amount, String unit) {
//        // once user hits add, make a query to database to retrieve the grocery item by name (API??)
//        // once got the data for the grocery item, save it in current user grocery list
//        GroceryItem newGroceryEntry = new GroceryItem();
//        newGroceryEntry.setName(groceryName);
//        newGroceryEntry.setAmount(amount);
//        newGroceryEntry.setUnit(unit);
//        if (currentUser != null) {
//            String currentUID = currentUser.getUid();
//            current_user_db = database.getReference().child("Users").child(currentUID);
//            DatabaseReference currentUserGroceryList = current_user_db.child("grocery_list");
////            System.out.println("current tab: "+currentTab);
////            if(currentTab==0) currentUserGroceryList = currentUserGroceryList.child("personal");
////            if(currentTab==1) currentUserGroceryList = currentUserGroceryList.child("shared");
//            currentUserGroceryList.push().setValue(newGroceryEntry);
//        }
//    }
}
