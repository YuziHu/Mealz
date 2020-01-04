package com.example.mealz.ViewModels.MealplanVMs;

import android.util.Log;

import com.example.mealz.Models.MealPlanModel;
import com.example.mealz.Repositories.PendingMealplanRepo;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PendingMealplanViewModel extends ViewModel {
    private static final String TAG = "PendingMealplanViewMode";

    private MutableLiveData<List<MealPlanModel>> mPendingMealplans;
    private PendingMealplanRepo pendingMealplanRepo;

    public void init(){
        if(mPendingMealplans != null){
            return;
        }
        pendingMealplanRepo = PendingMealplanRepo.getInstance();
        mPendingMealplans = pendingMealplanRepo.getPendingMealPlans();
        Log.d(TAG, "init: "+mPendingMealplans.getValue());
    }

    public LiveData<List<MealPlanModel>> getMealplans(){
        return mPendingMealplans;
    }
}
