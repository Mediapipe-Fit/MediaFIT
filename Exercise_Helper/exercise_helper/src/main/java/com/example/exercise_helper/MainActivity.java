/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.exercise_helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.wear.ambient.AmbientModeSupport;
import androidx.wear.widget.WearableRecyclerView;

import com.example.exercise_helper.DataLayerScreen.CapabilityDiscoveryData;
import com.example.exercise_helper.DataLayerScreen.DataLayerScreenData;
import com.example.exercise_helper.DataLayerScreen.Info;
import com.example.exercise_helper.DataLayerScreen.Counts;
import com.example.exercise_helper.DataLayerScreen.Rests;
import com.example.exercise_helper.DataLayerScreen.EventLoggingData;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.CapabilityClient;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Displays {@link WearableRecyclerView} with three main rows of data showing off various features
 * of the Data Layer APIs:
 *
 * <p>
 *
 * <ul>
 *   <li>Row 1: Shows a log of DataItems received from the phone application using {@link
 *       MessageClient}
 *   <li>Row 2: Shows a photo sent from the phone application using {@link DataClient}
 *   <li>Row 3: Displays two buttons to check the connected phone and watch devices using the {@link
 *       CapabilityClient}
 * </ul>
 */
public class MainActivity extends FragmentActivity
        implements AmbientModeSupport.AmbientCallbackProvider,
                DataClient.OnDataChangedListener,
                MessageClient.OnMessageReceivedListener,
                CapabilityClient.OnCapabilityChangedListener {

    private static final String TAG = "MainActivity";

    private static final String CAPABILITY_1_NAME = "capability_1";
    private static final String CAPABILITY_2_NAME = "capability_2";

    ArrayList<DataLayerScreenData> mDataLayerScreenData;

    private WearableRecyclerView mWearableRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private CustomRecyclerAdapter mCustomRecyclerAdapter;
    private TextView Tv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        AmbientModeSupport.attach(this);

        mWearableRecyclerView = findViewById(R.id.recycler_view);
        //Tv = findViewById(R.id.message);
        // Aligns the first and last items on the list vertically centered on the screen.
        mWearableRecyclerView.setEdgeItemsCenteringEnabled(true);

        // Improves performance because we know changes in content do not change the layout size of
        // the RecyclerView.
        mWearableRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mWearableRecyclerView.setLayoutManager(mLayoutManager);

        mDataLayerScreenData = new ArrayList<>();
        //
        mDataLayerScreenData.add(new DataLayerScreen.Info());
        mDataLayerScreenData.add(new DataLayerScreen.Counts());
        mDataLayerScreenData.add(new DataLayerScreen.Rests());
        //mDataLayerScreenData.add(new CapabilityDiscoveryData());
        //mDataLayerScreenData.add(new EventLoggingData());

        // Specifies an adapter (see also next example).
        mCustomRecyclerAdapter = new CustomRecyclerAdapter(mDataLayerScreenData);

        mWearableRecyclerView.setAdapter(mCustomRecyclerAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Instantiates clients without member variables, as clients are inexpensive to create and
        // won't lose their listeners. (They are cached and shared between GoogleApi instances.)
        Wearable.getDataClient(this).addListener(this);
        Wearable.getMessageClient(this).addListener(this);
        Wearable.getCapabilityClient(this)
                .addListener(this, Uri.parse("wear://"), CapabilityClient.FILTER_REACHABLE);
    }

    @Override
    protected void onPause() {
        super.onPause();

        Wearable.getDataClient(this).removeListener(this);
        Wearable.getMessageClient(this).removeListener(this);
        Wearable.getCapabilityClient(this).removeListener(this);
    }

    /*
     * Sends data to proper WearableRecyclerView logger row or if the item passed is an asset, sends
     * to row displaying Bitmaps.
     */
    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.d(TAG, "onDataChanged(): " + dataEvents);

        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());;
                String path = dataMapItem.getDataMap().getString("path");
                if (DataLayerListenerService.COUNT_PATH.equals(path)) {
                    String message = dataMapItem.getDataMap().getString("counts");
                    mCustomRecyclerAdapter.set_count(message);
                    mWearableRecyclerView.scrollToPosition(1);
                    //Tv.setText(message);
                }
                else if (DataLayerListenerService.info_PATH.equals(path)) {
                    final ArrayList<String> get = new ArrayList<>();
                    for(int i=1;i<=4;++i){
                        get.add(dataMapItem.getDataMap().getString(Integer.toString(i)));
                    }
                    Log.i("what",String.valueOf(get.size()));
                    mCustomRecyclerAdapter.set_Info(get);
                    mWearableRecyclerView.scrollToPosition(0);
                }
                else if (DataLayerListenerService.Rest_PATH.equals(path)) {
                    String message = dataMapItem.getDataMap().getString("Rest_time");
                    mCustomRecyclerAdapter.set_Rests(message);
                    mWearableRecyclerView.scrollToPosition(2);
                }
                else{
                    Log.d(TAG, "Unrecognized path: " + path);
                }

            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                mCustomRecyclerAdapter.appendToDataEventLog(
                        "DataItem Deleted", event.getDataItem().toString());

            } else {
                mCustomRecyclerAdapter.appendToDataEventLog(
                        "Unknown data event type", "Type = " + event.getType());
            }
        }
    }

    /*
     * Triggered directly from buttons in recycler_row_capability_discovery.xml to check
     * capabilities of connected devices.
     */
    public void onCapabilityDiscoveryButtonClicked(View view) {
        switch (view.getId()) {
            case R.id.capability_2_btn:
                showNodes(CAPABILITY_2_NAME);
                break;
            case R.id.capabilities_1_and_2_btn:
                showNodes(CAPABILITY_1_NAME, CAPABILITY_2_NAME);
                break;
            default:
                Log.e(TAG, "Unknown click event registered");
        }
    }

    /*
     * Sends data to proper WearableRecyclerView logger row.
     */
    @Override
    public void onMessageReceived(MessageEvent event) {
        Log.d(TAG, "onMessageReceived: " + event);
        mCustomRecyclerAdapter.appendToDataEventLog("Message", event.toString());
    }

    /*
     * Sends data to proper WearableRecyclerView logger row.
     */
    @Override
    public void onCapabilityChanged(CapabilityInfo capabilityInfo) {
        Log.d(TAG, "onCapabilityChanged: " + capabilityInfo);
        mCustomRecyclerAdapter.appendToDataEventLog(
                "onCapabilityChanged", capabilityInfo.toString());
    }

    /** Find the connected nodes that provide at least one of the given capabilities. */
    private void showNodes(final String... capabilityNames) {

        Task<Map<String, CapabilityInfo>> capabilitiesTask =
                Wearable.getCapabilityClient(this)
                        .getAllCapabilities(CapabilityClient.FILTER_REACHABLE);

        capabilitiesTask.addOnSuccessListener(
                new OnSuccessListener<Map<String, CapabilityInfo>>() {
                    @Override
                    public void onSuccess(Map<String, CapabilityInfo> capabilityInfoMap) {
                        Set<Node> nodes = new HashSet<>();

                        if (capabilityInfoMap.isEmpty()) {
                            showDiscoveredNodes(nodes);
                            return;
                        }
                        for (String capabilityName : capabilityNames) {
                            CapabilityInfo capabilityInfo = capabilityInfoMap.get(capabilityName);
                            if (capabilityInfo != null) {
                                nodes.addAll(capabilityInfo.getNodes());
                            }
                        }
                        showDiscoveredNodes(nodes);
                    }
                });
    }

    private void showDiscoveredNodes(Set<Node> nodes) {
        List<String> nodesList = new ArrayList<>();
        for (Node node : nodes) {
            nodesList.add(node.getDisplayName());
        }
        Log.d(
                TAG,
                "Connected Nodes: "
                        + (nodesList.isEmpty()
                                ? "No connected device was found for the given capabilities"
                                : TextUtils.join(",", nodesList)));
        String msg;
        if (!nodesList.isEmpty()) {
            msg = getString(R.string.connected_nodes, TextUtils.join(", ", nodesList));
        } else {
            msg = getString(R.string.no_device);
        }
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
    }


    @Override
    public AmbientModeSupport.AmbientCallback getAmbientCallback() {
        return new MyAmbientCallback();
    }

    /** Customizes appearance for Ambient mode. (We don't do anything minus default.) */
    private class MyAmbientCallback extends AmbientModeSupport.AmbientCallback {
        /** Prepares the UI for ambient mode. */
        @Override
        public void onEnterAmbient(Bundle ambientDetails) {
            super.onEnterAmbient(ambientDetails);
        }

        /**
         * Updates the display in ambient mode on the standard interval. Since we're using a custom
         * refresh cycle, this method does NOT update the data in the display. Rather, this method
         * simply updates the positioning of the data in the screen to avoid burn-in, if the display
         * requires it.
         */
        @Override
        public void onUpdateAmbient() {
            super.onUpdateAmbient();
        }

        /** Restores the UI to active (non-ambient) mode. */
        @Override
        public void onExitAmbient() {
            super.onExitAmbient();
        }
    }
}
