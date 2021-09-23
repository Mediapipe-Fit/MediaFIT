package com.example.myfit_navi.DB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.example.myfit_navi.DB.CreateRoutine.Routine;
import com.example.myfit_navi.cfg.Config;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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
        contentValues.put(Config.COLUMN_Exercise_NAME, Routine.getName());;
        contentValues.put(Config.COLUMN_Weekday, Routine.getWeekday());
        contentValues.put(Config.COLUMN_RegNO, Routine.getRegNO());
        contentValues.put(Config.COLUMN_Routine_Set_num, Routine.getSet_num());
        contentValues.put(Config.COLUMN_Routine_Repeat_num, Routine.getRepeat_num());
        contentValues.put(Config.COLUMN_Routine_Rest_time, Routine.getRest_time());
        //Log.i("Before_insert",contentValues.toString());
        try {
            id = sqLiteDatabase.insertOrThrow(Config.TABLE_Routine, null, contentValues);
        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }
        //Log.i("DB_Insert_Routine", String.format("ID = %d, name = %s, Set_num = %d, Repeat_num = %d, Rest_time = %d", id , Routine.getName() , Routine.getSet_num(), Routine.getRepeat_num(), Routine.getRepeat_num()));

        return id;
    }
    public List<Routine> getDaysRoutine(String day){

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {

            cursor = sqLiteDatabase.query(Config.TABLE_Routine, null,
                    Config.COLUMN_Weekday + " = ? ", new String[]{day},
                    null, null, Config.COLUMN_RegNO);

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
                        @SuppressLint("Range") int RegNo = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_RegNO));
                        @SuppressLint("Range") int Set_num = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_Routine_Set_num));
                        @SuppressLint("Range") int Rest_time = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_Routine_Rest_time));
                        @SuppressLint("Range") int Repeat_num = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_Routine_Repeat_num));
                        Log.i("DB_Get_Routine_ALL", String.format("ID = %d, name = %s, Set_num = %d, Repeat_num = %d, Rest_time = %d", id , name , Set_num, Repeat_num, Rest_time));

                        RoutineList.add(new Routine(id, name, RegNo, Set_num, Rest_time, Repeat_num));
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

    public List<String> getDaysRegNo(String day){

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        List<String> RegNo = new ArrayList<String>(Arrays.asList("1","2","3","4","5","6","7","8","9","10"));
        try {

            cursor = sqLiteDatabase.query(Config.TABLE_Routine, new String[]{Config.COLUMN_RegNO},
                    Config.COLUMN_Weekday + " = ? ", new String[]{day},
                    null, null, null);

            /**
             // If you want to execute raw query then uncomment below 2 lines. And comment out above line.

             String SELECT_QUERY = String.format("SELECT %s, %s, %s, %s, %s FROM %s", Config.COLUMN_Routine_ID, Config.COLUMN_Routine_NAME, Config.COLUMN_Routine_Set_num, Config.COLUMN_Routine_Rest_time, Config.COLUMN_Routine_Repeat_num, Config.TABLE_Routine);
             cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);
             */
            if(cursor!=null)
                if(cursor.moveToFirst()){
                    do {
                        @SuppressLint("Range") int cur_RegNo = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_RegNO));
                        Log.i("Yes",String.format("cur_Reg_NO = %d",cur_RegNo));
                        RegNo.remove(String.valueOf(cur_RegNo));
                    }   while (cursor.moveToNext());

                    return RegNo;
                }
        } catch (Exception e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return RegNo;
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
                        @SuppressLint("Range") int RegNo = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_RegNO));
                        @SuppressLint("Range") int Set_num = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_Routine_Set_num));
                        @SuppressLint("Range") int Rest_time = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_Routine_Rest_time));
                        @SuppressLint("Range") int Repeat_num = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_Routine_Repeat_num));
                        //Log.i("DB_Get_Routine_ALL", String.format("ID = %d, name = %s, Set_num = %d, Repeat_num = %d, Rest_time = %d", id , name , Set_num, Repeat_num, Rest_time));

                        RoutineList.add(new Routine(id, name, RegNo, Set_num, Rest_time, Repeat_num));
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

    public Routine getRoutineByRegNum(long Reg_ID){

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        Routine Routine = null;
        try {

            cursor = sqLiteDatabase.query(Config.TABLE_Routine, null,
                    Config.COLUMN_Routine_ID + " = ? ", new String[]{String.valueOf(Reg_ID)},
                    null, null, null);

            /**
             // If you want to execute raw query then uncomment below 2 lines. And comment out above sqLiteDatabase.query() method.

             String SELECT_QUERY = String.format("SELECT * FROM %s WHERE %s = %s", Config.TABLE_Routine, Config.COLUMN_Routine_Set_num, String.valueOf(Set_numNum));
             cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);
             */

            if(cursor.moveToFirst()){
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_Routine_ID));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(Config.COLUMN_Exercise_NAME));
                @SuppressLint("Range") int RegNo = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_RegNO));
                @SuppressLint("Range") int Set_num = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_Routine_Set_num));
                @SuppressLint("Range") int Repeat_num = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_Routine_Repeat_num));
                @SuppressLint("Range") int Rest_time = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_Routine_Rest_time));

                //Log.i("DB_Get_Routine_bynum", String.format("ID = %d, name = %s, Set_num = %d, Repeat_num = %d, Rest_time = %d", id , name , Set_num, Repeat_num, Rest_time));
                Routine = new Routine(id, name, RegNo, Set_num, Repeat_num, Rest_time);
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
        contentValues.put(Config.COLUMN_RegNO, Routine.getRegNO());
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

    public long deleteRoutineByRegNum(long ID) {
        long deletedRowCount = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            deletedRowCount = sqLiteDatabase.delete(Config.TABLE_Routine,
                    Config.COLUMN_Routine_ID + " = ? ",
                    new String[]{ String.valueOf(ID)});
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
