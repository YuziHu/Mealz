package com.example.mealz.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mealz.Adapters.RecyclerGrocerylistAdapter;
import com.example.mealz.Dialogs.AddGroceryDialog;
import com.example.mealz.Dialogs.EditGroceryDialog;
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

public class PersonalGrocerylistFragment extends Fragment implements RecyclerGrocerylistAdapter.OnEditIconClickListener {

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
//                            System.out.println(ds.getKey());
                            item.setGid(ds.getKey());
                            groceryList.add(item);
                            groceryNames.add(item.getName());
                            groceryAmount.add(item.getAmount());
                            groceryUnits.add(item.getUnit());
                        }
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
        rAdapter = new RecyclerGrocerylistAdapter(getActivity(),groceryList, groceryNames, groceryAmount, groceryUnits, null, this);
        personalGrocerylistRecyclerView.setAdapter(rAdapter);
        personalGrocerylistRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    @Override
    public void onEditIconClick(int position) {
//        System.out.println(groceryList);
//        System.out.println(position);
        Log.d(TAG, "onEditIconClick: "+groceryList.get(position).getGid());
        // create a dialog box
        GroceryItem item = (GroceryItem) groceryList.get(position);
        final String itemID = item.getGid();
        final String itemName = groceryNames.get(position);
        final int itemAmount = groceryAmount.get(position);
        final String itemUnit = groceryUnits.get(position);
        //
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_grocery, null);
        //
        TextView editGroceryName, editGroceryUnit;
        EditText editGroceryAmount;
        Button setSharedGroceryBtn, updateGroceryBtn, deleteGroceryBtn;
        //
        editGroceryName = dialogView.findViewById(R.id.editGroceryName);
        editGroceryAmount = dialogView.findViewById(R.id.editGroceryAmount);
        editGroceryUnit = dialogView.findViewById(R.id.editGroceryUnit);
        setSharedGroceryBtn = dialogView.findViewById(R.id.setSharedGroceryBtn);
        updateGroceryBtn = dialogView.findViewById(R.id.updateGroceryBtn);
        deleteGroceryBtn = dialogView.findViewById(R.id.deleteGroceryBtn);
        //
        editGroceryName.setText(groceryNames.get(position));
        editGroceryAmount.setText(groceryAmount.get(position).toString());
        editGroceryUnit.setText(groceryUnits.get(position));

        builder.setView(dialogView)
                .setTitle("Edit Grocery")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        final AlertDialog editGroceryDialog = builder.create();
        setSharedGroceryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setShared(itemName, itemAmount, itemUnit, "");
                editGroceryDialog.dismiss();
            }
        });
        updateGroceryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateGrocery(itemName, itemAmount, itemUnit);
                editGroceryDialog.dismiss();
            }
        });
        deleteGroceryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteGrocery(itemID);
                editGroceryDialog.dismiss();
            }
        });

        editGroceryDialog.show();
    }

    public void setShared(String groceryName, int amount, String unit, String sharedWith) {

    }

    public void updateGrocery(String groceryName, int amount, String unit) {

    }

    public void deleteGrocery(String groceryID) {
        Log.d(TAG, "deleteGrocery: "+groceryID);
        //
        DatabaseReference currentUserGroceryList = current_user_db.child("grocery_list").child("personal");
            DatabaseReference item = currentUserGroceryList.child(groceryID);
            item.setValue(null);
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
