package com.gauravk.bubblebarsample.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.gauravk.bubblebarsample.R;
import com.gauravk.bubblebarsample.cfg.MyGlobal;
import com.gauravk.bubblebarsample.mlkit.mlpose.CameraXLivePreviewActivity;


public class ExerciseFragment extends Fragment {
    private ImageButton kneelup;
    private ImageButton squat;
    private ImageButton pushup;
    private ImageButton situp;
    private ImageButton barbell_curl;
    private ImageButton dead;


    public static ExerciseFragment newInstance() {
        ExerciseFragment fragment = new ExerciseFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exercise, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        kneelup = getView().findViewById(R.id.kneelup);
        kneelup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CameraXLivePreviewActivity.class);
                MyGlobal.getInstance().setExercise("무릎올리기");
                MyGlobal.getInstance().setMode(false);
                startActivity(intent);
            }
        });
        squat = getView().findViewById(R.id.squat);
        squat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CameraXLivePreviewActivity.class);
                MyGlobal.getInstance().setExercise("스쿼트");
                MyGlobal.getInstance().setMode(false);
                startActivity(intent);
            }
        });
        pushup = getView().findViewById(R.id.pushup);
        pushup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CameraXLivePreviewActivity.class);
                MyGlobal.getInstance().setExercise("팔굽혀펴기");
                MyGlobal.getInstance().setMode(false);
                startActivity(intent);
            }
        });
        situp = getView().findViewById(R.id.situp);
        situp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CameraXLivePreviewActivity.class);
                MyGlobal.getInstance().setExercise("윗몸일으키기");
                MyGlobal.getInstance().setMode(false);
                startActivity(intent);
            }
        });
        barbell_curl = getView().findViewById(R.id.barbell_curl);
        barbell_curl.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CameraXLivePreviewActivity.class);
                MyGlobal.getInstance().setExercise("바벨컬");
                MyGlobal.getInstance().setMode(false);
                startActivity(intent);
            }
        });
        dead = getView().findViewById(R.id.dead);
        dead.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CameraXLivePreviewActivity.class);
                MyGlobal.getInstance().setExercise("데드리프트");
                MyGlobal.getInstance().setMode(false);
                startActivity(intent);
            }
        });
    }
}