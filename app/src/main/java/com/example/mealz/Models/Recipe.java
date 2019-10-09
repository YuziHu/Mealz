package com.example.mealz.Models;
import java.io.Serializable;
import java.util.List;

public class Recipe implements Serializable {
    public Recipe(){

    }
    public Recipe(String label, String url, List ingredientLines){
        this.label = label;
        this.url = url;
        this.ingredientLines = ingredientLines;
    }


    private String label;
    private String image;
    private String source;
    private String url;
    private List dietLabels;
    private List healthLabels;
    private List cautions;
    private List ingredientLines;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List getDietLabels() {
        return dietLabels;
    }

    public void setDietLabels(List dietLabels) {
        this.dietLabels = dietLabels;
    }

    public List getHealthLabels() {
        return healthLabels;
    }

    public void setHealthLabels(List healthLabels) {
        this.healthLabels = healthLabels;
    }

    public List getCautions() {
        return cautions;
    }

    public void setCautions(List cautions) {
        this.cautions = cautions;
    }

    public List getIngredientLines() {
        return ingredientLines;
    }

    public void setIngredientLines(List ingredientLines) {
        this.ingredientLines = ingredientLines;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "label='" + label + '\'' +
                ", image='" + image + '\'' +
                ", source='" + source + '\'' +
                ", url='" + url + '\'' +
                ", dietLabels=" + dietLabels +
                ", healthLabels=" + healthLabels +
                ", cautions=" + cautions +
                ", ingredientLines=" + ingredientLines +
                '}';
    }
}
