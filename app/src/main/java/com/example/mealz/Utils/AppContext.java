package com.example.mealz.Utils;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;

import java.lang.ref.WeakReference;

public class AppContext extends Application {
    private static WeakReference<Context> referenceCxt;

    @Override
    public void onCreate() {
        super.onCreate();
        referenceCxt = new WeakReference<>(getApplicationContext());
    }

    public static Context getAppCxt(){
        return referenceCxt.get();
    }
}
