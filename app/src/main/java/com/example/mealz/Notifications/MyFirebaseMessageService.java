package com.example.mealz.Notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.example.mealz.Activities.UserActivity;
import com.example.mealz.Notifications.NotificationHelper;
import com.example.mealz.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import static com.example.mealz.Utils.AppContext.getAppCxt;

public class MyFirebaseMessageService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMessageServic";
    private final String CHANNEL_ID = "Notification channel";
    private static final String SUBSCRIBE_TO = "1LCy6eCWCaaHrn3ciapXuv0Grfh2";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if(remoteMessage != null){
            Log.i(TAG, "onMessageReceived: messageReceived");
            String title = remoteMessage.getData().get("title");
            String msg = remoteMessage.getData().get("message");
//            String title = remoteMessage.getNotification().getTitle();
//            String msg = remoteMessage.getNotification().getBody();
            NotificationHelper.displayNotification(getAppCxt(), title, msg);
        }
//        NotificationManager notificationManager = null;
//        int notificationID = new Random().nextInt(3000);
//        final Intent intent = new Intent(getAppCxt(), UserActivity.class);
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            notificationManager = (NotificationManager) (getAppCxt().getSystemService(Context.NOTIFICATION_SERVICE));
//            setupChannels(notificationManager);
//        }
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(getAppCxt(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
//        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getAppCxt(), CHANNEL_ID)
//                .setSmallIcon(R.drawable.mealz_logo)
//                .setContentTitle(remoteMessage.getData().get("title"))
//                .setContentText(remoteMessage.getData().get("message"))
//                .setAutoCancel(true)
//                .setSound(notificationSound)
//                .setContentIntent(pendingIntent);
//        if(notificationManager!=null){
//            notificationManager.notify(notificationID, notificationBuilder.build());
//        }
    }

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private void setupChannels(NotificationManager notificationManager) {
//        String channelName = "New Notification";
//        String channelDesc = "Device to device notification";
//
//        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_DEFAULT);
//        channel.setDescription(channelDesc);
//        channel.enableVibration(true);
//        channel.enableLights(true);
//        channel.setLightColor(Color.CYAN);
//        if(notificationManager != null){
//            notificationManager.createNotificationChannel(channel);
//        }
//
//    }
}
