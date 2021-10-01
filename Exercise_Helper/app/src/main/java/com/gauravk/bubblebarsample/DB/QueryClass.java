package com.gauravk.bubblebarsample.DB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.gauravk.bubblebarsample.DB.CreateRoutine.Routine;
import com.gauravk.bubblebarsample.cfg.Config;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QueryClass {

    private Context context;

    public QueryClass(Context context){
        this.context = context;
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    public long insertRoutine(Routine routine){

        long id = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_Exercise_NAME, routine.getName());;
        contentValues.put(Config.COLUMN_Weekday, routine.getWeekday());
        contentValues.put(Config.COLUMN_RegNO, routine.getRegNO());
        contentValues.put(Config.COLUMN_Routine_Set_num, routine.getSet_num());
        contentValues.put(Config.COLUMN_Routine_Repeat_num, routine.getRepeat_num());
        contentValues.put(Config.COLUMN_Routine_Rest_time, routine.getRest_time());
        contentValues.put(Config.COLUMN_Routine_Counts, routine.getCounts());
        contentValues.put(Config.COLUMN_Routine_Complete, routine.getcheck());
        //Log.i("Before_insert",contentValues.toString());
        Log.i("Holder_routine_Insert", String.format("ID = %d, Reg_no = %d, name = %s, Set_num = %d, Repeat_num = %d, Rest_time = %d", routine.getId() , routine.getRegNO(), routine.getName() , routine.getSet_num(), routine.getRepeat_num(), routine.getRest_time()));


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
                    int i=0;
                    do {
                        @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_Routine_ID));
                        @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(Config.COLUMN_Exercise_NAME));
                        @SuppressLint("Range") int RegNo = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_RegNO));
                        @SuppressLint("Range") int Set_num = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_Routine_Set_num));
                        @SuppressLint("Range") int Rest_time = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_Routine_Rest_time));
                        @SuppressLint("Range") int Repeat_num = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_Routine_Repeat_num));
                        @SuppressLint("Range") int Counts = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_Routine_Counts));
                        @SuppressLint("Range") int check = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_Routine_Complete));
                        Log.i("Holder_routine_FROM_DB", String.format("ID = %d, Reg_no = %d, name = %s, Set_num = %d, Repeat_num = %d, Rest_time = %d", id , RegNo, name , Set_num, Repeat_num, Rest_time));
                        RoutineList.add(new Routine(id, name, RegNo, Set_num, Repeat_num, Rest_time, Counts, check));
                        Routine routine = RoutineList.get(i++);
                        Log.i("Holder_routine_FROM_DB_", String.format("ID = %d, Reg_no = %d, name = %s, Set_num = %d, Repeat_num = %d, Rest_time = %d", routine.getId() , routine.getRegNO(), routine.getName() , routine.getSet_num(), routine.getRepeat_num(), routine.getRest_time()));


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

    public List<String> getDaysRegNo_Update(String Reg,String day){

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
                        if(Reg != String.valueOf(cur_RegNo)) RegNo.remove(String.valueOf(cur_RegNo));
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
                        @SuppressLint("Range") int Counts = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_Routine_Counts));
                        @SuppressLint("Range") int check = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_Routine_Complete));
                        Log.i("DB_Get_Routine_ALL", String.format("ID = %d, Reg_no = %d, name = %s, Set_num = %d, Repeat_num = %d, Rest_time = %d", id , RegNo, name , Set_num, Repeat_num, Rest_time));

                        RoutineList.add(new Routine(id, name, RegNo, Set_num, Rest_time, Repeat_num, Counts, check));
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

                @SuppressLint("Range") int Counts = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_Routine_Counts));
                @SuppressLint("Range") int check = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_Routine_Complete));
                //Log.i("DB_Get_Routine_bynum", String.format("ID = %d, name = %s, Set_num = %d, Repeat_num = %d, Rest_time = %d", id , name , Set_num, Repeat_num, Rest_time));
                Routine = new Routine(id, name, RegNo, Set_num, Repeat_num, Rest_time, Counts, check);
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

    public void updateRoutineInfo(Routine Routine){

        long rowCount = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        //contentValues.put(Config.COLUMN_Routine_ID, Routine.getId());
        contentValues.put(Config.COLUMN_Exercise_NAME, Routine.getName());;
        contentValues.put(Config.COLUMN_Weekday, Routine.getWeekday());
        contentValues.put(Config.COLUMN_RegNO, Routine.getRegNO());
        contentValues.put(Config.COLUMN_Routine_Set_num, Routine.getSet_num());
        contentValues.put(Config.COLUMN_Routine_Repeat_num, Routine.getRepeat_num());
        contentValues.put(Config.COLUMN_Routine_Rest_time, Routine.getRest_time());
        contentValues.put(Config.COLUMN_Routine_Counts, Routine.getCounts());
        contentValues.put(Config.COLUMN_Routine_Complete, Routine.getcheck());
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

        return ;
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

    public boolean deleteDayRoutines(String Day){
        boolean deleteStatus = false;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            //for "1" delete() method returns  of deleted rows
            //if you don't want row count just use delete(TABLE_NAME, null, null)
            sqLiteDatabase.delete(Config.TABLE_Routine, Config.COLUMN_Weekday + " = ? ", new String[]{Day});
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
