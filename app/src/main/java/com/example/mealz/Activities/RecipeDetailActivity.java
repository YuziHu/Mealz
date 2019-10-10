package com.example.mealz.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mealz.Models.GroceryItem;
import com.example.mealz.Models.RecipeModel;
import com.example.mealz.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {
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

        RecipeModel recipe=(RecipeModel) getIntent().getSerializableExtra("recipeList");

        ImageView img = findViewById(R.id.imageView);

        String img_url = recipe.getImage();

        if (!img_url.equalsIgnoreCase(""))
            Picasso.get().load(img_url).placeholder(R.drawable.ic_launcher_background)// Place holder image from drawable folder
                    .error(R.drawable.b).resize(110, 110).centerCrop()
                    .into(img);

        TextView ingredient = findViewById(R.id.textView2);
        final List<String> foods = recipe.getIngredientLines();
        String result = "";
        for (int i = 0; i < foods.size(); i++) {
            result += foods.get(i);
            System.out.println("foods(i): "+foods.get(i));
            result += "\n\n";
        }

        System.out.println("result: "+result);

        //Intent intent = new Intent(this, GroceryActivity.class);

        ingredient.setText(result);
        curr = result;

        // add ingredients of current food into database
        Button add = findViewById(R.id.button2);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Recipe Added Successfully", Toast.LENGTH_SHORT).show();
                if(currentUser != null){
                    String currentUID = currentUser.getUid();
                    current_user_db = database.getReference().child("Users").child(currentUID);
                    for(String ingredient : foods){
                        GroceryItem newGroceryEntry = new GroceryItem();
                        newGroceryEntry.setName(ingredient);
                        newGroceryEntry.setAmount(-1);
                        newGroceryEntry.setUnit("");
                        DatabaseReference currentUserGroceryList = current_user_db.child("grocery_list");
                        currentUserGroceryList.push().setValue(newGroceryEntry);
                    }
                }
//                rp += curr;
//                System.out.println("rp: "+rp);
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
        Intent intent = new Intent(this, HomeActivity.class);
//        intent.putExtra(EXTRA_TEXT, rp);
        startActivity(intent);
    }
}