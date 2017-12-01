package com.denma.moodtracker.Controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyAlarmReceiver extends BroadcastReceiver {

    public static final int REQUEST_CODE = 50;

    // Triggered by the Alarm at midnight
    public void onReceive(Context context, Intent intent){

        // Get parameters from Broadcast Intent
        int mood = intent.getIntExtra("DailyMood", 3);
        String commentary = intent.getStringExtra("DailyCommentary");

        // Create an Intent for service with Broadcast Intent Parameters
        Intent i = new Intent(context, AutoSaveService.class);
        i.putExtra("DailyMood", mood);
        i.putExtra("DailyCommentary", commentary);
        context.startService(i);
    }
}
