package com.example.mealz.Activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mealz.Fragments.PersonalGrocerylistFragment;
import com.example.mealz.Models.GroceryItem;
import com.example.mealz.Models.IngredientModel;
import com.example.mealz.Models.MealPlanModel;
import com.example.mealz.Models.RecipeModel;
import com.example.mealz.Models.User;
import com.example.mealz.R;

import com.example.mealz.Utils.MySingleton;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class RecipeDetailActivity extends AppCompatActivity {
    private static final String TAG = "RecipeDetailActivity";

    public static String curr = "";
    public static String rp = "";
    public static final String EXTRA_TEXT = "com.example.mealz.example.EXTRA_TEXT";
    //
    List<String> members = new ArrayList<>();
    // firebase objects
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database;
    private DatabaseReference current_user_db;
    // creating firebase messages for notification
    // 1. Notification Channel
    // 2. Notification Builder
    // 3. Notification Manager
    public static final String CHANNEL_ID = "SEND_NOTIFICATION";
    private static final String CHANNEL_NAME = "SEND_PENDING_RECIPE";
    private static final String CHANNEL_DESC = "Pending recipe notification";
    // FCM messages
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAA2_F3Uto:APA91bEwAg8SEWXS3svPUGomRZyHVvz2tzbLlstcoEF4DTuEL9fEtKHvocWaoyjouDo_c-I73ebjm4yoQpcXTD9mI8pWLSfG3ILPaHlk0EmyvSEP-o2eSSFrLMTmfaQKSh8fYYCqMLhw";
    final private String contentType = "application/json";
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        currentUser = mAuth.getCurrentUser();

        if(UserActivity.groupID!=null) {
            Log.i(TAG, "onCreate: user group id "+UserActivity.groupID);
            // get list of members
            DatabaseReference curUserGroup = database.getReference().child("Groups").child(UserActivity.groupID).child("members");
            curUserGroup.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Log.i(TAG, "onDataChange: "+ds.getKey());
                        members.add(ds.getKey());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

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
                    if(UserActivity.groupID!=null){
                        DatabaseReference curUserGroupMealplans = database.getReference().child("Groups").child(UserActivity.groupID).child("meal_plans").child("current").child("pending");
                        curUserGroupMealplans.push().setValue(newMealplanEntry);
                    }
                    // local test for showing notification
//                    displayNotification();
                    // send notification
                    if(members != null) {
                        for(String member : members){
                            Log.i(TAG, "onClick: members"+member);
                            if(!member.equals(currentUID)){
                                String to = "/topics/"+member;
                                JSONObject notification = new JSONObject();
                                JSONObject notificationBody = new JSONObject();
                                try {
                                    notificationBody.put("title", "New Pending Meal");
                                    notificationBody.put("message", "Your roommate just added a new meal!");
                                    notification.put("to", to);
                                    notification.put("data", notificationBody);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                sendNotification(notification);
                            }
                        }
                    }
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

        // notification
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if(task.isSuccessful()){
                            String token = task.getResult().getToken();
                        }
                        else{
                            Log.e(TAG, "onComplete: Error" + task.getException().getMessage());
                        }
                    }
                });


    }

    private void sendNotification(JSONObject notification){
        JsonObjectRequest req = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "onErrorResponse: " + error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        requestQueue.add(req);
    }

    public void openGroceryList() {
        Intent intent = new Intent(this, UserActivity.class);
//        intent.putExtra(EXTRA_TEXT, rp);
        intent.putExtra("grocerylistFragment", "OpenPersonalGroceryList");
        startActivity(intent);
    }
}
