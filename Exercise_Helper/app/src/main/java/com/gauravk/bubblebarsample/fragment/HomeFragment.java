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
        implements CircleProgressBar.ProgressFormatter,
        DataClient.OnDataChangedListener,
        MessageClient.OnMessageReceivedListener,
        CapabilityClient.OnCapabilityChangedListener{

    private QueryClass databaseQueryClass;

    private List<Routine> Days_routineList;

    private RecyclerView recyclerView;
    private HomeViewAdapter routineListRecyclerViewAdapter;
    private static final String DEFAULT_PATTERN = "%d%%";
    private Button button;
    private Button Wear_os,Info,Counts,Rest;
    CircleProgressBar circleProgressBar;

    private int total_setnum = 0;
    private int complete_setnum = 0;
    private int prograss_num = 0;


    // Wear os

    private static final String TAG = "HomeFragment";


    public static final String START_ACTIVITY_PATH = "/start-activity";
    public static final String COUNT_PATH = "/count";
    public static final String Rest_PATH = "/rest";
    public static final String info_PATH = "/info";


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


        Wear_os = getView().findViewById(R.id.wear_os);
        Info = getView().findViewById(R.id.send_info);
        Counts = getView().findViewById(R.id.send_counts);
        Rest = getView().findViewById(R.id.send_rest);
        recyclerView = (RecyclerView) getView().findViewById(R.id.home_recycler);

        Days_routineList.addAll(databaseQueryClass.getDaysRoutine(today_hangle()));
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(Days_routineList.size()==0){
                    Toast.makeText(getActivity(), "오늘의 운동 루틴을 만들어 주세요", Toast.LENGTH_SHORT).show();
                }
                else{
                    //Intent intent  = new Intent(view.getContext(), CameraXLivePreviewActivity.class );
                    Intent intent = new Intent(view.getContext(), RoutineCameraXLivePreviewActivity.class);
                    startActivity(intent);
                }

            }
        });
        Wear_os.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onStartWearableActivityClick();
            }
        });
        Info.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(Days_routineList.size()!=0) sendData_info(Days_routineList.get(0));
            }
        });
        Counts.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                sendData_counts(1);
            }
        });
        Rest.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(Days_routineList.size()!=0)
                    sendData_Rest(Days_routineList.get(0).getRest_time());
            }
        });

        routineListRecyclerViewAdapter = new HomeViewAdapter(getActivity(), Days_routineList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(routineListRecyclerViewAdapter);
        set_circle();
    }
    public void set_circle(){
        total_setnum = 0;
        complete_setnum = 0;
        prograss_num = 0;
        for(int i=0;i<Days_routineList.size();++i){
            Routine cur = Days_routineList.get(i);
            if(cur.getcheck() == 1){
                total_setnum = total_setnum + (int) cur.getSet_num();
                complete_setnum = complete_setnum + (int) cur.getCounts();
                continue;
                //Days_routineList.remove(i);
            }
            total_setnum = total_setnum + (int) cur.getSet_num();
        }
        if(total_setnum!=0){
            prograss_num = (int)(100*complete_setnum/total_setnum);}
        else{prograss_num = 0;}
        Log.e("routinelistsize", Integer.toString(Days_routineList.size()));
        Log.e("total_setup", Integer.toString(total_setnum));
        Log.e("complete_setup", Integer.toString(complete_setnum));
        circleProgressBar.setProgress(prograss_num);
    }

    @Override
    public CharSequence format(int progress, int max) {
        return String.format(DEFAULT_PATTERN, (int) ((float) progress / (float) max * 100));
    }

    @Override
    public void onResume() {
        super.onResume();
        Wear_os.setEnabled(true);
        // Instantiates clients without member variables, as clients are inexpensive to create and
        // won't lose their listeners. (They are cached and shared between GoogleApi instances.)
        Wearable.getDataClient(getActivity()).addListener(this);
        Wearable.getMessageClient(getActivity()).addListener(this);
        Wearable.getCapabilityClient(getActivity())
                .addListener(this, Uri.parse("wear://"), CapabilityClient.FILTER_REACHABLE);
    }

    @Override
    public void onStop() {
        super.onStop();
        Wearable.getDataClient(getActivity()).removeListener(this);
        Wearable.getMessageClient(getActivity()).removeListener(this);
        Wearable.getCapabilityClient(getActivity()).removeListener(this);
    }

    @Override
    public void onCapabilityChanged(@NonNull CapabilityInfo capabilityInfo) {

        LOGD(TAG, "onCapabilityChanged: " + capabilityInfo);
    }

    @Override
    public void onDataChanged(@NonNull DataEventBuffer dataEventBuffer) {

        LOGD(TAG, "onDataChanged: " + dataEventBuffer);
    }

    @Override
    public void onMessageReceived(@NonNull MessageEvent messageEvent) {
        LOGD(
                TAG,
                "onMessageReceived() A message from watch was received:"
                        + messageEvent.getRequestId()
                        + " "
                        + messageEvent.getPath());
    }

    public void onStartWearableActivityClick() {
        LOGD(TAG, "Generating RPC");

        // Trigger an AsyncTask that will query for a list of connected nodes and send a
        // "start-activity" message to each connected node.
        new StartWearableActivityTask().execute();
    }

    @WorkerThread
    private void sendStartActivityMessage(String node) {

        Task<Integer> sendMessageTask =
                Wearable.getMessageClient(getActivity()).sendMessage(node, START_ACTIVITY_PATH, new byte[0]);

        try {
            // Block on a task and get the result synchronously (because this is on a background
            // thread).
            Integer result = Tasks.await(sendMessageTask);
            LOGD(TAG, "Message sent: " + result);

        } catch (ExecutionException exception) {
            Log.e(TAG, "Task failed: " + exception);

        } catch (InterruptedException exception) {
            Log.e(TAG, "Interrupt occurred: " + exception);
        }
    }

    @WorkerThread
    private Collection<String> getNodes() {
        HashSet<String> results = new HashSet<>();

        Task<List<Node>> nodeListTask =
                Wearable.getNodeClient(getActivity()).getConnectedNodes();

        try {
            // Block on a task and get the result synchronously (because this is on a background
            // thread).
            List<Node> nodes = Tasks.await(nodeListTask);

            for (Node node : nodes) {
                results.add(node.getId());
            }

        } catch (ExecutionException exception) {
            Log.e(TAG, "Task failed: " + exception);

        } catch (InterruptedException exception) {
            Log.e(TAG, "Interrupt occurred: " + exception);
        }

        return results;
    }


    /** As simple wrapper around Log.d */
    private static void LOGD(final String tag, String message) {
        if (Log.isLoggable(tag, Log.DEBUG)) {
            Log.d(tag, message);
        }
    }


    private class StartWearableActivityTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... args) {
            Collection<String> nodes = getNodes();
            for (String node : nodes) {
                sendStartActivityMessage(node);
            }
            return null;
        }
    }

    public void sendData_counts(int count) {
        Log.i("Send_info","send Counts");
        PutDataMapRequest dataMap = PutDataMapRequest.create(COUNT_PATH);
        dataMap.getDataMap().putString("path", COUNT_PATH);
        dataMap.getDataMap().putString("counts", Integer.toString(count));
        PutDataRequest request = dataMap.asPutDataRequest();
        request.setUrgent();
        Wearable.getDataClient(getActivity()).putDataItem(request);

    }

    public void sendData_info(Routine routine) {
        Log.i("Send_info",routine.getName());
        PutDataMapRequest dataMap = PutDataMapRequest.create(COUNT_PATH);
        dataMap.getDataMap().putString("path", info_PATH);
        dataMap.getDataMap().putString("1", routine.getName());
        dataMap.getDataMap().putString("2", Integer.toString((int)routine.getSet_num()));
        dataMap.getDataMap().putString("3", Integer.toString((int)routine.getRepeat_num()));
        dataMap.getDataMap().putString("4", Integer.toString((int)routine.getRest_time()));
        PutDataRequest request = dataMap.asPutDataRequest();
        request.setUrgent();

        Task<DataItem> dataItemTask = Wearable.getDataClient(getActivity()).putDataItem(request);
        dataItemTask
                .addOnSuccessListener(new OnSuccessListener<DataItem>() {
                    @Override
                    public void onSuccess(DataItem dataItem) {
                        Log.d(TAG, "Sending message was successful: " + dataItem);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Sending message failed: " + e);
                    }
                })
        ;

    }
    public void sendData_Rest(long Rest_time) {
        Log.i("Send_info","send Rest_time");
        PutDataMapRequest dataMap = PutDataMapRequest.create(COUNT_PATH);
        dataMap.getDataMap().putString("path", Rest_PATH);
        dataMap.getDataMap().putString("Rest_time", Integer.toString((int)Rest_time));
        PutDataRequest request = dataMap.asPutDataRequest();
        request.setUrgent();


        Task<DataItem> dataItemTask = Wearable.getDataClient(getActivity()).putDataItem(request);
        dataItemTask
                .addOnSuccessListener(new OnSuccessListener<DataItem>() {
                    @Override
                    public void onSuccess(DataItem dataItem) {
                        Log.d(TAG, "Sending message was successful: " + dataItem);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Sending message failed: " + e);
                    }
                })
        ;
    }
}

