package com.denma.moodtracker.Controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;

import android.widget.ImageView;
import android.widget.LinearLayout;

import com.denma.moodtracker.R;

public class MainActivity extends AppCompatActivity {
    ImageView mSmileySad;
    ImageView mSmileyDisappointed;
    ImageView mSmileyNormal;
    ImageView mSmileyHappy;
    ImageView mSmileySuperHappy;

    LinearLayout mSmileySadLayout;
    LinearLayout mSmileyDisappointedLayout;
    LinearLayout mSmileyNormalLayout;
    LinearLayout mSmileyHappyLayout;
    LinearLayout mSmileySuperHappyLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSmileySad = (ImageView) findViewById(R.id.activity_main_smiley_sad);
        mSmileyDisappointed = (ImageView) findViewById(R.id.activity_main_smiley_disappointed);
        mSmileyNormal = (ImageView) findViewById(R.id.activity_main_smiley_normal);
        mSmileyHappy = (ImageView) findViewById(R.id.activity_main_smiley_happy);
        mSmileySuperHappy = (ImageView) findViewById(R.id.activity_main_smiley_super_happy);


        mSmileySadLayout = (LinearLayout) findViewById(R.id.activity_main_smiley_sad_layout);
        mSmileyDisappointedLayout = (LinearLayout) findViewById(R.id.activity_main_smiley_disappointed_layout);
        mSmileyNormalLayout = (LinearLayout) findViewById(R.id.activity_main_smiley_normal_layout);
        mSmileyHappyLayout = (LinearLayout) findViewById(R.id.activity_main_smiley_happy_layout);
        mSmileySuperHappyLayout = (LinearLayout) findViewById(R.id.activity_main_smiley_super_happy_layout);

        Display display = getWindowManager().getDefaultDisplay();
        int screenHeight = display.getHeight();

        mSmileySadLayout.getLayoutParams().height = screenHeight;
        mSmileyDisappointedLayout.getLayoutParams().height = screenHeight;
        mSmileyNormalLayout.getLayoutParams().height = screenHeight;
        mSmileyHappyLayout.getLayoutParams().height = screenHeight;
	mSmileySuperHappyLayout.getLayoutParams().height = screenHeight;
    } }
