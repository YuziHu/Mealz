package com.example.mealz.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mealz.Activities.HomeActivity;
import com.example.mealz.Models.GroceryItem;
import com.example.mealz.Models.IngredientModel;
import com.example.mealz.Models.Recipe;
import com.example.mealz.Models.RecipeModel;
import com.example.mealz.R;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class RecipeDetailFragment extends Fragment {

    public static String curr = "";
    public static String rp = "";
    public static final String EXTRA_TEXT = "com.example.mealz.example.EXTRA_TEXT";
    // firebase objects
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database;
    private DatabaseReference current_user_db;
    //
    private View view;
    //
    private List<IngredientModel> ingredients;
    private ImageView img;
    private TextView ingredient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        Log.i("view: ",view.toString());
        System.out.println(view);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        currentUser = mAuth.getCurrentUser();

        img = view.findViewById(R.id.imageView);
        ingredient = view.findViewById(R.id.textView2);

//        RecipeModel recipe=(RecipeModel) getIntent().getSerializableExtra("recipeList");

//        ImageView img = view.findViewById(R.id.imageView);
//
//        String img_url = recipe.getImage();
//
//        if (!img_url.equalsIgnoreCase(""))
//            Picasso.get().load(img_url).placeholder(R.drawable.ic_launcher_background)// Place holder image from drawable folder
//                    .error(R.drawable.b).resize(110, 110).centerCrop()
//                    .into(img);
//
//        TextView ingredient = view.findViewById(R.id.textView2);
//        ObjectMapper mapper = new ObjectMapper();
//        final List<IngredientModel> ingredients = mapper.convertValue(recipe.getIngredients(), new TypeReference<List<IngredientModel>>(){});
//        System.out.println(ingredients);
//        String result = "";
//        for (int i = 0; i < ingredients.size(); i++) {
//            if(ingredients.get(i).getWeight()>0){
//                result += ingredients.get(i).getText();
//                result += "\n\n";
//            }
//        }
//
//        ingredient.setText(result);
//        curr = result;

        // add ingredients of current food into database
        Button add = view.findViewById(R.id.button2);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Recipe Added Successfully", Toast.LENGTH_SHORT).show();
                if(currentUser != null){
                    String currentUID = currentUser.getUid();
                    current_user_db = database.getReference().child("Users").child(currentUID);
                    for(IngredientModel ingredient : ingredients){
                        if(ingredient.getWeight()>0){
                            GroceryItem newGroceryEntry = new GroceryItem();
                            newGroceryEntry.setName(ingredient.getText());
                            newGroceryEntry.setAmount((int)Math.floor(ingredient.getWeight()));
                            newGroceryEntry.setUnit("g");
                            DatabaseReference currentUserGroceryList = current_user_db.child("grocery_list");
                            currentUserGroceryList.push().setValue(newGroceryEntry);
                        }
                    }
                }
            }
        });

        Button openList = view.findViewById(R.id.button3);
        openList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGroceryList();
            }
        });

        return view;
    }

    public void getRecipeDetail(RecipeModel recipe){
//        System.out.println(recipe);
//        img = view.findViewById(R.id.imageView);

        String img_url = recipe.getImage();

        if (!img_url.equalsIgnoreCase(""))
            Picasso.get().load(img_url).placeholder(R.drawable.ic_launcher_background)// Place holder image from drawable folder
                    .error(R.drawable.b).resize(110, 110).centerCrop()
                    .into(img);

//        TextView ingredient = view.findViewById(R.id.textView2);
        ObjectMapper mapper = new ObjectMapper();
        ingredients = mapper.convertValue(recipe.getIngredients(), new TypeReference<List<IngredientModel>>(){});
//        System.out.println(ingredients);
        String result = "";
        for (int i = 0; i < ingredients.size(); i++) {
            if(ingredients.get(i).getWeight()>0){
                result += ingredients.get(i).getText();
                result += "\n\n";
            }
        }
        ingredient.setText(result);
        curr = result;
    }

    public void openGroceryList() {
//        Intent intent = new Intent(this, HomeActivity.class);
////        intent.putExtra(EXTRA_TEXT, rp);
//        startActivity(intent);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new GrocerylistFragment()).commit();
    }
}
