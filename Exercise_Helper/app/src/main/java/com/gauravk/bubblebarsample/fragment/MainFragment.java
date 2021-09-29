package com.gauravk.bubblebarsample.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gauravk.bubblebarsample.DB.CreateRoutine.Routine;
import com.gauravk.bubblebarsample.R;
import com.gauravk.bubblebarsample.mlkit.mlpose.CameraXLivePreviewActivity;
import com.gauravk.bubblebarsample.mlkit.mlpose.RoutineCameraXLivePreviewActivity;

public class MainFragment extends Fragment{

    private Button button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main,container,false);
        button = view.findViewById(R.id.camera_start);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        button = getView().findViewById(R.id.camera_start);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Intent intent  = new Intent(view.getContext(), CameraXLivePreviewActivity.class );
                Intent intent = new Intent(view.getContext(), RoutineCameraXLivePreviewActivity.class);
                startActivity(intent);
            }
        });
    }



    public static MainFragment newInstance(){
        MainFragment fragment = new MainFragment();
        return fragment;
    }

}
