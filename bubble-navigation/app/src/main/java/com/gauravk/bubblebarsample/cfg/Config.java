package com.gauravk.bubblebarsample.cfg;

public class Config {

    public static final String DATABASE_NAME = "Routine-db";

    //column names of Routine table
    public static final String TABLE_Routine = "Routines";
    public static final String COLUMN_Routine_ID = "_id";
    public static final String COLUMN_Weekday = "Weekday";
    public static final String COLUMN_RegNO = "RegNO";
    public static final String COLUMN_Exercise_NAME = "name";
    public static final String COLUMN_Routine_Set_num = "Set_num";
    public static final String COLUMN_Routine_Repeat_num = "Repeat_num";
    public static final String COLUMN_Routine_Rest_time = "Rest_time";
    public static final String COLUMN_Routine_Counts = "Counts";
    public static final String COLUMN_Routine_Complete = "Complete";

    //others for general purpose key-value pair data
    public static final String TITLE = "title";
    public static final String CREATE_Routine = "create_Routine";
    public static final String UPDATE_Routine = "update_Routine";

    public static String selected_weekday = "";
    public static long selected_ID = -1;
    public static String Today = "";


    //column names of Calendar table
    public static final String TABLE_Calendar = "Calendar";
    public static final String COLUMN_Calendar_Day = "_Day";
    public static final String COLUMN_Calendar_Complete = "Complete";
}