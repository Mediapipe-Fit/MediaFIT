package com.gauravk.bubblebarsample.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gauravk.bubblebarsample.cfg.Config;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper databaseHelper;

    // All Static variables
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = Config.DATABASE_NAME;

    // Constructor
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    public static synchronized DatabaseHelper getInstance(Context context){
        if(databaseHelper==null){
            databaseHelper = new DatabaseHelper(context);
        }
        return databaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create tables SQL execution
        String CREATE_Routine_TABLE = "CREATE TABLE " + Config.TABLE_Routine + "("
                + Config.COLUMN_Routine_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Config.COLUMN_Exercise_NAME + " TEXT NOT NULL, "
                + Config.COLUMN_Weekday + " TEXT NOT NULL, "
                + Config.COLUMN_RegNO + " INTEGER NOT NULL, "
                + Config.COLUMN_Routine_Set_num + " INTEGER NOT NULL, "
                + Config.COLUMN_Routine_Repeat_num + " INTEGER NOT NULL, "
                + Config.COLUMN_Routine_Rest_time + " INTEGER NOT NULL, "
                + Config.COLUMN_Routine_Counts + " INTEGER NOT NULL, "
                + Config.COLUMN_Routine_Complete + " INTEGER NOT NULL "
                + ")";

        Logger.d("Table create SQL: " + CREATE_Routine_TABLE);

        db.execSQL(CREATE_Routine_TABLE);

        Logger.d("TABLE_Routine created!");

        // Create tables SQL execution
        String CREATE_Calendar_TABLE = "CREATE TABLE " + Config.TABLE_Calendar + "("
                + Config.COLUMN_Calendar_Day + " TEXT PRIMARY KEY , "
                + Config.COLUMN_Calendar_Complete + " INTEGER NOT NULL "
                + ")";

        Logger.d("Table_calendar create SQL: " + CREATE_Calendar_TABLE);

        db.execSQL(CREATE_Calendar_TABLE);

        Logger.d("Table_calendar created!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Config.TABLE_Routine);

        // Create tables again
        onCreate(db);
    }

}
