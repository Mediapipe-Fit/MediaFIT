package com.gauravk.bubblebarsample.fragment;


import static com.gauravk.bubblebarsample.cfg.MyGlobal.today_hangle;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.gauravk.bubblebarsample.DB.CreateRoutine.Routine;
import com.gauravk.bubblebarsample.DB.QueryClass;
import com.gauravk.bubblebarsample.DB.ShowRoutine.RoutineViewAdapter2;
import com.gauravk.bubblebarsample.R;
import com.gauravk.bubblebarsample.mlkit.mlpose.RoutineCameraXLivePreviewActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class HomeFragment extends Fragment implements CircleProgressBar.ProgressFormatter{

    private QueryClass databaseQueryClass;

    private List<Routine> Days_routineList;

    private RecyclerView recyclerView;
    private RoutineViewAdapter2 routineListRecyclerViewAdapter;
    private static final String DEFAULT_PATTERN = "%d%%";
    private Button button;
    CircleProgressBar circleProgressBar;

    private int total_setnum = 0;
    private int complete_setnum = 0;
    private int prograss_num = 0;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        databaseQueryClass = new QueryClass(getActivity());
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);
        circleProgressBar = rootView.findViewById(R.id.cpb_circlebar);
        return rootView;
    }
    @Override
    public void onStart() {
        super.onStart();
        Days_routineList = new ArrayList<>();
        button = getView().findViewById(R.id.exercise_start_btn);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Intent intent  = new Intent(view.getContext(), CameraXLivePreviewActivity.class );
                Intent intent = new Intent(view.getContext(), RoutineCameraXLivePreviewActivity.class);
                startActivity(intent);
            }
        });
        recyclerView = (RecyclerView) getView().findViewById(R.id.home_recycler);

        Days_routineList.addAll(databaseQueryClass.getDaysRoutine(today_hangle()));

        routineListRecyclerViewAdapter = new RoutineViewAdapter2(getActivity(), Days_routineList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(routineListRecyclerViewAdapter);
        set_circle();
    }
    public void set_circle(){
        for(int i=0;i<Days_routineList.size();++i){
            Routine cur = Days_routineList.get(i);
            total_setnum = (int) (total_setnum + cur.getSet_num());
            complete_setnum = (int) (complete_setnum + cur.getCounts());
        }
        circleProgressBar.setProgress((int)(100*complete_setnum/total_setnum));
    }

    @Override
    public CharSequence format(int progress, int max) {
        return String.format(DEFAULT_PATTERN, (int) ((float) progress / (float) max * 100));
    }
}

