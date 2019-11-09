package com.example.mealz.Adapters;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mealz.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerMealplanAdapter extends RecyclerView.Adapter<RecyclerMealplanAdapter.ViewHolder> {

    private static final String TAG = "RecyclerMealplanAdapter";

    private ArrayList<String> imageUrls;
    private ArrayList<String> mealplanNames;
    private Context context;

    public RecyclerMealplanAdapter(Context context, ArrayList<String> imageUrls, ArrayList<String> mealplanNames) {
        this.imageUrls = imageUrls;
        this.mealplanNames = mealplanNames;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called");

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_single_mealplan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(imageUrls!=null && imageUrls.size()>0){
            Glide.with(context)
                    .asBitmap()
                    .load(imageUrls.get(position))
                    .into(holder.image);
            holder.name.setText(mealplanNames.get(position));

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick: image clicked");
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mealplanNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.mealplan_image);
            name = itemView.findViewById(R.id.mealplan_name);
        }
    }
}
