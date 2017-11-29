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
    private int superHappyNumber = 0;
    private int happyNumber = 0;
    private int normalNumber = 0;
    private int disappointedNumber = 0;
    private int sadNumber = 0;

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
        mPieChart.setUsePercentValues(true);
        mPieChart.setEntryLabelTextSize(15);
        mPieChart.setNoDataText("No data yet");

        Description description = new Description();
        description.setText("Global Mood");
        description.setTextSize(20);

        mPieChart.setDescription(description);

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
                        superHappyNumber++;
                        break;
                    case ":)":
                        happyNumber++;
                        break;
                    case ":|":
                        normalNumber++;
                        break;
                    case ":/":
                        disappointedNumber++;
                        break;
                    case ":(":
                        sadNumber++;
                        break;
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        dM.close();

        if(superHappyNumber != 0){
            entries.add(new PieEntry(superHappyNumber, "Super Happy"));
            colors.add(getResources().getColor(R.color.banana_yellow));

        }
        if(happyNumber != 0){
            entries.add(new PieEntry(happyNumber, "Happy"));
            colors.add(getResources().getColor(R.color.light_sage));
        }
        if(normalNumber != 0){
            entries.add(new PieEntry(normalNumber, "Normal"));
            colors.add(getResources().getColor(R.color.cornflower_blue_65));
        }
        if(disappointedNumber != 0){
            entries.add(new PieEntry(disappointedNumber, "Disappointed"));
            colors.add(getResources().getColor(R.color.warm_grey));
        }
        if(sadNumber != 0){
            entries.add(new PieEntry(sadNumber, "Sad"));
            colors.add(getResources().getColor(R.color.faded_red));
        }

        PieDataSet pieDataSet = new PieDataSet(entries, "");
        pieDataSet.setSliceSpace(3);
        pieDataSet.setValueTextSize(15);
        pieDataSet.setValueFormatter(new PercentFormatter());

        if(!colors.isEmpty())
            pieDataSet.setColors(colors);

        PieData data = new PieData(pieDataSet);
        mPieChart.setData(data);

        mPieChart.invalidate(); // refresh
    }
}
