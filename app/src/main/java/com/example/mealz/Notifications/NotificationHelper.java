package com.example.mealz.Notifications;

import android.content.Context;
import android.content.Intent;

import com.example.mealz.Activities.RecipeDetailActivity;
import com.example.mealz.R;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationHelper {

    public static void displayNotification(Context c, String title, String body){

//        Intent intent

        NotificationCompat.Builder nBuilder =
                new NotificationCompat.Builder(c, RecipeDetailActivity.CHANNEL_ID)
                        .setSmallIcon(R.drawable.mealz_logo)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Notification Manager
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(c);
        // notification id is used update/delete notification, ignore it now
        notificationManager.notify(1,nBuilder.build());

    }
}
