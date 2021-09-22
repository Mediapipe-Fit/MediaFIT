package com.example.myfit_navi.DB.CreateRoutine;

public class Routine {
    private long id;
    private String name;
    private long Set_num;
    private long Repeat_num;
    private long Rest_time;

    public Routine(int id, String name, int Set_num, int Repeat_num, int Rest_time) {
        this.id = id;
        this.name = name;
        this.Set_num = Set_num;
        this.Repeat_num = Repeat_num;
        this.Rest_time = Rest_time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
