package com.example.waleed.chatappv2.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.waleed.chatappv2.convsUtils.FriendlyMessage;

public class FirebaseDatabaseIntentService extends IntentService {


    public FirebaseDatabaseIntentService(){
        super("FirebaseDatabaseIntentService");

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
       String userName=intent.getStringExtra("User");
        DatabaseSyncTask.syncMessages(this);
        FriendlyMessage getMessageInBackground=DatabaseSyncTask.getSyncMessage(this);
        String senderName=DatabaseSyncTask.getUserName(this);
        String messageText;
        String ImageUrl;
        try {
             messageText=getMessageInBackground.getText();
             ImageUrl=getMessageInBackground.getPhotoUrl();
        } catch (Exception e){
            messageText="kuch ni is me";
            Toast.makeText(this, messageText, Toast.LENGTH_SHORT).show();
            ImageUrl=null;
            Toast.makeText(this, ImageUrl, Toast.LENGTH_SHORT).show();
        }


        if (messageText!=null){
            NotificationsUtils.getNewMesssageNotification(this,userName,messageText);
        } else if (ImageUrl!=null){
            NotificationsUtils.getNewMesssageNotification(this,senderName,"Send New Photo");
        }

    }


}
