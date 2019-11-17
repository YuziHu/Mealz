package com.example.mealz.Models;

import java.io.Serializable;

public class IngredientModel implements Serializable {
    private String text;
    private float weight;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}
