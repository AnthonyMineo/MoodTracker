package com.denma.moodtracker.Controller;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.denma.moodtracker.Model.DailyMoodDAO;
import com.denma.moodtracker.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

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

        // Reference view's item
        mPieChart = (PieChart) findViewById(R.id.activity_piehistory_chart);

        // Set chart style
        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setHoleRadius(20);
        mPieChart.setTransparentCircleRadius(25);
        mPieChart.setEntryLabelColor(Color.parseColor("#000000"));
        //mPieChart.setUsePercentValues(true);
        mPieChart.setEntryLabelTextSize(15);
        mPieChart.setNoDataText("No data yet");
        mPieChart.setNoDataTextColor(Color.parseColor("#000000"));

        // Create array for colors + add data for pie display
        ArrayList<Integer> colors = new ArrayList<Integer>();
        List<PieEntry> entries = new ArrayList<>();

        // Open database + connect with DAO
        DailyMoodDAO dM = new DailyMoodDAO(this);
        dM.open();

        // Get all last mood and increment mood state number from what the database gave to us
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

        //Hold the door !... nop bitch, close the door !
        cursor.close();
        dM.close();

        // Dynamically set Entries and colors
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

        // Create a dataSet to separate our data categories
        PieDataSet pieDataSet = new PieDataSet(entries, "");
        pieDataSet.setSliceSpace(3);
        pieDataSet.setValueTextSize(15);
        //pieDataSet.setValueFormatter(new PercentFormatter());

        // Control if color is empty
        if(!colors.isEmpty())
            pieDataSet.setColors(colors);

        // Create Data from our categories
        PieData data = new PieData(pieDataSet);

        // Control if entries are empty -> allow us to display or not the "noDataText"
        if(!entries.isEmpty())
            mPieChart.setData(data);

        // Add description using the total day since user use our app
        mTotalDay = mSuperHappyNumber + mHappyNumber + mNormalNumber + mDisappointedNumber + mSadNumber;
        Description description = new Description();
        if(mTotalDay <= 1){
            description.setText("Total since : " + mTotalDay + " day");
        } else {
            description.setText("Total since : " + mTotalDay + " days");
        }
        description.setTextSize(20);

        // Set the description + refresh the pieChart
        mPieChart.setDescription(description);
        mPieChart.invalidate();
    }
}
