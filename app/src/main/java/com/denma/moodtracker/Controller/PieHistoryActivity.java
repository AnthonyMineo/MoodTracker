package com.denma.moodtracker.Controller;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.widget.Switch;

import com.denma.moodtracker.Model.DailyMood;
import com.denma.moodtracker.Model.DailyMoodDAO;
import com.denma.moodtracker.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.List;

import static com.denma.moodtracker.Model.DailyMoodDAO.KEY_ID_MOOD;
import static com.denma.moodtracker.Model.DailyMoodDAO.KEY_MOOD_COMMENTARY;
import static com.denma.moodtracker.Model.DailyMoodDAO.KEY_MOOD_STAT;

public class PieHistoryActivity extends AppCompatActivity {

    private PieChart mPieChart;
    private int mSuperHappyNumber = 0;
    private int mHappyNumber = 0;
    private int mNormalNumber = 0;
    private int mDisappointedNumber = 0;
    private int mSadNumber = 0;
    private int mTotalDay = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_history);

        mPieChart = (PieChart) findViewById(R.id.activity_piehistory_chart);

        //Set chart style
        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setHoleRadius(20);
        mPieChart.setTransparentCircleRadius(25);
        mPieChart.setEntryLabelColor(Color.parseColor("#000000"));
        //mPieChart.setUsePercentValues(true);
        mPieChart.setEntryLabelTextSize(15);
        mPieChart.setNoDataText("No data yet");
        mPieChart.setNoDataTextColor(Color.parseColor("#000000"));

        //Create array for colors add data that the pie will use
        ArrayList<Integer> colors = new ArrayList<Integer>();
        List<PieEntry> entries = new ArrayList<>();

        //Open database + connect with DAO
        DailyMoodDAO dM = new DailyMoodDAO(this);
        dM.open();

        //get all last mood and increment mood state number from what the database gave to us
        Cursor cursor = dM.getAllDailyMood();
        if (cursor.moveToFirst()) {
            do {
                String moodState = cursor.getString(cursor.getColumnIndex(KEY_MOOD_STAT));
                switch(moodState) {
                    case ":D":
                        mSuperHappyNumber++;
                        break;
                    case ":)":
                        mHappyNumber++;
                        break;
                    case ":|":
                        mNormalNumber++;
                        break;
                    case ":/":
                        mDisappointedNumber++;
                        break;
                    case ":(":
                        mSadNumber++;
                        break;
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        dM.close();

        if(mSuperHappyNumber != 0){
            entries.add(new PieEntry(mSuperHappyNumber, "Super Happy"));
            colors.add(getResources().getColor(R.color.banana_yellow));

        }
        if(mHappyNumber != 0){
            entries.add(new PieEntry(mHappyNumber, "Happy"));
            colors.add(getResources().getColor(R.color.light_sage));
        }
        if(mNormalNumber != 0){
            entries.add(new PieEntry(mNormalNumber, "Normal"));
            colors.add(getResources().getColor(R.color.cornflower_blue_65));
        }
        if(mDisappointedNumber != 0){
            entries.add(new PieEntry(mDisappointedNumber, "Disappointed"));
            colors.add(getResources().getColor(R.color.warm_grey));
        }
        if(mSadNumber != 0){
            entries.add(new PieEntry(mSadNumber, "Sad"));
            colors.add(getResources().getColor(R.color.faded_red));
        }

        PieDataSet pieDataSet = new PieDataSet(entries, "");
        pieDataSet.setSliceSpace(3);
        pieDataSet.setValueTextSize(15);
        //pieDataSet.setValueFormatter(new PercentFormatter());

        if(!colors.isEmpty())
            pieDataSet.setColors(colors);

        PieData data = new PieData(pieDataSet);
        if(!entries.isEmpty())
            mPieChart.setData(data);

        mTotalDay = mSuperHappyNumber + mHappyNumber + mNormalNumber + mDisappointedNumber + mSadNumber;
        Description description = new Description();
        if(mTotalDay <= 1){
            description.setText("Total since : " + mTotalDay + " day");
        } else {
            description.setText("Total since : " + mTotalDay + " days");
        }

        description.setTextSize(20);

        mPieChart.setDescription(description);

        mPieChart.invalidate(); // refresh
    }
}
