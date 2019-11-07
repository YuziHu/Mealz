package com.example.mealz.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.mealz.Adapters.RecyclerGrocerylistAdapter;
import com.example.mealz.Models.GroceryItem;
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
//import android.support.v4.app.Fragment;

public class PersonalGrocerylistFragment extends Fragment {

    private static final String TAG = "PersonalGroceryListFrag";

    private Button addGroceryBtn;
    private ListView groceryListView;
    RecyclerGrocerylistAdapter rAdapter;
    // get grocery list as a list from firebase
    List<GroceryItem> groceryList = new ArrayList<>();
    List<String> groceryNames = new ArrayList<>();
    List<Integer> groceryAmount = new ArrayList<>();
    List<String> groceryUnits = new ArrayList<>();

    // firebase objects
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database;
    private DatabaseReference current_user_db;

//    // sign out user
//    private Button signout;
//    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_grocerylist, container, false);

        addGroceryBtn = view.findViewById(R.id.addGroceryItemBtn);
        groceryListView = view.findViewById(R.id.groceryListView);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        currentUser = mAuth.getCurrentUser();

        // signout
//        signout = view.findViewById(R.id.signoutBtn);
//        setUpFirebaseListener();
//        signout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseAuth.getInstance().signOut();
//            }
//        });


        // populate grocery list view if current user has grocery items in list
        if (currentUser != null) {
            String currentUID = currentUser.getUid();
            current_user_db = database.getReference().child("Users").child(currentUID);
            // if current user has grocery list
            DatabaseReference currentUserGroceryList = current_user_db.child("grocery_list").child("personal");
            if (currentUserGroceryList != null) {
                currentUserGroceryList.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        groceryList.clear();
                        groceryNames.clear();
                        groceryAmount.clear();
                        groceryUnits.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            GroceryItem item = ds.getValue(GroceryItem.class);
                            groceryList.add(item);
                            groceryNames.add(item.getName());
                            groceryAmount.add(item.getAmount());
                            groceryUnits.add(item.getUnit());
                        }
//                        adapter.notifyDataSetChanged();
//                        rAdapter.notifyDataSetChanged();
                        initRecyclerView();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }

//        addGroceryBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // pops up a window and lets user to add a grocery by name
//                openDialog();
//            }
//        });


        return view;
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview");
        RecyclerView personalGrocerylistRecyclerView = getView().findViewById(R.id.personalGrocerylistView);
        rAdapter = new RecyclerGrocerylistAdapter(getActivity(),groceryNames, groceryAmount, groceryUnits, null);
        personalGrocerylistRecyclerView.setAdapter(rAdapter);
        personalGrocerylistRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

//    private void setUpFirebaseListener() {
//        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null) {
//
//                } else {
//                    Toast.makeText(getActivity(), "Signed out", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(getActivity(), LoginActivity.class);
//                    startActivity(intent);
//                }
//            }
//        };
//    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        FirebaseAuth.getInstance().addAuthStateListener(mAuthStateListener);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (mAuthStateListener != null) {
//            FirebaseAuth.getInstance().removeAuthStateListener(mAuthStateListener);
//        }
//    }

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
//        DatabaseReference currentUserGroceryList = current_user_db.child("grocery_list").child("shared");
//        currentUserGroceryList.push().setValue(newGroceryEntry);
//    }
}
