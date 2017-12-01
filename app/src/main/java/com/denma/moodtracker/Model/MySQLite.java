package com.denma.moodtracker.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLite extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "db.sqlite";
    public static final int DATABASE_VERSION = 1;
    private static  MySQLite sInstance;

    private MySQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Our Singleton :)
    public static synchronized MySQLite getInstance(Context context){
        if(sInstance == null)
            sInstance = new MySQLite(context);

        return sInstance;
    }

    public void onCreate(SQLiteDatabase sqliteDb){
        //on Database creation we execute table creation request
        sqliteDb.execSQL(DailyMoodDAO.CREATE_TABLE_DAILYMOOD);
    }

    public void onUpgrade(SQLiteDatabase sqliteDb, int v1, int v2){
        onCreate(sqliteDb);
    }
}
