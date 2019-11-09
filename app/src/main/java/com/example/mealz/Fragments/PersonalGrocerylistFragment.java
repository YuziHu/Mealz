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

    private RecyclerView personalGrocerylist;
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




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_grocerylist, container, false);

        personalGrocerylist = view.findViewById(R.id.personalGrocerylistView);
        personalGrocerylist.setLayoutManager(new LinearLayoutManager(getActivity()));
        rAdapter = new RecyclerGrocerylistAdapter(getActivity(),groceryList, groceryNames, groceryAmount, groceryUnits, null, this);
        personalGrocerylist.setAdapter(rAdapter);


        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        currentUser = mAuth.getCurrentUser();


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
                            item.setGid(ds.getKey());
                            groceryList.add(item);
                            groceryNames.add(item.getName());
                            groceryAmount.add(item.getAmount());
                            groceryUnits.add(item.getUnit());
                        }
//                        initRecyclerView();
                        rAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }

        return view;
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview");
        personalGrocerylist.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    @Override
    public void onEditIconClick(int position) {
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

}
