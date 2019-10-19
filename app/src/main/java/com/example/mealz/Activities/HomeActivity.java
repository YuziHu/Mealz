package com.example.mealz.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mealz.Dialogs.AddGroceryDialog;
import com.example.mealz.Models.GroceryItem;
import com.example.mealz.R;
import com.example.mealz.Adapters.GroceryListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements AddGroceryDialog.AddGroceryDialogListener {

    private Button addGroceryBtn;
    private Button searchRecipeBtn;
    private ListView groceryListView;
    GroceryListAdapter adapter;
    // get grocery list as a list from firebase
    List<GroceryItem> groceryList = new ArrayList<>();
    List<String> groceryNames = new ArrayList<>();
    List<Integer> groceryAmount = new ArrayList<>();
    List<String> groceryUnits = new ArrayList<>();

    // edit grocery actions spinner
//    private Spinner editGroceryActions;

    // firebase objects
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database;
    private DatabaseReference current_user_db;

    // sign out user
    private Button signout;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        addGroceryBtn = findViewById(R.id.addGroceryItemBtn);
        searchRecipeBtn = findViewById(R.id.toSearchRecipeBtn);
        groceryListView = findViewById(R.id.groceryListView);

        adapter = new GroceryListAdapter(this, groceryNames, groceryAmount, groceryUnits);
        groceryListView.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        currentUser = mAuth.getCurrentUser();

        // signout
        signout = findViewById(R.id.signoutBtn);
        setUpFirebaseListener();
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
            }
        });


        // populate grocery list view if current user has grocery items in list
        if (currentUser != null) {
            String currentUID = currentUser.getUid();
            current_user_db = database.getReference().child("Users").child(currentUID);
            // if current user has grocery list
            DatabaseReference currentUserGroceryList = current_user_db.child("grocery_list");
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
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }

        addGroceryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // pops up a window and lets user to add a grocery by name
                openDialog();
            }
        });

        searchRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toSearchActivity = new Intent(getApplicationContext(), SearchRecipeActivity.class);
                startActivity(toSearchActivity);
            }
        });


    }

    private void setUpFirebaseListener() {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                } else {
                    Toast.makeText(HomeActivity.this, "Signed out", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthStateListener);
        }
    }

    private void openDialog() {
        AddGroceryDialog addGroceryDialog = new AddGroceryDialog();
        addGroceryDialog.show(getSupportFragmentManager(), "Add grocery item");
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
        currentUserGroceryList.push().setValue(newGroceryEntry);
    }
}
