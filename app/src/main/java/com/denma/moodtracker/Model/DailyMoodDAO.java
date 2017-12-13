package com.denma.moodtracker.Model;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

// Daily Mood Data Access Object
public class DailyMoodDAO {

    //Init our Table
    private static final String TABLE_NAME = "DailyMood";
    public static final String KEY_ID_MOOD = "id_dailyMood";
    public static final String KEY_MOOD_STAT = "dailyMoodStat";
    public static final String KEY_MOOD_COMMENTARY = "dailyCommentary";
    public static final String CREATE_TABLE_DAILYMOOD = "CREATE TABLE " + TABLE_NAME +
            " ( " + KEY_ID_MOOD + " INTEGER primary key, " + KEY_MOOD_STAT + " TEXT, " +
            KEY_MOOD_COMMENTARY + " TEXT );";

    private MySQLite mySQLiteBase;
    private SQLiteDatabase db;

    public DailyMoodDAO(Context context){
        mySQLiteBase = MySQLite.getInstance(context);
    }

    public void open(){
        // Open database read/write mode
        db = mySQLiteBase.getWritableDatabase();
    }

    public void close()
    {
        // Close database
        db.close();
    }

    public long addDailyMood(DailyMood myMood){
        // Init values that you will put into the table
        ContentValues myValues = new ContentValues();
        myValues.put(KEY_MOOD_STAT, myMood.getDailyMood());
        myValues.put(KEY_MOOD_COMMENTARY, myMood.getDailyCommentary());

        // Return the new save id or -1 if an error occur
        return db.insert(TABLE_NAME, null, myValues);
    }

    public int modDailyMood(DailyMood myMood){
        // Init values that you will update into the table
        ContentValues myValues = new ContentValues();
        myValues.put(KEY_MOOD_STAT, myMood.getDailyMood());
        myValues.put(KEY_MOOD_COMMENTARY, myMood.getDailyCommentary());

        // Set where the update will be execute
        String where = KEY_ID_MOOD + " = ?";
        String[] whereArgs = {myMood.getId_dailyMood() + ""};

        return db.update(TABLE_NAME, myValues, where, whereArgs);
    }

    public int supDailyMood(DailyMood myMood) {
        // Set where are the data you want to delete
        String where = KEY_ID_MOOD + " = ?";
        String[] whereArgs = {myMood.getId_dailyMood() + ""};

        return db.delete(TABLE_NAME, where, whereArgs);
    }

    public DailyMood getOneDailyMood(int id){

        DailyMood selectedMood = new DailyMood(0, "", "");

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE "+ KEY_ID_MOOD +
                " = " + id, null);
        if (cursor.moveToFirst()) {
            selectedMood.setId_dailyMood(cursor.getInt(cursor.getColumnIndex(KEY_ID_MOOD)));
            selectedMood.setDailyMood(cursor.getString(cursor.getColumnIndex(KEY_MOOD_STAT)));
            selectedMood.setDailyCommentary(cursor.getString(cursor.getColumnIndex(KEY_MOOD_COMMENTARY)));
            cursor.close();
        }

        return selectedMood;
    }

    // Return example : id = 14 / 13 / 12 / 11 / 10 / 9 / 8 (14 is the last in database)
    public Cursor getLast7DailyMood(){
        return db.rawQuery( "SELECT * FROM " + TABLE_NAME + " ORDER BY " + KEY_ID_MOOD +  " DESC LIMIT 7 ", null);
    }

    public Cursor getAllDailyMood(){
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }
}
