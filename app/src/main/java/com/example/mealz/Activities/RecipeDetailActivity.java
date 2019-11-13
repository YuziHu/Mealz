package com.example.mealz.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mealz.Fragments.PersonalGrocerylistFragment;
import com.example.mealz.Models.GroceryItem;
import com.example.mealz.Models.IngredientModel;
import com.example.mealz.Models.MealPlanModel;
import com.example.mealz.Models.RecipeModel;
import com.example.mealz.R;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {
    private static final String TAG = "RecipeDetailActivity";

    public static String curr = "";
    public static String rp = "";
    public static final String EXTRA_TEXT = "com.example.mealz.example.EXTRA_TEXT";
    // firebase objects
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database;
    private DatabaseReference current_user_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        currentUser = mAuth.getCurrentUser();

        RecipeModel recipe=(RecipeModel) getIntent().getSerializableExtra("recipe");

        ImageView img = findViewById(R.id.imageView);

        final String img_url = recipe.getImage();
        final String label = recipe.getLabel();
        if (!img_url.equalsIgnoreCase(""))
            Picasso.get().load(img_url).placeholder(R.drawable.ic_launcher_background)// Place holder image from drawable folder
                    .error(R.drawable.b).resize(110, 110).centerCrop()
                    .into(img);

        TextView ingredient = findViewById(R.id.textView2);
        ObjectMapper mapper = new ObjectMapper();
        final List<IngredientModel> ingredients = mapper.convertValue(recipe.getIngredients(), new TypeReference<List<IngredientModel>>(){});
        System.out.println(ingredients);
        String result = "";
        for (int i = 0; i < ingredients.size(); i++) {
            if(ingredients.get(i).getWeight()>0){
                result += ingredients.get(i).getText();
                result += "\n\n";
            }
        }

        //Intent intent = new Intent(this, GroceryActivity.class);

        ingredient.setText(result);
        curr = result;

        // add ingredients of current food into database
        Button addToGrocerylist = findViewById(R.id.button2);
        addToGrocerylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Ingredients Added Successfully", Toast.LENGTH_SHORT).show();
                if(currentUser != null){
                    String currentUID = currentUser.getUid();
                    current_user_db = database.getReference().child("Users").child(currentUID);
                    for(IngredientModel ingredient : ingredients){
                        if(ingredient.getWeight()>0){
                            GroceryItem newGroceryEntry = new GroceryItem();
                            newGroceryEntry.setName(ingredient.getText());
                            newGroceryEntry.setAmount((int)Math.floor(ingredient.getWeight()));
                            newGroceryEntry.setUnit("g");
                            DatabaseReference currentUserGroceryList = current_user_db.child("grocery_list").child("personal");
                            currentUserGroceryList.push().setValue(newGroceryEntry);
                        }
                    }
                }
//                rp += curr;
//                System.out.println("rp: "+rp);
            }
        });

        // add ingredients of current recipe into database
        Button addToMealplan = findViewById(R.id.addToMealplanBtn);
        addToMealplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Recipe Added Successfully", Toast.LENGTH_SHORT).show();
                if(currentUser != null){
                    String currentUID = currentUser.getUid();
                    current_user_db = database.getReference().child("Users").child(currentUID);
                    MealPlanModel newMealplanEntry = new MealPlanModel();
                    newMealplanEntry.setName(label);
                    newMealplanEntry.setImageUrl(img_url);
                    newMealplanEntry.setIngredients(ingredients);
                    // add to current pending mealplan by default
                    DatabaseReference curUserMealplans = current_user_db.child("meal_plans").child("current").child("personal");
                    curUserMealplans.push().setValue(newMealplanEntry);
                }
            }
        });

        Button openList = findViewById(R.id.button3);
        openList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGroceryList();
            }
        });

    }

    public void openGroceryList() {
        Intent intent = new Intent(this, UserActivity.class);
//        intent.putExtra(EXTRA_TEXT, rp);
        intent.putExtra("grocerylistFragment", "OpenPersonalGroceryList");
        startActivity(intent);
    }
}
