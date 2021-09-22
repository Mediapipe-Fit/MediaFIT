package com.example.myfit_navi.DB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;

import com.example.myfit_navi.DB.CreateRoutine.Routine;
import com.example.myfit_navi.cfg.Config;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QueryClass {

    private Context context;

    public QueryClass(Context context){
        this.context = context;
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    public long insertRoutine(Routine Routine){

        long id = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_Exercise_NAME, Routine.getName());
        contentValues.put(Config.COLUMN_Routine_Set_num, Routine.getSet_num());
        contentValues.put(Config.COLUMN_Routine_Repeat_num, Routine.getRepeat_num());
        contentValues.put(Config.COLUMN_Routine_Rest_time, Routine.getRepeat_num());

        try {
            id = sqLiteDatabase.insertOrThrow(Config.TABLE_Routine, null, contentValues);
        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return id;
    }

    public List<Routine> getAllRoutine(){

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {

            cursor = sqLiteDatabase.query(Config.TABLE_Routine, null, null, null, null, null, null, null);

            /**
             // If you want to execute raw query then uncomment below 2 lines. And comment out above line.

             String SELECT_QUERY = String.format("SELECT %s, %s, %s, %s, %s FROM %s", Config.COLUMN_Routine_ID, Config.COLUMN_Routine_NAME, Config.COLUMN_Routine_Set_num, Config.COLUMN_Routine_Rest_time, Config.COLUMN_Routine_Repeat_num, Config.TABLE_Routine);
             cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);
             */

            if(cursor!=null)
                if(cursor.moveToFirst()){
                    List<Routine> RoutineList = new ArrayList<>();
                    do {
                        @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_Routine_ID));
                        @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(Config.COLUMN_Exercise_NAME));
                        @SuppressLint("Range") int Set_num = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_Routine_Set_num));
                        @SuppressLint("Range") int Rest_time = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_Routine_Rest_time));
                        @SuppressLint("Range") int Repeat_num = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_Routine_Repeat_num));

                        RoutineList.add(new Routine(id, name, Set_num, Rest_time, Repeat_num));
                    }   while (cursor.moveToNext());

                    return RoutineList;
                }
        } catch (Exception e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return Collections.emptyList();
    }

    public Routine getRoutineByRegNum(long Set_numNum){

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        Routine Routine = null;
        try {

            cursor = sqLiteDatabase.query(Config.TABLE_Routine, null,
                    Config.COLUMN_Routine_Set_num + " = ? ", new String[]{String.valueOf(Set_numNum)},
                    null, null, null);

            /**
             // If you want to execute raw query then uncomment below 2 lines. And comment out above sqLiteDatabase.query() method.

             String SELECT_QUERY = String.format("SELECT * FROM %s WHERE %s = %s", Config.TABLE_Routine, Config.COLUMN_Routine_Set_num, String.valueOf(Set_numNum));
             cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);
             */

            if(cursor.moveToFirst()){
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_Routine_ID));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(Config.COLUMN_Exercise_NAME));
                @SuppressLint("Range") int Set_num = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_Routine_Set_num));
                @SuppressLint("Range") int Repeat_num = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_Routine_Repeat_num));
                @SuppressLint("Range") int Rest_time = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_Routine_Rest_time));

                Routine = new Routine(id, name, Set_num, Repeat_num, Rest_time);
            }
        } catch (Exception e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return Routine;
    }

    public long updateRoutineInfo(Routine Routine){

        long rowCount = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_Exercise_NAME, Routine.getName());
        contentValues.put(Config.COLUMN_Routine_Set_num, Routine.getSet_num());
        contentValues.put(Config.COLUMN_Routine_Repeat_num, Routine.getRepeat_num());
        contentValues.put(Config.COLUMN_Routine_Rest_time, Routine.getRest_time());

        try {
            rowCount = sqLiteDatabase.update(Config.TABLE_Routine, contentValues,
                    Config.COLUMN_Routine_ID + " = ? ",
                    new String[] {String.valueOf(Routine.getId())});
        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return rowCount;
    }

    public long deleteRoutineByRegNum(long Set_numNum) {
        long deletedRowCount = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            deletedRowCount = sqLiteDatabase.delete(Config.TABLE_Routine,
                    Config.COLUMN_Routine_Set_num + " = ? ",
                    new String[]{ String.valueOf(Set_numNum)});
        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return deletedRowCount;
    }

    public boolean deleteAllRoutines(){
        boolean deleteStatus = false;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            //for "1" delete() method returns  of deleted rows
            //if you don't want row count just use delete(TABLE_NAME, null, null)
            sqLiteDatabase.delete(Config.TABLE_Routine, null, null);

            long count = DatabaseUtils.queryNumEntries(sqLiteDatabase, Config.TABLE_Routine);

            if(count==0)
                deleteStatus = true;

        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return deleteStatus;
    }

}
