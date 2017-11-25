package com.denma.moodtracker.Controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.denma.moodtracker.R;

public class HistoryActivity extends AppCompatActivity {

    private RelativeLayout mDay7Layout;
    private TextView mDay7Text;
    private TextView mDay7Mood;
    private ImageView mDay7Commentary;

    private RelativeLayout mDay6Layout;
    private TextView mDay6Text;
    private TextView mDay6Mood;
    private ImageView mDay6Commentary;

    private RelativeLayout mDay5Layout;
    private TextView mDay5Text;
    private TextView mDay5Mood;
    private ImageView mDay5Commentary;

    private RelativeLayout mDay4Layout;
    private TextView mDay4Text;
    private TextView mDay4Mood;
    private ImageView mDay4Commentary;

    private RelativeLayout mDay3Layout;
    private TextView mDay3Text;
    private TextView mDay3Mood;
    private ImageView mDay3Commentary;

    private RelativeLayout mDay2Layout;
    private TextView mDay2Text;
    private TextView mDay2Mood;
    private ImageView mDay2Commentary;

    private RelativeLayout mDay1Layout;
    private TextView mDay1Text;
    private TextView mDay1Mood;
    private ImageView mDay1Commentary;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mDay7Layout = (RelativeLayout) findViewById(R.id.activity_history_day_7_layout);
        mDay7Text = (TextView) findViewById(R.id.activity_history_day_7_text);
        mDay7Mood = (TextView) findViewById(R.id.activity_history_day_7_mood);
        mDay7Commentary = (ImageView) findViewById(R.id.activity_history_day_7_commentary);

        mDay6Layout = (RelativeLayout) findViewById(R.id.activity_history_day_6_layout);
        mDay6Text = (TextView) findViewById(R.id.activity_history_day_6_text);
        mDay6Mood = (TextView) findViewById(R.id.activity_history_day_6_mood);
        mDay6Commentary = (ImageView) findViewById(R.id.activity_history_day_6_commentary);

        mDay5Layout = (RelativeLayout) findViewById(R.id.activity_history_day_5_layout);
        mDay5Text = (TextView) findViewById(R.id.activity_history_day_5_text);
        mDay5Mood = (TextView) findViewById(R.id.activity_history_day_5_mood);
        mDay5Commentary = (ImageView) findViewById(R.id.activity_history_day_5_commentary);


        mDay4Layout = (RelativeLayout) findViewById(R.id.activity_history_day_4_layout);
        mDay4Text = (TextView) findViewById(R.id.activity_history_day_4_text);
        mDay4Mood = (TextView) findViewById(R.id.activity_history_day_4_mood);
        mDay4Commentary = (ImageView) findViewById(R.id.activity_history_day_4_commentary);

        mDay3Layout = (RelativeLayout) findViewById(R.id.activity_history_day_3_layout);
        mDay3Text = (TextView) findViewById(R.id.activity_history_day_3_text);
        mDay3Mood = (TextView) findViewById(R.id.activity_history_day_3_mood);
        mDay3Commentary = (ImageView) findViewById(R.id.activity_history_day_3_commentary);

        mDay2Layout = (RelativeLayout) findViewById(R.id.activity_history_day_2_layout);
        mDay2Text = (TextView) findViewById(R.id.activity_history_day_2_text);
        mDay2Mood = (TextView) findViewById(R.id.activity_history_day_2_mood);
        mDay2Commentary = (ImageView) findViewById(R.id.activity_history_day_2_commentary);

        mDay1Layout = (RelativeLayout) findViewById(R.id.activity_history_day_1_layout);
        mDay1Text = (TextView) findViewById(R.id.activity_history_day_1_text);
        mDay1Mood = (TextView) findViewById(R.id.activity_history_day_1_mood);
        mDay1Commentary = (ImageView) findViewById(R.id.activity_history_day_1_commentary);

        //petit test
        mDay7Layout.setBackgroundColor(getResources().getColor(R.color.banana_yellow));
        mDay7Commentary.setVisibility(View.INVISIBLE);
    }
}
