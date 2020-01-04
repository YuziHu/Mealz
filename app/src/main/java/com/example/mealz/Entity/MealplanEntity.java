package com.example.mealz.Entity;

import com.example.mealz.Models.IngredientModel;

import java.util.List;

public class MealplanEntity {
    String mId;
    String name;
    String cookingTime;
    Long calories;
    Long servingSize;
    String imageUrl;
    List<IngredientModel> ingredients;

    public MealplanEntity(){}

    public MealplanEntity(String mId, String name, String cookingTime, Long calories, Long servingSize, String imageUrl, List<IngredientModel> ingredients) {
        this.mId = mId;
        this.name = name;
        this.cookingTime = cookingTime;
        this.calories = calories;
        this.servingSize = servingSize;
        this.imageUrl = imageUrl;
        this.ingredients = ingredients;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
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

    public Long getCalories() {
        return calories;
    }

    public void setCalories(Long calories) {
        this.calories = calories;
    }

    public Long getServingSize() {
        return servingSize;
    }

    public void setServingSize(Long servingSize) {
        this.servingSize = servingSize;
    }

    public List<IngredientModel> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientModel> ingredients) {
        this.ingredients = ingredients;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

