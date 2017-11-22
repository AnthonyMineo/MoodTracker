package com.denma.moodtracker.Controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.denma.moodtracker.R;

public class MainActivity extends AppCompatActivity {

    private ImageView mHistoryBlack;
    private ImageView mNoteAddBlack;
    private FrameLayout mRootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyPagerAdapter adapter = new MyPagerAdapter();
        MyViewPager myPager = (MyViewPager) findViewById(R.id.activity_main_panel_pager);
        myPager.setAdapter(adapter);
        myPager.setCurrentItem(3);

        mRootLayout = (FrameLayout) findViewById(R.id.acitvity_main_root_layout);
        mHistoryBlack = (ImageView) findViewById(R.id.activity_main_history_black);
        mNoteAddBlack = (ImageView) findViewById(R.id.activity_main_note_add_black);

        //define screen dimension (width and height)
        Display display = getWindowManager().getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();

        //define icone dimension relative to the screen
        int iconeWidth = (int) (screenWidth * 0.10);
        int iconeHeight = (int) (screenHeight * 0.10);

        //define icone placement relative to the screen
        int historyLeftMargin = (int) (screenWidth * 0.85);
        int historyTopMargin = (int) (screenHeight * 0.85);
        int noteLeftMargin = (int) (screenWidth * 0.05);
        int noteTopMargin = (int) (screenHeight * 0.85);

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

    }
}
