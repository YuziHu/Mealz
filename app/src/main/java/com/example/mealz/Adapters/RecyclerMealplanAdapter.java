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

    private List<String> imageUrls;
    private List<String> mealplanNames;
    private Context context;
    private String tag;
    private MealPlanClickListener onMealplanClickListener;

    public RecyclerMealplanAdapter(MealPlanClickListener onMealplanClickListener, String tag, Context context, List<String> imageUrls, List<String> mealplanNames) {
        this.imageUrls = imageUrls;
        this.mealplanNames = mealplanNames;
        this.context = context;
        this.tag = tag;
        this.onMealplanClickListener = onMealplanClickListener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called");

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_single_mealplan, parent, false);
        return new ViewHolder(view, onMealplanClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(imageUrls!=null && imageUrls.size()>0){
            Glide.with(context)
                    .asBitmap()
                    .load(imageUrls.get(position))
                    .into(holder.image);
            holder.name.setText(mealplanNames.get(position));

        }
    }

    @Override
    public int getItemCount() {
        return mealplanNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView name;
        MealPlanClickListener onMealplanClickListener;

        public ViewHolder(@NonNull View itemView, MealPlanClickListener onMealplanClickListener) {
            super(itemView);
            image = itemView.findViewById(R.id.mealplan_image);
            name = itemView.findViewById(R.id.mealplan_name);
            this.onMealplanClickListener = onMealplanClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: "+tag);
            onMealplanClickListener.onMealplanClick(tag, getAdapterPosition());
        }
    }
    public interface MealPlanClickListener{
        void onMealplanClick(String tag, int position);
    }
}
