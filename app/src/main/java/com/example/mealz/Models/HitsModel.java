package com.example.mealz.Models;

public class HitsModel {

    private RecipeModel recipe;

    public RecipeModel getRecipe() {
        return recipe;
    }

    public void setRecipe(RecipeModel recipe) {
        this.recipe = recipe;
    }

    @Override
    public String toString() {
        return "Hits{" +
                "recipe=" + recipe +
                '}';
    }
}
