package com.denma.moodtracker.Controller;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.denma.moodtracker.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyPageAdapter adapter = new MyPageAdapter();
        MyViewPager myPager = (MyViewPager) findViewById(R.id.activity_main_panel_pager);
        myPager.setAdapter(adapter);
        myPager.setCurrentItem(3);

    }
}
