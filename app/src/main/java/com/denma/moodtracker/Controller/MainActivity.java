package com.denma.moodtracker.Controller;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.denma.moodtracker.R;

import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private ImageView mHistory7Days;
    private ImageView mNoteAdd;
    private ImageView mPieChart;
    private ImageView mShare;

    private FrameLayout mRootLayout;
    private MyViewPager myPager;

    private static final String PREF_COMMENTARY = "PREF_COMMENTARY";
    private SharedPreferences mPreferences;

    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init PagerAdapter with a ViewPager
        MyPagerAdapter adapter = new MyPagerAdapter();
        myPager = (MyViewPager) findViewById(R.id.activity_main_panel_pager);
        myPager.setAdapter(adapter);
        myPager.setCurrentItem(3);

        // Reference view's items
        mRootLayout = (FrameLayout) findViewById(R.id.acitvity_main_root_layout);
        mHistory7Days = (ImageView) findViewById(R.id.activity_main_history);
        mNoteAdd = (ImageView) findViewById(R.id.activity_main_note_add);
        mPieChart = (ImageView) findViewById(R.id.activity_main_pie_chart);
        mShare = (ImageView) findViewById(R.id.activity_main_share);

        // Init SharedPreferences using Default wich make it easily recoverable throught activity/service
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        scheduleAlarm();

        // Define action on ViewPager when user change the current Page
        myPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                // Init + set the sound to play when user switch between Page
                int i = myPager.getCurrentItem();
                setSound(i);

                // Call the Alarm scheduler because the current mood was changed by user -> update the Intent send to the service
                scheduleAlarm();
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        // Define screen dimension (width and height)
        Display display = getWindowManager().getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();

        // Define icone dimension relative to the screen
        int iconeWidth = (int) (screenWidth * 0.10);
        int iconeHeight = (int) (screenHeight * 0.10);

        // Define icone placement relative to the screen
        int historyLeftMargin = (int) (screenWidth * 0.80);
        int historyTopMargin = (int) (screenHeight * 0.80);
        int pieHistoryLeftMargin = (int) (screenWidth * 0.80);
        int pieHistoryTopMargin = (int) (screenHeight * 0.10);
        int noteLeftMargin = (int) (screenWidth * 0.10);
        int noteTopMargin = (int) (screenHeight * 0.80);
        int shareLeftMargin = (int) (screenWidth * 0.10);
        int shareTopMargin = (int) (screenHeight * 0.10);

        // Set history's icone placement absolute
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(iconeWidth, iconeHeight);
        params.leftMargin = historyLeftMargin;
        params.topMargin  = historyTopMargin;
        mRootLayout.updateViewLayout(mHistory7Days, params);

        // Set note's icone placement absolute
        FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(iconeWidth, iconeHeight);
        params2.leftMargin = noteLeftMargin;
        params2.topMargin  = noteTopMargin;
        mRootLayout.updateViewLayout(mNoteAdd, params2);

        // Set pieHistory's icone placement absolute
        FrameLayout.LayoutParams params3 = new FrameLayout.LayoutParams(iconeWidth, iconeHeight);
        params3.leftMargin = pieHistoryLeftMargin;
        params3.topMargin  = pieHistoryTopMargin;
        mRootLayout.updateViewLayout(mPieChart, params3);

        // Set share's icone placement absolute
        FrameLayout.LayoutParams params4 = new FrameLayout.LayoutParams(iconeWidth, iconeHeight);
        params4.leftMargin = shareLeftMargin;
        params4.topMargin  = shareTopMargin;
        mRootLayout.updateViewLayout(mShare, params4);

        // History onClickListener that will start HistoryActivity
        mHistory7Days.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent historyActivityIntent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(historyActivityIntent);
            }
        });

        // PieChart onClickListener that will start PieHistoryActivity
        mPieChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pieHistoryActivityIntent = new Intent(MainActivity.this, PieHistoryActivity.class);
                startActivity(pieHistoryActivityIntent);
            }
        });

        // Share onClickListener that will start an activity with "ACTION_SEND" Intent -> allow user to share is current mood throught different way to who he wants
        mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // String currentMood = the mood state + commentary in a string format, allow us to send it to multiple others apps
                String currentMood = "Today's mood " + convertIntMoodToStringMood(myPager.getCurrentItem()) + " because : " + mPreferences.getString(PREF_COMMENTARY, "");
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, currentMood);

                // setType allow us to informed others apps the type of our send intent and then, with createChooser to display apps that can use this type of intent
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "How do you want to send your mood ?"));
            }
        });

        // Note onClickListener that will display an AlertDialog asking for the user's daily commentary
        mNoteAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setMessage("Commentaire");

                // Create + Adding EditText to the AlertDialog's layout
                final EditText comInput = new EditText(MainActivity.this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                comInput.setLayoutParams(layoutParams);
                alertDialog.setView(comInput);

                // If a daily commentary is already set, add it to the EditText
                if(mPreferences.contains(PREF_COMMENTARY))
                    comInput.setText(mPreferences.getString(PREF_COMMENTARY, ""));

                // Set Positive button
                alertDialog.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Add the commentary to shared prefs
                        mPreferences.edit().putString(PREF_COMMENTARY, comInput.getText().toString()).apply();

                        // Call the Alarm scheduler because the current commentary was changed by user -> update the Intent send to the service
                        scheduleAlarm();
                    }

                });

                // Set Negative button
                alertDialog.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }

                });

                // Create + show the AlertDialog
                alertDialog.create().show();
            }
        });
    }

    // Convert mood get from ViewPager (as an int) to a mood that we can use in DataBase (as a String)
    private String convertIntMoodToStringMood(int mood){
        String currentMood = "";

        switch (mood){
            case 4:
                currentMood = ":D";
                break;
            case 3:
                currentMood = ":)";
                break;
            case 2:
                currentMood = ":|";
                break;
            case 1:
                currentMood = ":/";
                break;
            case 0:
                currentMood = ":(";
                break;
        }

        return currentMood;
    }

    // Shedule when the alarm will be firing and then execute the AutoSaveService
    private void scheduleAlarm(){
        // Construct an intent that will send to MyAlarmReceiver
        Intent myIntent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
        myIntent.putExtra("DailyMood", myPager.getCurrentItem());
        myIntent.putExtra("DailyCommentary", mPreferences.getString(PREF_COMMENTARY, ""));

        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(MainActivity.this, MyAlarmReceiver.REQUEST_CODE, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

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
        alarm.set(AlarmManager.RTC_WAKEUP, alarmUp, pIntent);
    }

    // Method that init parameters of the MediaPlayer and play it
    private void setSound(int smiley){

        switch (smiley){
            case 4:
                try {
                    stopPlayingMedia();
                    AssetFileDescriptor afd = getAssets().openFd("Super_Happy_Sound.mp3");
                    mMediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                    mMediaPlayer.prepare();
                    mMediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    stopPlayingMedia();
                    AssetFileDescriptor afd = getAssets().openFd("Happy_Sound.mp3");
                    mMediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                    mMediaPlayer.prepare();
                    mMediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    stopPlayingMedia();
                    AssetFileDescriptor afd = getAssets().openFd("Normal_Sound.mp3");
                    mMediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                    mMediaPlayer.prepare();
                    mMediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    stopPlayingMedia();
                    AssetFileDescriptor afd = getAssets().openFd("Disappointed_Sound.mp3");
                    mMediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                    mMediaPlayer.prepare();
                    mMediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 0:
                try {
                    stopPlayingMedia();
                    AssetFileDescriptor afd = getAssets().openFd("Sad_Sound.mp3");
                    mMediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                    mMediaPlayer.prepare();
                    mMediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    // Method that "stop" MediaPlayer and create a new instance of it -> allow us to avoid bug when user play a new sound before the last one finished.
    private void stopPlayingMedia(){
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = new MediaPlayer();
        } else {
            mMediaPlayer = new MediaPlayer();
        }
    }
}
