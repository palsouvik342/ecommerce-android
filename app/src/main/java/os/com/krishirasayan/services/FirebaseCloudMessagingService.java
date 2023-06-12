package os.com.krishirasayan.services;

import static os.com.krishirasayan.consts.Config.FCM_ACTION_LOGOUT;
import static os.com.krishirasayan.consts.Config.FCM_ACTION_VIEW_HOME;
import static os.com.krishirasayan.consts.Config.NOTIFICATION_BROADCAST_ACTION;
import static os.com.krishirasayan.consts.Helper.d;
import static os.com.krishirasayan.consts.Helper.getNotificationIntent;
import static os.com.krishirasayan.consts.Helper.logout;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Objects;

import os.com.krishirasayan.R;
import os.com.krishirasayan.activities.AuthActivity;
import os.com.krishirasayan.classes.UserData;

public class FirebaseCloudMessagingService extends FirebaseMessagingService {

    final int DEFAULT_NOTIFICATION_ID = 0;
    final String TAG = "LOGZ";
    final String NOTIFICATION_CHANNEL_ID = "FTN2020";
    final String NOTIFICATION_CHANNEL_NAME = "FeedThyNeighbor Notification";
    final String NOTIFICATION_CHANNEL_DESCRIPTION = "Firebase Cloud Messaging Notification Channel";

   /* @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        // Get updated InstanceID token.
        String newToken = FirebaseInstanceId.getInstance().getToken();
        saveFcmToken(newToken, this);
    }*/

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(NOTIFICATION_BROADCAST_ACTION);
        broadcastIntent.putExtra("hasNotification", true);
        sendBroadcast(broadcastIntent);

        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        d("FCM/From: " + remoteMessage.getFrom());
        d("FCM/Title: " + remoteMessage.getNotification().getTitle());
        d("FCM/Body: " + remoteMessage.getNotification().getBody());
        d("FCM/Icon: " + remoteMessage.getNotification().getIcon());
        d("FCM/Action: " + remoteMessage.getData().get("action"));
        d("FCM/ID: " + remoteMessage.getData().get("id"));
        d("FCM/clickAction: " + remoteMessage.getNotification().getClickAction());

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
//            showNotification(remoteMessage);
            sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getClickAction(), remoteMessage.getData());
            //showNotification(remoteMessage);
        }

        String action = remoteMessage.getData().containsKey("action") ? remoteMessage.getData().get("action") : "";
        String id = remoteMessage.getData().containsKey("id") ? remoteMessage.getData().get("id") : "";

        try {
            d("FCM/TRIGGER: onMessageReceived");
            Intent intent;
            switch (Objects.requireNonNull(action)) {
                // force logout right away
                case FCM_ACTION_LOGOUT:
                    logout(this);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        showNotification(remoteMessage);
    }

    private void sendNotification(String title, String messageBody, String clickAction, Map<String, String> data)
    {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_ID);

        Intent intent = new Intent(clickAction);

        for(Map.Entry<String, String> entry : data.entrySet()) {
            intent.putExtra(entry.getKey(), entry.getValue());
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(title);
        bigText.setBigContentTitle(messageBody);

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.drawable.ic_launcher_foreground);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(messageBody);
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setAutoCancel(true);
        mBuilder.setStyle(bigText);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
        }

        mNotificationManager.notify(0, mBuilder.build());
    }

    private void showNotification(RemoteMessage remoteMessage) {

        createNotificationChannel();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
//                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody());

        if (Build.VERSION.SDK_INT >= 21) builder.setVibrate(new long[0]);

        try {
            Intent intent = null;
            String fcm_action = "";
            String fcm_id = "";

            fcm_action = remoteMessage.getData().get("action");
            fcm_id = remoteMessage.getData().get("id");

            intent = getNotificationIntent(fcm_action, fcm_id, this);

            if(intent != null) {
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                builder.setContentIntent(pendingIntent);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        int notificationId = DEFAULT_NOTIFICATION_ID;
        notificationManager.notify(notificationId, builder.build());
    }

    private void createNotificationChannel() {
        /*
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            channel.setDescription(CHANNEL_DESCRIPTION);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            Log.i(TAG, "Notification Channel set");
        }*/


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = NOTIFICATION_CHANNEL_NAME;
            String description = NOTIFICATION_CHANNEL_DESCRIPTION;
            int importance = NotificationManager.IMPORTANCE_HIGH; //Important for heads-up notification
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.setShowBadge(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


}
