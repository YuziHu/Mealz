package com.example.mealz.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mealz.Activities.RecipeDetailActivity;
import com.example.mealz.Models.HitsModel;
import com.example.mealz.Models.RecipeModel;
import com.example.mealz.Models.ResponseModel;
import com.example.mealz.R;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

//import okhttp3.Response;


public class SearchRecipeActivity extends AppCompatActivity  {

//    private List<Recipe> recipe  = new ArrayList<>(Arrays.asList(
//            new Recipe("meat",
//                    "https://www.jessicagavin.com/wp-content/uploads/2018/06/how-to-reverse-sear-a-steak-11.jpg",
//                    new ArrayList<String>(Arrays.asList("Blood", "Bone", "Cat"))),
//            new Recipe("meat",
//                    "https://www.jessicagavin.com/wp-content/uploads/2018/06/how-to-reverse-sear-a-steak-11.jpg",
//                    new ArrayList<String>(Arrays.asList("Blood", "Bone", "Cat"))),
//            new Recipe("meat",
//                    "https://www.jessicagavin.com/wp-content/uploads/2018/06/how-to-reverse-sear-a-steak-11.jpg",
//                    new ArrayList<String>(Arrays.asList("Blood", "Bone", "Cat")))
//    ));
    private List<HitsModel> recipeList;
    private Spinner dietSpinner;
    private Spinner healthSpinner;
    private EditText searchField;
    private ImageButton img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipe);

        searchField = findViewById(R.id.recipeSearch);
        img = findViewById(R.id.SearchedImg2);

        dietSpinner = findViewById(R.id.spinner);
        ArrayAdapter<String> dietSelectionAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.diets));
        dietSelectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dietSpinner.setAdapter(dietSelectionAdapter);

        healthSpinner = findViewById(R.id.spinner2);
        ArrayAdapter<String> healthSelectionAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.health));
        healthSelectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        healthSpinner.setAdapter(healthSelectionAdapter);

//        String img_url = recipe.get(0).getUrl();

//        if (!img_url.equalsIgnoreCase(""))
//            Picasso.get().load(img_url).placeholder(R.drawable.b)// Place holder image from drawable folder
//                    .error(R.drawable.b).resize(110, 110).centerCrop()
//                    .into(img);
    }


    public void searchRecipeClicked(View buttonView) {
        String recipe = searchField.getText().toString();
        String diet = dietSpinner.getSelectedItem().toString();
        String health = healthSpinner.getSelectedItem().toString();
        String message = "&q=" + recipe + "&diet=" + diet + "&health=" + health;
        searchForRecipe(message);
    }


    public void searchForRecipe(String message){
        // Do something in response to button
//        EditText editText = (EditText) findViewById(R.id.editText);
//        String message = editText.getText().toString();

        String url = "https://api.edamam.com/search?app_id=736dba64&app_key=8b9c2a666b2c005a8c34b35a26063330" + message;
        System.out.println(url);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            recipeList = responseToRecipeList(response);
                            // All following function should be achieved under this function;
                            displayRecipe();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Rest Response", error.toString());
                    }
                }
        );
        requestQueue.add(stringRequest);

    }

    public void displayRecipe(){
        final ImageButton[] img = new ImageButton[5];
        img[0] = findViewById(R.id.SearchedImg1);
        img[1] = findViewById(R.id.SearchedImg2);
        img[2] = findViewById(R.id.SearchedImg3);
        img[3] = findViewById(R.id.SearchedImg4);
//        img[4] = findViewById(R.id.SearchedImg5);


        TextView[] label = new TextView[5];
        label[0] = findViewById(R.id.label1);
        label[1] = findViewById(R.id.label2);
        label[2] = findViewById(R.id.label3);
        label[3] = findViewById(R.id.label4);
//        label[4] = findViewById(R.id.label5);


        if (recipeList.size() > 0) {
            for (int i=0 ; i<4; ++i) { //5
                String img_url = recipeList.get(i).getRecipe().getImage();
                if (!img_url.equalsIgnoreCase(""))
                    Picasso.get().load(img_url).placeholder(R.drawable.ic_launcher_background)// Place holder image from drawable folder
                            .error(R.drawable.b).resize(300, 225).centerCrop()
                            .into(img[i]);
                String label_text = recipeList.get(i).getRecipe().getLabel();
                label[i].setText(label_text);
                final int j=i;
                img[i].setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        recipeImageClicked(j);
                    }
                });
            }

        }


    }

    public List<HitsModel> responseToRecipeList(String response) throws IOException {
//        System.out.println(response);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ResponseModel r = objectMapper.readValue(response, ResponseModel.class);
        this.recipeList = r.getHits();
        return this.recipeList;
    }

    public void recipeImageClicked(int j) {
        Intent myIntent = new Intent(this, RecipeDetailActivity.class);
        System.out.println(recipeList.get(j).getRecipe().getIngredients());
        RecipeModel recipe_detail = recipeList.get(j).getRecipe();
        myIntent.putExtra("recipeList",(Serializable)recipe_detail);
        startActivity(myIntent);

    }

}
