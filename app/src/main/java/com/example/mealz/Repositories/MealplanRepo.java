package com.example.mealz.Repositories;

import com.example.mealz.Activities.UserActivity;
import com.example.mealz.Mapper.MealplanMapper;
import com.example.mealz.Models.MealPlanModel;

import java.util.ArrayList;
import java.util.List;

public class MealplanRepo extends FirebaseDatabaseRepo<MealPlanModel> {
    public MealplanRepo(){
        super(new MealplanMapper());
    }


    @Override
    protected List<String> getPath(String uid) {
        List<String> path = new ArrayList<>();
        path.add("Users");
        path.add(UserActivity.currentUID);
        path.add("meal_plans");
        path.add("current");
        path.add("personal");
        return path;
    }
}
