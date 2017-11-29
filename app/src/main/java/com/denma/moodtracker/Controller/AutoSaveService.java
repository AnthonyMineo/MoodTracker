package com.denma.moodtracker.Controller;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
        System.out.println("onStart");

        String commentary = "";
        int mood = intent.getIntExtra("DailyMood", 3);
        if(intent.getStringExtra("DailyCommentary") != null) {
            commentary = intent.getStringExtra("DailyCommentary");
        }

        System.out.println("mood : " + mood);
        System.out.println("com : " + commentary);

        //SQLite Bdd test
        DailyMoodDAO dM = new DailyMoodDAO(this);
        dM.open();

        if(mood == 4){
            dM.addDailyMood(new DailyMood(0, ":D", commentary));
        }else if(mood == 3){
            dM.addDailyMood(new DailyMood(0, ":)", commentary));
        }else if(mood == 2){
            dM.addDailyMood(new DailyMood(0, ":|", commentary));
        }else if(mood == 1){
            dM.addDailyMood(new DailyMood(0, ":/", commentary));
        }else if(mood == 0){
            dM.addDailyMood(new DailyMood(0, ":(", commentary));
        }

        dM.close();

        //Remove Prefs for new commentary
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().clear().apply();

        //Send notif to user
        sendNotification();

        this.stopSelf();

        return START_STICKY;
    }

    public void onTaskRemoved(Intent rootIntent){
        System.out.print("taskRemoved");

        Intent restartServiceTask = new Intent(getApplicationContext(),this.getClass());
        restartServiceTask.setPackage(getPackageName());
        PendingIntent restartPendingIntent = PendingIntent.getService(getApplicationContext(), MyAlarmReceiver.REQUEST_CODE, restartServiceTask, PendingIntent.FLAG_ONE_SHOT);

        //AlarmTest
        long alarmUp = 0;
        Calendar midnight = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        midnight.set(Calendar.HOUR_OF_DAY, 23);
        midnight.set(Calendar.MINUTE, 59);
        midnight.set(Calendar.SECOND, 59);

        if(midnight.getTimeInMillis() <= now.getTimeInMillis())
            alarmUp = midnight.getTimeInMillis() + (AlarmManager.INTERVAL_DAY+1);
        else
            alarmUp = midnight.getTimeInMillis();

        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        //init Alarm at midnight
        alarm.set(AlarmManager.RTC_WAKEUP, alarmUp, restartPendingIntent);

        super.onTaskRemoved(rootIntent);
    }

    private void sendNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Save Done !")
                .setContentText("Your daily mood has been saved");
        int NOTIFICATION_ID = 1;

        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(NOTIFICATION_ID, builder.build());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
