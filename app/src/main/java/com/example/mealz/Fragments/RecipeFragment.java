package com.example.mealz.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mealz.Activities.RecipeDetailActivity;
import com.example.mealz.Adapters.SearchRecipeAdapter;
import com.example.mealz.Models.HitsModel;
import com.example.mealz.Models.RecipeModel;
import com.example.mealz.Models.ResponseModel;
import com.example.mealz.R;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecipeFragment extends Fragment implements SearchRecipeAdapter.RecipeImageClickListener {

    private final String TAG = "RecipeFragment.";

    private List<HitsModel> recipeList;
    private ArrayList<String> recipeImages = new ArrayList<>();
    private ArrayList<String> recipeNames = new ArrayList<>();

    private Spinner dietSpinner;
    private Spinner healthSpinner;
    private EditText searchField;
    private AppCompatImageButton searchBtn;
    private ImageButton img;

    private View view;

    private RecipeClickedListener recipeClickedListener;

    // create an interface to pass the recipe object into userActivity then to recipe detail fragment
    public interface RecipeClickedListener {
        void onRecipeSent(RecipeModel rm);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_recipe, container, false);

//        Log.i(TAG, "create meal plan fragment instance.");

        dietSpinner = view.findViewById(R.id.spinner);
        ArrayAdapter<String> dietSelectionAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.diets));
        dietSelectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dietSpinner.setAdapter(dietSelectionAdapter);

        healthSpinner = view.findViewById(R.id.spinner2);
        ArrayAdapter<String> healthSelectionAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.health));
        healthSelectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        healthSpinner.setAdapter(healthSelectionAdapter);

        searchField = view.findViewById(R.id.recipeSearch);
        img = view.findViewById(R.id.SearchedImg2);
        searchBtn = view.findViewById(R.id.searchButton);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchRecipeClicked();
            }
        });


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // context is the UserActivity which should implement RecipeClickedListener
        if(context instanceof RecipeClickedListener){
            recipeClickedListener = (RecipeClickedListener) context;
        }
        else{
            throw new RuntimeException(context.toString()+" must implement RecipeClickedListener.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        recipeClickedListener = null;
    }

    public void searchRecipeClicked(){
        String message;
        String diet = dietSpinner.getSelectedItem().toString();
        System.out.println(diet);
        String health = healthSpinner.getSelectedItem().toString();
        System.out.println(health);
        String recipe = searchField.getText().toString();
        System.out.println(recipe);

        if (diet.equals("none") && health.equals("none")) {
            message = "&q=" + recipe;
        } else if (diet.equals("none") && !health.equals("none")) {
            message = "&q=" + recipe + "&health=" + health;
        } else if (health.equals("none") && !diet.equals("none")) {
            message = "&q=" + recipe + "&diet=" + diet;
        } else {
            message = "&q=" + recipe + "&diet=" + diet + "&health=" + health;
        }
        System.out.println(message);
        searchForRecipe(message);
    }


    public void searchForRecipe(String message){
        // Do something in response to button
//        EditText editText = (EditText) findViewById(R.id.editText);
//        String message = editText.getText().toString();

        String url = "https://api.edamam.com/search?app_id=736dba64&app_key=8b9c2a666b2c005a8c34b35a26063330" + message;

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

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
        final ImageButton[] img = new ImageButton[3];
        img[0] = view.findViewById(R.id.SearchedImg1);
        img[1] = view.findViewById(R.id.SearchedImg2);
        img[2] = view.findViewById(R.id.SearchedImg3);

        TextView[] label = new TextView[3];
        label[0] = view.findViewById(R.id.label1);
        label[1] = view.findViewById(R.id.label2);
        label[2] = view.findViewById(R.id.label3);

        recipeImages.clear();
        recipeNames.clear();

        for(int i=0; i<recipeList.size(); i++){
            recipeImages.add(recipeList.get(i).getRecipe().getImage());
            recipeNames.add(recipeList.get(i).getRecipe().getLabel());
        }

        if (recipeList.size() > 0) {

            Log.d(TAG, "displayRecipe: ");
            RecyclerView recipes = view.findViewById(R.id.search_recipe_recyclerview);
            SearchRecipeAdapter adapter = new SearchRecipeAdapter(getActivity(), this, recipeImages, recipeNames);
            recipes.setAdapter(adapter);
            recipes.setLayoutManager(new LinearLayoutManager(getActivity()));

//            for (int i=0 ; i<3; ++i) {
//                String img_url = recipeList.get(i).getRecipe().getImage();
//                if (!img_url.equalsIgnoreCase(""))
//                    Picasso.get().load(img_url).placeholder(R.drawable.ic_launcher_background)// Place holder image from drawable folder
//                            .error(R.drawable.b).resize(400, 400).centerCrop()
//                            .into(img[i]);
//                String label_text = recipeList.get(i).getRecipe().getLabel();
//                label[i].setText(label_text);
//                final int j=i;
//                img[i].setOnClickListener(new View.OnClickListener() {
//                    public void onClick(View v) {
//                        recipeImageClicked(j);
//                    }
//                });
//            }

        }
    }

    @Override
    public void onRecipeImageClick(int position) {
        Log.d(TAG, "onRecipeImageClick: "+position);
        recipeImageClicked(position);
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
        Intent myIntent = new Intent(getActivity(), RecipeDetailActivity.class);
//        System.out.println(recipeList.get(j).getRecipe().getIngredients());
        RecipeModel recipe_detail = recipeList.get(j).getRecipe();
        myIntent.putExtra("recipe",(Serializable)recipe_detail);
        startActivity(myIntent);

    }
}