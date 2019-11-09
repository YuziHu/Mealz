package com.example.mealz.Models;

import java.util.List;

public class MealPlanModel {
    String name;
    String cookingTime;
    String calories;
    String servingSize;
    List<IngredientModel> ingredients;

    public MealPlanModel(String name, String cookingTime, String calories, String servingSize, List<IngredientModel> ingredients) {
        this.name = name;
        this.cookingTime = cookingTime;
        this.calories = calories;
        this.servingSize = servingSize;
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(String cookingTime) {
        this.cookingTime = cookingTime;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getServingSize() {
        return servingSize;
    }

    public void setServingSize(String servingSize) {
        this.servingSize = servingSize;
    }

    public List<IngredientModel> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientModel> ingredients) {
        this.ingredients = ingredients;
    }
}
