package com.example.mealz.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mealz.Models.GroceryItem;
import com.example.mealz.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerGrocerylistAdapter extends RecyclerView.Adapter<RecyclerGrocerylistAdapter.ViewHolder> {
    private static final String TAG = "RecyclerGrocerylistAdap";

    Context context;
    List<GroceryItem> groceryItemList;
    List<String> groceryNames;
    List<Integer> groceryAmount;
    List<String> groceryUnits;
    List<String> groceryShares;
    // OnEditIconClickListener
    private OnEditIconClickListener onEditIconClickListener;
    // Spinner
//    Spinner editGrocerySpinner;
    // Firebase objects
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database;
    private DatabaseReference current_user_db;

    public RecyclerGrocerylistAdapter(Context context,
                                      List<GroceryItem> groceryItemList,
                                      List<String> groceryNames,
                                      List<Integer> groceryAmount,
                                      List<String> groceryUnits,
                                      List<String> groceryShares,
                                      OnEditIconClickListener onEditIconClickListener) {
        this.context = context;
        this.groceryItemList = groceryItemList;
        this.groceryNames = groceryNames;
        this.groceryAmount = groceryAmount;
        this.groceryUnits = groceryUnits;
        this.groceryShares = groceryShares;
        this.onEditIconClickListener = onEditIconClickListener;
        // Firebase initialization
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        currentUser = mAuth.getCurrentUser();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_grocery_item, parent, false);
//        this.editGrocerySpinner = view.findViewById(R.id.editGrocery);
//        ArrayAdapter<String> editGroceryActionAdapter = new ArrayAdapter<String>(context,
//                android.R.layout.simple_list_item_1, context.getResources().getStringArray(R.array.editGroceryActions));
//        editGroceryActionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        editGrocerySpinner.setAdapter(editGroceryActionAdapter);
//        editGrocerySpinner.setOnItemSelectedListener(this);

        ViewHolder holder = new ViewHolder(view, onEditIconClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");

        holder.gName.setText(groceryNames.get(position));
        holder.gAmount.setText(groceryAmount.get(position).toString());
        holder.gUnit.setText(groceryUnits.get(position));
        if(groceryShares!=null && groceryShares.get(position)!=null) holder.gShare.setText("Shared With: "+groceryShares.get(position));
        else holder.gShare.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return groceryNames.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView gName;
        TextView gAmount;
        TextView gUnit;
        TextView gShare;
        //ImageView editGroceryAction;
        ImageButton ingredientEdit;
        //
        OnEditIconClickListener onEditIconClickListener;

        public ViewHolder(@NonNull View itemView, final OnEditIconClickListener onEditIconClickListener) {
            super(itemView);

            gName = itemView.findViewById(R.id.groceryName);
            gAmount = itemView.findViewById(R.id.gAmount);
            gUnit = itemView.findViewById(R.id.gUnit);
            gShare = itemView.findViewById(R.id.gShare);
            ingredientEdit = itemView.findViewById(R.id.ingridientEdit);
            //editGroceryAction = itemView.findViewById(R.id.editGrocery);
            this.onEditIconClickListener = onEditIconClickListener;
            ingredientEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onEditIconClickListener.onEditIconClick(getAdapterPosition());
                }
            });
//            itemView.setOnClickListener(this);
        }

    }

    public interface OnEditIconClickListener {
        void onEditIconClick(int position);
    }
}
