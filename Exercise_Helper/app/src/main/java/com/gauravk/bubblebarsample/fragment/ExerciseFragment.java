package com.gauravk.bubblebarsample.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.gauravk.bubblebarsample.R;
import com.gauravk.bubblebarsample.cfg.MyGlobal;
import com.gauravk.bubblebarsample.mlkit.mlpose.CameraXLivePreviewActivity;
import com.gauravk.bubblebarsample.mlkit.mlpose.RoutineCameraXLivePreviewActivity;


public class ExerciseFragment extends Fragment {
    private ImageButton kneelup;
    private ImageButton squat;

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
                MyGlobal.getInstance().setExercise("KNEEL UP");
                MyGlobal.getInstance().setPOSE_SAMPLE_FILE("pose/kneel_up.csv");
                MyGlobal.getInstance().setMode(false);
                startActivity(intent);
            }
        });
        squat = getView().findViewById(R.id.squirt);
        squat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CameraXLivePreviewActivity.class);
                MyGlobal.getInstance().setExercise("SQUATS");
                MyGlobal.getInstance().setPOSE_SAMPLE_FILE("pose/fitness_pose_samples.csv");
                MyGlobal.getInstance().setMode(false);
                startActivity(intent);
            }
        });
    }

}