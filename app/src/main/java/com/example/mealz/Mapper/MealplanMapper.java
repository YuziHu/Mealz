package com.example.mealz.Mapper;

import com.example.mealz.Entity.MealplanEntity;
import com.example.mealz.Models.MealPlanModel;

import org.w3c.dom.Entity;

public class MealplanMapper extends FirebaseMapper<MealplanEntity, MealPlanModel> {
    @Override
    public MealPlanModel map(MealplanEntity mealplanEntity){
        MealPlanModel mealplan = new MealPlanModel();
        mealplan.setImageUrl(mealplanEntity.getImageUrl());
        mealplan.setName(mealplanEntity.getName());
        mealplan.setIngredients(mealplanEntity.getIngredients());
        return mealplan;
    }
}
