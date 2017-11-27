package com.denma.moodtracker.Controller;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.denma.moodtracker.Model.DailyMood;
import com.denma.moodtracker.Model.DailyMoodDAO;
import com.denma.moodtracker.R;

import java.util.Calendar;

public class AutoSaveService extends IntentService {

    public AutoSaveService(){
        super("test");
    }

    public AutoSaveService(String name) {
        super(name);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        System.out.print("onStart");
        System.out.println("AutoSaveOnHandle");

        int mood = intent.getIntExtra("DailyMood", 3);
        String commentary = intent.getStringExtra("DailyCommentary");
        System.out.println("mood : " + mood);
        System.out.println("com : " + commentary);

        //SQLite Bdd test
        DailyMoodDAO testDB = new DailyMoodDAO(this);
        testDB.open();

        if(mood == 4){
            testDB.addDailyMood(new DailyMood(0, ":D", commentary));
        }else if(mood == 3){
            testDB.addDailyMood(new DailyMood(0, ":)", commentary));
        }else if(mood == 2){
            testDB.addDailyMood(new DailyMood(0, ":|", commentary));
        }else if(mood == 1){
            testDB.addDailyMood(new DailyMood(0, ":/", commentary));
        }else if(mood == 0){
            testDB.addDailyMood(new DailyMood(0, ":(", commentary));
        }
        testDB.close();

        return START_STICKY;
    }

    public void onTaskRemoved(Intent rootIntent){
        System.out.print("taskRemoved");
        Intent restartServiceTask = new Intent(getApplicationContext(),this.getClass());
        restartServiceTask.setPackage(getPackageName());
        PendingIntent restartPendingIntent = PendingIntent.getService(getApplicationContext(), MyAlarmReceiver.REQUEST_CODE,restartServiceTask, PendingIntent.FLAG_ONE_SHOT);

        //AlarmTest
        Calendar midnight = Calendar.getInstance();
        midnight.set(Calendar.HOUR_OF_DAY, 00);
        midnight.set(Calendar.MINUTE, 00);
        midnight.set(Calendar.SECOND, 00);

        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        //init Alarm at midnight
        alarm.set(AlarmManager.RTC_WAKEUP, midnight.getTimeInMillis(), restartPendingIntent);

        super.onTaskRemoved(rootIntent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
