package com.example.mealz.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mealz.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerGrocerylistAdapter extends RecyclerView.Adapter<RecyclerGrocerylistAdapter.ViewHolder> {
    private static final String TAG = "RecyclerGrocerylistAdap";

    Context context;
    List<String> groceryNames;
    List<Integer> groceryAmount;
    List<String> groceryUnits;
    List<String> groceryShares;

    public RecyclerGrocerylistAdapter(Context context, List<String> groceryNames, List<Integer> groceryAmount, List<String> groceryUnits, List<String> groceryShares) {
        this.context = context;
        this.groceryNames = groceryNames;
        this.groceryAmount = groceryAmount;
        this.groceryUnits = groceryUnits;
        this.groceryShares = groceryShares;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_grocery_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");

        holder.gName.setText(groceryNames.get(position));
        holder.gAmount.setText(groceryAmount.get(position).toString());
        holder.gUnit.setText(groceryUnits.get(position));
        if(groceryShares!=null) holder.gShare.setText(groceryShares.get(position));
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            gName = itemView.findViewById(R.id.groceryName);
            gAmount = itemView.findViewById(R.id.gAmount);
            gUnit = itemView.findViewById(R.id.gUnit);
            gShare = itemView.findViewById(R.id.gShare);
        }
    }
}
