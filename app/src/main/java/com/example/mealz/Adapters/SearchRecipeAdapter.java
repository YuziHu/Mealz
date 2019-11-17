package com.example.mealz.Adapters;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mealz.Models.Recipe;
import com.example.mealz.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class SearchRecipeAdapter extends RecyclerView.Adapter<SearchRecipeAdapter.ViewHolder> {
    private static final String TAG = "SearchRecipeAdapter";

    private Context context;
    private ArrayList<String> recipeImages = new ArrayList<>();
    private ArrayList<String> recipeNames = new ArrayList<>();
    private RecipeImageClickListener recipeImageClickListener;

    public SearchRecipeAdapter(Context context, RecipeImageClickListener recipeImageClickListener, ArrayList<String> recipeImages, ArrayList<String> recipeNames) {
        this.context = context;
        this.recipeImages = recipeImages;
        this.recipeNames = recipeNames;
        this.recipeImageClickListener = recipeImageClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_recipe_search, parent, false);
        ViewHolder holder = new ViewHolder(view, recipeImageClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: ");
        String img_url = recipeImages.get(position);
        if (!img_url.equalsIgnoreCase(""))
            Picasso.get().load(img_url).placeholder(R.drawable.ic_launcher_background)// Place holder image from drawable folder
                    .error(R.drawable.b).resize(400, 400).centerCrop()
                    .into(holder.recipeImage);
        holder.recipeName.setText(recipeNames.get(position));
    }

    @Override
    public int getItemCount() {
        return recipeNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageButton recipeImage;
        TextView recipeName;
        ConstraintLayout recipelistParentLayout;
        RecipeImageClickListener recipeImageClickListener;

        public ViewHolder(@NonNull View itemView, RecipeImageClickListener recipeImageClickListener) {
            super(itemView);
            recipeImage = itemView.findViewById(R.id.recipe_image);
            recipeName = itemView.findViewById(R.id.recipe_name);
            recipelistParentLayout = itemView.findViewById(R.id.single_recipe_parent_layout);
            this.recipeImageClickListener = recipeImageClickListener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: called");
            recipeImageClickListener.onRecipeImageClick(getAdapterPosition());
        }
    }

    // on click interface
    public interface RecipeImageClickListener{
        void onRecipeImageClick(int position);
    }
}
