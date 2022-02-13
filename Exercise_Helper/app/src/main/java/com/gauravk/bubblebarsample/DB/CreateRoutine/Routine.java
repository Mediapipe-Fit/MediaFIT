package com.gauravk.bubblebarsample.DB.CreateRoutine;

import com.gauravk.bubblebarsample.cfg.Config;

public class Routine {
    private long id;
    private String name;
    private String Weekday;
    private int RegNO;
    private long Set_num;
    private long Repeat_num;
    private long Rest_time;
    private long Counts;
    private int check;

    public Routine(int id, String name, int RegNO, int Set_num, int Repeat_num, int Rest_time, int Counts, int check) {
        this.id = id;
        this.name = name;
        this.RegNO = RegNO;
        this.Set_num = Set_num;
        this.Repeat_num = Repeat_num;
        this.Rest_time = Rest_time;
        this.Weekday = Config.selected_weekday;
        this.Counts = Counts;
        this.check = check;
        //Log.i("DB_Insert_Routine_in_R", String.format("ID = %d, name = %s, Set_num = %d, Repeat_num = %d, Rest_time = %d", id , name , Set_num, Repeat_num, Repeat_num));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getWeekday() { return Weekday; }

    public void setWeekday(String Weekday) { this.Weekday = Weekday; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRegNO() {
        return RegNO;
    }

    public void setRegNO(long RegNO) {
        this.Set_num = RegNO;
    }

    public long getSet_num() {
        return Set_num;
    }

    public void setSet_num(long Set_num) {
        this.Set_num = Set_num;
    }

    public long getRepeat_num() {
        return Repeat_num;
    }

    public void setRepeat_num(long Repeat_num) {
        this.Repeat_num = Repeat_num;
    }

    public long getRest_time() {
        return Rest_time;
    }

    public void setRest_time(long Rest_time) {
        this.Rest_time = Rest_time;
    }

    public long getCounts() {
        return Counts;
    }

    public void setCounts(long Counts) {
        this.Counts = Counts;
    }

    public void plusCounts(){
        this.Counts++;
    }
    public int getcheck() {
        return check;
    }

    public void Complete() {
        this.check = 1;
    }
}
