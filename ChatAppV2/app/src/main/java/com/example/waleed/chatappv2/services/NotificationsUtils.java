package com.example.waleed.chatappv2.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.example.waleed.chatappv2.ConversationActivity;
import com.example.waleed.chatappv2.R;

public class NotificationsUtils {
 public static final int NEW_MESSAGE_ID=001;
 public static final String NEW_MESSAGE_CHANNEL_ID="007";

 public static void clearAllNotifications(Context context){
     NotificationManager notificationManager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
     notificationManager.cancelAll();
 }

 public static void getNewMesssageNotification(Context context,String userName,String message){
     NotificationManager notificationManager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

     if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
         NotificationChannel notificationChannel = new NotificationChannel(
                 NEW_MESSAGE_CHANNEL_ID,
                 "My Message Notification Channel",
                 NotificationManager.IMPORTANCE_HIGH);
         notificationManager.createNotificationChannel(notificationChannel);
     }

     NotificationCompat.Builder notificationBuilder=
             new NotificationCompat.Builder(context,NEW_MESSAGE_CHANNEL_ID)
             .setColor(ContextCompat.getColor(context,R.color.colorPrimary))
             .setSmallIcon(R.drawable.chat_logo)
             .setLargeIcon(largeIcon(context))
             .setContentTitle(userName)
             .setContentText(message)
             .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
             .setDefaults(Notification.DEFAULT_VIBRATE)
             .setContentIntent(contentIntent(context))
             .setAutoCancel(true);

     if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN
             && Build.VERSION.SDK_INT<Build.VERSION_CODES.O){
         notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
     }

     notificationManager.notify(NEW_MESSAGE_ID,notificationBuilder.build());
 }

    private static PendingIntent contentIntent(Context  context){
        Intent startActivityIntent=new Intent(context, ConversationActivity.class);

        return PendingIntent.getActivity(
                context,
                NEW_MESSAGE_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static Bitmap largeIcon(Context context){
        Resources resources=context.getResources();
        Bitmap largeIcon= BitmapFactory.decodeResource(resources, R.drawable.chat_logo);
        return largeIcon;
    }
}
