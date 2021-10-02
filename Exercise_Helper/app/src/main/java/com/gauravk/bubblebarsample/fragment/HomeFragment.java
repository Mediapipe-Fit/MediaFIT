package com.gauravk.bubblebarsample.fragment;


import static com.gauravk.bubblebarsample.cfg.MyGlobal.today_hangle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.gauravk.bubblebarsample.DB.CreateRoutine.Routine;
import com.gauravk.bubblebarsample.DB.QueryClass;
import com.gauravk.bubblebarsample.DB.ShowRoutine.HomeViewAdapter;
import com.gauravk.bubblebarsample.R;
import com.gauravk.bubblebarsample.mlkit.mlpose.RoutineCameraXLivePreviewActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.CapabilityClient;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class HomeFragment extends Fragment
        implements CircleProgressBar.ProgressFormatter {

    private QueryClass databaseQueryClass;

    private List<Routine> Days_routineList;

    private RecyclerView recyclerView;
    private HomeViewAdapter routineListRecyclerViewAdapter;
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


        recyclerView = (RecyclerView) getView().findViewById(R.id.home_recycler);

        Days_routineList.addAll(databaseQueryClass.getDaysRoutine(today_hangle()));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Days_routineList.size() == 0) {
                    Toast.makeText(getActivity(), "오늘의 운동 루틴을 만들어 주세요", Toast.LENGTH_SHORT).show();
                } else {
                    //Intent intent  = new Intent(view.getContext(), CameraXLivePreviewActivity.class );
                    Intent intent = new Intent(view.getContext(), RoutineCameraXLivePreviewActivity.class);
                    startActivity(intent);
                }

            }
        });

        routineListRecyclerViewAdapter = new HomeViewAdapter(getActivity(), Days_routineList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(routineListRecyclerViewAdapter);
        set_circle();
    }

    public void set_circle() {
        total_setnum = 0;
        complete_setnum = 0;
        prograss_num = 0;
        for (int i = 0; i < Days_routineList.size(); ++i) {
            Routine cur = Days_routineList.get(i);
            if (cur.getcheck() == 1) {
                total_setnum = total_setnum + (int) cur.getSet_num();
                complete_setnum = complete_setnum + (int) cur.getCounts();
                continue;
                //Days_routineList.remove(i);
            }
            total_setnum = total_setnum + (int) cur.getSet_num();
        }
        if (total_setnum != 0) {
            prograss_num = (int) (100 * complete_setnum / total_setnum);
        } else {
            prograss_num = 0;
        }
        Log.e("routinelistsize", Integer.toString(Days_routineList.size()));
        Log.e("total_setup", Integer.toString(total_setnum));
        Log.e("complete_setup", Integer.toString(complete_setnum));
        circleProgressBar.setProgress(prograss_num);
    }

    @Override
    public CharSequence format(int progress, int max) {
        return String.format(DEFAULT_PATTERN, (int) ((float) progress / (float) max * 100));
    }
}