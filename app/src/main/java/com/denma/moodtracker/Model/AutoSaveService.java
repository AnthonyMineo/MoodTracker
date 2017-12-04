package com.denma.moodtracker.Model;

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

import com.denma.moodtracker.Controller.MyAlarmReceiver;
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
        // Get intent extras
        String commentary = "";
        int mood = intent.getIntExtra("DailyMood", 3);
        if(intent.getStringExtra("DailyCommentary") != null) {
            commentary = intent.getStringExtra("DailyCommentary");
        }

        // Open database + connect with DAO
        DailyMoodDAO dM = new DailyMoodDAO(this);
        dM.open();

        // Adding DailyMood to Database
        switch (mood) {
            case 4:
                dM.addDailyMood(new DailyMood(0, ":D", commentary));
                break;
            case 3:
                dM.addDailyMood(new DailyMood(0, ":)", commentary));
                break;
            case 2:
                dM.addDailyMood(new DailyMood(0, ":|", commentary));
                break;
            case 1:
                dM.addDailyMood(new DailyMood(0, ":/", commentary));
                break;
            case 0:
                dM.addDailyMood(new DailyMood(0, ":(", commentary));
                break;
        }

        // Close the door !
        dM.close();

        // Remove Prefs for new commentary
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().clear().apply();

        // Send notif to user
        sendNotification();

        // Stop the service -> don't save daily mood if user don't launch at least one time the app during the day
        this.stopSelf();

        return START_STICKY;
    }

    // Used if App was killed
    public void onTaskRemoved(Intent rootIntent){
        // Go to the onStartCommand !
        Intent restartServiceTask = new Intent(getApplicationContext(),this.getClass());
        restartServiceTask.setPackage(getPackageName());

        // Create a PendingIntent to be triggered when the alarm goes off
        PendingIntent restartPendingIntent = PendingIntent.getService(getApplicationContext(), MyAlarmReceiver.REQUEST_CODE, restartServiceTask, PendingIntent.FLAG_ONE_SHOT);

        // Define when the alarm will be firing
        long alarmUp = 0;
        Calendar midnight = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        midnight.set(Calendar.HOUR_OF_DAY, 23);
        midnight.set(Calendar.MINUTE, 59);
        midnight.set(Calendar.SECOND, 59);

        // Allow us to know if alarm time is past or not -> don't start it if it's past
        if(midnight.getTimeInMillis() <= now.getTimeInMillis())
            alarmUp = midnight.getTimeInMillis() + (AlarmManager.INTERVAL_DAY+1);
        else
            alarmUp = midnight.getTimeInMillis();

        // Create AlarmManager Object
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        // Set Alarm at midnight
        alarm.set(AlarmManager.RTC_WAKEUP, alarmUp, restartPendingIntent);

        super.onTaskRemoved(rootIntent);
    }

    // Send simple notification withouy any action on click, just informative
    private void sendNotification(){
        String NOTIFICATION_ID = "channel_id_01";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Save Done !")
                .setContentText("Your daily mood has been saved");


        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(1, builder.build());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {}
}
