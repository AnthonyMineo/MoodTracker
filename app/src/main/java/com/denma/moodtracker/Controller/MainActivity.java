package com.denma.moodtracker.Controller;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    private ImageView mHistoryBlack;
    private ImageView mNoteAddBlack;
    private FrameLayout mRootLayout;
    private MyViewPager myPager;

    private static final String PREF_COMMENTARY = "PREF_COMMENTARY";
    private SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyPagerAdapter adapter = new MyPagerAdapter();
        myPager = (MyViewPager) findViewById(R.id.activity_main_panel_pager);
        myPager.setAdapter(adapter);
        myPager.setCurrentItem(3);

        mRootLayout = (FrameLayout) findViewById(R.id.acitvity_main_root_layout);
        mHistoryBlack = (ImageView) findViewById(R.id.activity_main_history_black);
        mNoteAddBlack = (ImageView) findViewById(R.id.activity_main_note_add_black);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);


        myPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                scheduleAlarm();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //define screen dimension (width and height)
        Display display = getWindowManager().getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();

        //define icone dimension relative to the screen
        int iconeWidth = (int) (screenWidth * 0.10);
        int iconeHeight = (int) (screenHeight * 0.10);

        //define icone placement relative to the screen
        int historyLeftMargin = (int) (screenWidth * 0.80);
        int historyTopMargin = (int) (screenHeight * 0.80);
        int noteLeftMargin = (int) (screenWidth * 0.10);
        int noteTopMargin = (int) (screenHeight * 0.80);

        //set history's icone placement absolute
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(iconeWidth, iconeHeight);
        params.leftMargin = historyLeftMargin;
        params.topMargin  = historyTopMargin;
        mRootLayout.updateViewLayout(mHistoryBlack, params);

        //set note's icone placement absolute
        FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(iconeWidth, iconeHeight);
        params2.leftMargin = noteLeftMargin;
        params2.topMargin  = noteTopMargin;
        mRootLayout.updateViewLayout(mNoteAddBlack, params2);

        //SQLite Bdd test
        //DailyMoodDAO testDB = new DailyMoodDAO(this);
        //testDB.open();


        /*testDB.addDailyMood(new DailyMood(0, ":(", "c'est à l'envers"));
        testDB.addDailyMood(new DailyMood(0, ":|", "J'ai encore mal aux dents"));
        testDB.addDailyMood(new DailyMood(0, ":/", ""));
        testDB.addDailyMood(new DailyMood(0, ":(", "J'ai pas encore vue l'épisode de RWBY !"));
        testDB.addDailyMood(new DailyMood(0, ":)", "mais j'ai bien avancé"));
        testDB.addDailyMood(new DailyMood(0, ":|", ""));
        testDB.addDailyMood(new DailyMood(0, ":D", "Enfin ca fonctionne !"));


        testDB.supDailyMood(new DailyMood(0, "", ""));
        testDB.supDailyMood(new DailyMood(1, "", ""));
        testDB.supDailyMood(new DailyMood(2, "", ""));
        testDB.supDailyMood(new DailyMood(3, "", ""));
        testDB.supDailyMood(new DailyMood(4, "", ""));
        testDB.supDailyMood(new DailyMood(5, "", ""));
        testDB.supDailyMood(new DailyMood(6, "", ""));
        testDB.supDailyMood(new DailyMood(7, "", ""));*/

        mHistoryBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent historyActivityIntent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(historyActivityIntent);
            }
        });

        mNoteAddBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setMessage("Commentaire");

                final EditText comInput = new EditText(MainActivity.this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                comInput.setLayoutParams(layoutParams);
                alertDialog.setView(comInput);

                //If a daily commentary is already set, add it to the EditText
                if(mPreferences.contains(PREF_COMMENTARY))
                    comInput.setText(mPreferences.getString(PREF_COMMENTARY, ""));

                alertDialog.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Add the commentary to shared prefs
                        mPreferences.edit().putString(PREF_COMMENTARY, comInput.getText().toString()).apply();
                        scheduleAlarm();
                        System.out.println(comInput.getText().toString());
                    }

                });

                alertDialog.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }

                });

                alertDialog.create().show();

            }
        });
    }

    private void scheduleAlarm(){

        System.out.println("scheduleAlarm");

        // Construct an intent that will execute MyAlarmReceiver
        Intent myIntent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
        myIntent.putExtra("DailyMood", myPager.getCurrentItem());
        myIntent.putExtra("DailyCommentary", mPreferences.getString(PREF_COMMENTARY, ""));

        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(MainActivity.this, MyAlarmReceiver.REQUEST_CODE, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //AlarmTest
        Calendar midnight = Calendar.getInstance();
        midnight.set(Calendar.HOUR_OF_DAY, 16);
        midnight.set(Calendar.MINUTE, 28);
        midnight.set(Calendar.SECOND, 01);

        //Create AlarmManager Object
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        //init Alarm at midnight
        alarm.set(AlarmManager.RTC_WAKEUP, midnight.getTimeInMillis(), pIntent);
    }
}
