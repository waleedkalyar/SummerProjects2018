package com.example.waleed.chatappv3.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartFireChatAtBoot extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context,FireaseChatBackgroundService.class));
    }
}
