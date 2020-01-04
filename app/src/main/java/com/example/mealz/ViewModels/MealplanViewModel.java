package com.example.mealz.ViewModels;

import android.util.Log;

import com.example.mealz.Models.MealPlanModel;
import com.example.mealz.Repositories.FirebaseDatabaseRepo;
import com.example.mealz.Repositories.MealplanRepo;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MealplanViewModel extends ViewModel {

    private static final String TAG = "MealplanViewModel";

    private MutableLiveData<List<MealPlanModel>> mMealplans;
    private MealplanRepo repo = new MealplanRepo();

    public LiveData<List<MealPlanModel>> getMealplans(){
        if(mMealplans==null){
            mMealplans = new MutableLiveData<>();
            loadMealplans();
        }
        return mMealplans;
    }

    @Override
    protected void onCleared(){
        repo.removeListener();
    }

    private void loadMealplans(){
        repo.addListener(new FirebaseDatabaseRepo.FirebaseDatabaseRepoCallback<MealPlanModel>() {
            @Override
            public void onSuccess(List<MealPlanModel> result) {
                Log.d(TAG, "onSuccess: "+result.get(0).getName());
                mMealplans.setValue(result);
            }

            @Override
            public void onError(Exception e) {
                mMealplans.setValue(null);
            }
        });
    }
}