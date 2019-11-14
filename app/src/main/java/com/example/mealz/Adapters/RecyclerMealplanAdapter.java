package com.example.mealz.Adapters;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mealz.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerMealplanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "RecyclerMealplanAdapter";

//    private static final int PENDING_MEALPLAN_LISTVIEW = 1;

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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called" + viewType);
        View view = null;
        if(tag=="PENDING"){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_single_mealplan_pending, parent, false);
            return new ViewHolderPending(view, onMealplanClickListener);
        }
        else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_single_mealplan, parent, false);
            return new ViewHolderOther(view, onMealplanClickListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ViewHolderPending) {
            if(imageUrls!=null && imageUrls.size()>0){
                Glide.with(context)
                        .asBitmap()
                        .load(imageUrls.get(position))
                        .into(((ViewHolderPending) holder).image);
                ((ViewHolderPending) holder).name.setText(mealplanNames.get(position));
            }
        }
        else
        if(imageUrls!=null && imageUrls.size()>0){
            Glide.with(context)
                    .asBitmap()
                    .load(imageUrls.get(position))
                    .into(((ViewHolderOther) holder).image);
            ((ViewHolderOther) holder).name.setText(mealplanNames.get(position));

        }
    }

    @Override
    public int getItemCount() {
        return mealplanNames.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(tag=="PENDING") return 1;
        else return 0;
    }

    public class ViewHolderPending extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView name;
        // for pending mealplan list
        Button dislike;
        Button like;
        MealPlanClickListener onMealplanClickListener;

        public ViewHolderPending(@NonNull View itemView, MealPlanClickListener onMealplanClickListener) {
            super(itemView);
            image = itemView.findViewById(R.id.mealplan_image);
            name = itemView.findViewById(R.id.mealplan_name);
            dislike = itemView.findViewById(R.id.dislike_button);
            like = itemView.findViewById(R.id.like_button);
            this.onMealplanClickListener = onMealplanClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: "+tag);
            onMealplanClickListener.onMealplanClick(tag, getAdapterPosition());
        }
    }

    public class ViewHolderOther extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView name;
        MealPlanClickListener onMealplanClickListener;

        public ViewHolderOther(@NonNull View itemView, MealPlanClickListener onMealplanClickListener) {
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
