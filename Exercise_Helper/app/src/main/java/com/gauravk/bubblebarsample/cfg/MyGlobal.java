package com.gauravk.bubblebarsample.cfg;


import com.gauravk.bubblebarsample.DB.CreateRoutine.Routine;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MyGlobal {
    private static MyGlobal instance = null;
    public static synchronized MyGlobal getInstance(){
        if(null == instance) {
            instance = new MyGlobal();
        }
        return instance;
    }
    private boolean mode = true;    //운동 실행 모드   true면 루틴모드 false면 각 운동모드
    private Date currentTime = Calendar.getInstance().getTime();
    private SimpleDateFormat weekdayFormat = new SimpleDateFormat("EE", Locale.getDefault());
    private String weekDay = weekdayFormat.format(currentTime);
    private List<Routine> routinList;


    private int nowProcess = 0;//현재 운동의 번째째

   private String Exercise; //운동이름
    private long SET;      //세트
    private long now_set;  //현재세트
    private long REST;      //쉬는시간
    private long Num = 2;  //개수
    private long now_num;  //현재개수
    private Routine now_routine; //현재 돌고있는 루틴
    private boolean finish = false;

    private boolean rest_time;
    private float REP;  //상자 그려줄떄 사용

    private String POSE_SAMPLE_FILE = "pose/kneel_up.csv";


    private String[] POSE_CLASSES = {"end"};

    public boolean getmode() {return this.mode;}
    public String getExercise(){
        return this.Exercise;
    }
    public long getSET(){return this.SET;}
    public float getREP(){return this.REP;}
    public long getREST(){return this.REST;}
    public String getPOSE_SAMPLE_FILE(){return this.POSE_SAMPLE_FILE;}
    public String[] getPOSE_CLASSES(){return this.POSE_CLASSES;}
    public long getNum(){return this.Num;}
    public long getNow_num(){return this.now_num;}
    public long getnow_set(){return this.now_set;}
    public Routine getNow_routine(){return this.now_routine;}
    public boolean getRest_time(){return this.rest_time;}
    public boolean isFinish(){return this.finish;}

    public void initRoutine(List<Routine> routineList){
        this.rest_time = false;
        routinList = new ArrayList<>();
        for(int i=0; i< routineList.size();i++){
            if(routineList.get(i).getcheck() == 0){
                routinList.add(routineList.get(i));
            }
        }
        nowProcess = 0;
        if(routinList.isEmpty()){
            return;
        }
        this.Exercise = routinList.get(0).getName();
        this.SET = routinList.get(0).getSet_num();
        this.REST = routinList.get(0).getRest_time();
        this.Num  = routinList.get(0).getRepeat_num();
        this.now_num = 0;
        this.now_set = routinList.get(0).getCounts();
        this.now_routine = routinList.get(0);
    }

    public boolean Done(){              //다음운동으로 넘어감
        if(routinList.isEmpty()){
            return false;
        }
        routinList.remove(0);
        if(!routinList.isEmpty()) {
            this.Exercise = routinList.get(0).getName();
            this.SET = routinList.get(0).getSet_num();
            this.now_set = 0;
            this.REST = routinList.get(0).getRest_time();
            this.Num  = routinList.get(0).getRepeat_num();
            this.now_num = 0;
            this.now_routine = routinList.get(0);
            return true;
        }
        else{
            return false;
        }
    }
    public void setRest_time(boolean rest_time){this.rest_time = rest_time;}
    public void do1set(){
        this.now_set++;
        this.now_num = 0;
    }

    public void Do1(){this.now_num++;} //한개 했을떄 now_num 1증가;
    public void setMode(boolean mode){this.mode = mode;}
    public void setNum(long num){this.Num = num;}
    public void setExercise(String exercise){ this.Exercise = exercise; }
    public void setSET(long set){ this.SET = set; }
    public void setREP(float rep){ this.REP = rep; }
    public void setREST(long rest){ this.REST = rest; }
    public void setPOSE_SAMPLE_FILE(String str){this.POSE_SAMPLE_FILE = str;}
    public void setFinish(boolean fin){this.finish = fin;}

    public static int index = -1;
    public static String today_hangle(){
        Calendar cal = Calendar.getInstance();
        index = cal.get(Calendar.DAY_OF_WEEK);
        String WhatWeek;
        switch (index){
            case 1:
                WhatWeek = "일";
                break;
            case 2:
                WhatWeek = "월";
                break;
            case 3:
                WhatWeek = "화";
                break;
            case 4:
                WhatWeek = "수";
                break;
            case 5:
                WhatWeek = "목";
                break;
            case 6:
                WhatWeek = "금";
                break;
            case 7:
                WhatWeek = "토";
                break;
            default:
                WhatWeek = "이상하다";
                break;
        }
        return WhatWeek;
    }
}
