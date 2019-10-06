package com.example.mealz.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mealz.Dialogs.AddGroceryDialog;
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

public class HomeActivity extends AppCompatActivity implements AddGroceryDialog.AddGroceryDialogListener {

    private Button addGroceryBtn;
    private ListView groceryListView;
    CustomAdapter adapter;
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

    // pop up window for adding a grocery item
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        addGroceryBtn = findViewById(R.id.addGroceryItemBtn);
        groceryListView = findViewById(R.id.groceryListView);

        adapter = new CustomAdapter(this, groceryNames, groceryAmount, groceryUnits);
        groceryListView.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        currentUser = mAuth.getCurrentUser();


        // populate grocery list view if current user has grocery items in list
        String currentUID = currentUser.getUid();
        current_user_db = database.getReference().child("Users").child(currentUID);
        // if current user has grocery list
        DatabaseReference currentUserGroceryList = current_user_db.child("grocery_list");
        if(currentUserGroceryList!=null){
            currentUserGroceryList.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
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

        addGroceryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // pops up a window and lets user to add a grocery by name
                openDialog();
                // once got the data for the grocery item, save it in current user grocery list
            }
        });
    }

    private void openDialog() {
        AddGroceryDialog addGroceryDialog = new AddGroceryDialog();
        addGroceryDialog.show(getSupportFragmentManager(), "Add grocery item");
    }

    @Override
    public void addGrocery(String groceryName, int amount, String unit) {
        // once user hits add, make a query to database to retrieve the grocery item by name
        GroceryItem newGroceryEntry = new GroceryItem();
        newGroceryEntry.setName(groceryName);
        newGroceryEntry.setAmount(amount);
        newGroceryEntry.setUnit(unit);
        DatabaseReference currentUserGroceryList = current_user_db.child("grocery_list");
        currentUserGroceryList.push().setValue(newGroceryEntry);
    }

    class CustomAdapter extends ArrayAdapter<String>{
        Context context;
        List<String> groceryNames;
        List<Integer> groceryAmount;
        List<String> groceryUnits;

        CustomAdapter(Context c, List<String> names, List<Integer> amount, List<String> units){
            super(c, R.layout.layout_grocery_item, R.id.groceryName, names);
            this.context = c;
            this.groceryNames = names;
            this.groceryAmount = amount;
            this.groceryUnits = units;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View grocery_item = layoutInflater.inflate((R.layout.layout_grocery_item),parent, false);
            TextView gName = grocery_item.findViewById(R.id.groceryName);
            TextView gAmount = grocery_item.findViewById(R.id.gAmount);
            TextView gUnit = grocery_item.findViewById(R.id.gUnit);


            gName.setText(groceryNames.get(position));
            gAmount.setText(groceryAmount.get(position).toString());
            gUnit.setText(groceryUnits.get(position));

            return grocery_item;
        }
    }
}
