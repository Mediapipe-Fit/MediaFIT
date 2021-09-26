package com.google.mlkit.vision.demo.java;


import com.google.mlkit.vision.demo.java.CameraXLivePreviewActivity;
import com.google.mlkit.vision.demo.java.posedetector.classification.PoseClassifierProcessor;


public class MyGlobal {
    private String Exercise;

    private int SET;
    private int REP;
    private int REST;
    private static MyGlobal instance = null;
    public static synchronized MyGlobal getInstance(){
        if(null == instance) {
            instance = new MyGlobal();
        }
        return instance;
    }

    public String getExercise(){
        return this.Exercise;
    }
    public int getSET(){return this.SET;}
    public int getREP(){return this.REP;}
    public int getREST(){return this.REST;}

    public void setExercise(String exercise){ this.Exercise = exercise; }
    public void setSET(int set){ this.SET = set; }
    public void setREP(float rep){

        this.REP = (int)rep*10;
    }
    public void setREST(int rest){ this.REST = rest; }


}
