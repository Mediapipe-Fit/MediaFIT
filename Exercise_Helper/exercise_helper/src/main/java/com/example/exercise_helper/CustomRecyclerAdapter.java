/*
 * Copyright (C) 2018 The Android Open Source Project
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

import static com.example.exercise_helper.DataLayerScreen.TYPE_CAPABILITY_DISCOVERY;
import static com.example.exercise_helper.DataLayerScreen.TYPE_EVENT_LOGGING;
import static com.example.exercise_helper.DataLayerScreen.TYPE_GET_COUNT;
import static com.example.exercise_helper.DataLayerScreen.TYPE_GET_INFO;
import static com.example.exercise_helper.DataLayerScreen.TYPE_start_REST_TIME;

import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import androidx.wear.widget.WearableRecyclerView;

import com.example.exercise_helper.DataLayerScreen.DataLayerScreenData;
import com.example.exercise_helper.DataLayerScreen.EventLoggingData;
import com.example.exercise_helper.DataLayerScreen.Info;
import com.example.exercise_helper.DataLayerScreen.Rests;
import com.example.exercise_helper.DataLayerScreen.Counts;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.StringJoiner;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Populates a {@link WearableRecyclerView}.
 *
 * <p>Provides a custom {@link ViewHolder} for each unique row associated with a feature from the
 * Data Layer APIs (one for transferring images, one for event logging, and one for checking
 * capabilities). Data for each {@link ViewHolder} populated by {@link DataLayerScreen}.
 */
public class CustomRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "CustomRecyclerAdapter";
    public static String cur_exe_name = "";
    public static String cur_second = "";
    private ArrayList<DataLayerScreen.DataLayerScreenData> mDataSet;

    public CustomRecyclerAdapter(ArrayList<DataLayerScreenData> dataSet) {
        mDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Log.d(TAG, "onCreateViewHolder(): viewType: " + viewType);

        ViewHolder viewHolder = null;

        switch (viewType) {

            case TYPE_EVENT_LOGGING:
                viewHolder =
                        new EventLoggingViewHolder(
                                LayoutInflater.from(viewGroup.getContext())
                                        .inflate(
                                                R.layout.recycler_row_event_logging,
                                                viewGroup,
                                                false));
                break;

            case TYPE_CAPABILITY_DISCOVERY:
                viewHolder =
                        new CapabilityDiscoveryViewHolder(
                                LayoutInflater.from(viewGroup.getContext())
                                        .inflate(
                                                R.layout.recycler_row_capability_discovery,
                                                viewGroup,
                                                false));
                break;
            case TYPE_GET_INFO:
                viewHolder =
                        new InfoViewHolder(
                                LayoutInflater.from(viewGroup.getContext())
                                        .inflate(
                                                R.layout.cur_exercise,
                                                viewGroup,
                                                false));
                break;
            case TYPE_GET_COUNT:
                viewHolder =
                        new CountsViewHolder(
                                LayoutInflater.from(viewGroup.getContext())
                                        .inflate(
                                                R.layout.display_count,
                                                viewGroup,
                                                false));
                break;
            case TYPE_start_REST_TIME:
                viewHolder =
                        new RestsViewHolder(
                                LayoutInflater.from(viewGroup.getContext())
                                        .inflate(
                                                R.layout.rest_time,
                                                viewGroup,
                                                false));
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");

        switch (viewHolder.getItemViewType()) {

            case TYPE_EVENT_LOGGING:
                EventLoggingData eventLoggingData = (EventLoggingData) mDataSet.get(position);

                EventLoggingViewHolder eventLoggingViewHolder = (EventLoggingViewHolder) viewHolder;

                String log = eventLoggingData.getLog();

                if (log.length() > 0) {
                    eventLoggingViewHolder.logDataLayerInformation(eventLoggingData.getLog());
                }
                break;

            case TYPE_CAPABILITY_DISCOVERY:
                // This view never changes, as it contains just two buttons that trigger
                // capabilities requests to other devices.
                break;

            case TYPE_GET_INFO:
                InfoViewHolder exe_info = (InfoViewHolder) viewHolder;
                Info myinfo = (Info) mDataSet.get(position);
                exe_info.setText(myinfo.get_info());
                break;

            case TYPE_GET_COUNT:
                CountsViewHolder exe_c = (CountsViewHolder) viewHolder;
                Counts myc = (Counts) mDataSet.get(position);
                exe_c.setc(myc.get_Counts());
                break;

            case TYPE_start_REST_TIME:
                RestsViewHolder exe_R = (RestsViewHolder) viewHolder;
                Rests rest = (Rests) mDataSet.get(position);
                exe_R.setR(rest.get_Rest());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    @Override
    public int getItemViewType(int position) {
        DataLayerScreenData dataLayerScreenData = mDataSet.get(position);
        return dataLayerScreenData.getType();
    }

    private int findItemIndex(@NonNull int assetType) {

        for (int index = 0; index < mDataSet.size(); index++) {
            if (mDataSet.get(index).getType() == assetType) {
                return index;
            }
        }
        return -1;
    }

    public void appendToDataEventLog(@NonNull String eventName, @NonNull String details) {
        int index = findItemIndex(TYPE_EVENT_LOGGING);

        if (index > -1) {
            EventLoggingData dataItemType = (EventLoggingData) mDataSet.get(index);
            dataItemType.addEventLog(eventName, details);

            notifyItemChanged(index);
        }
    }

    public void set_Info(ArrayList<String> get){
        int index = findItemIndex(TYPE_GET_INFO);

        if (index > -1) {
            Info dataItemType = (Info) mDataSet.get(index);
            dataItemType.set_info(get);

            notifyItemChanged(index);
        }
    }
    public void set_count(String s){
        int index = findItemIndex(TYPE_GET_COUNT);

        if (index > -1) {
            Counts dataItemType = (Counts) mDataSet.get(index);
            dataItemType.Set_Count(s);

            notifyItemChanged(index);
        }
    }
    public void set_Rests(String s){
        int index = findItemIndex(TYPE_start_REST_TIME);

        if (index > -1) {
            Rests dataItemType = (Rests) mDataSet.get(index);
            dataItemType.set_Rest(s);

            notifyItemChanged(index);
        }
    }
    /**
     * Displays text log of data passed from other devices via the {@link
     * com.google.android.gms.wearable.MessageClient} API.
     */
    public static class EventLoggingViewHolder extends ViewHolder {

        private final TextView mIntroTextView;
        private final TextView mDataLogTextView;

        public EventLoggingViewHolder(View view) {
            super(view);
            mIntroTextView = view.findViewById(R.id.intro);
            mDataLogTextView = view.findViewById(R.id.event_logging);
        }

        @Override
        public String toString() {
            return (String) mDataLogTextView.getText();
        }

        public void logDataLayerInformation(String log) {
            mIntroTextView.setVisibility(View.INVISIBLE);
            mDataLogTextView.setText(log);
        }
    }



    public static class InfoViewHolder extends ViewHolder {

        private final TextView Exercise_name;
        private final TextView Set_num;
        private final TextView Repeat_num;
        private final TextView Rest_time;

        public InfoViewHolder(View view) {
            super(view);
            Exercise_name = view.findViewById(R.id.Exercise_name);
            Set_num = view.findViewById(R.id.Set_num);
            Repeat_num = view.findViewById(R.id.Repeat_num);
            Rest_time = view.findViewById(R.id.Rest_time);
        }

        public void setText(ArrayList<String> my) {
            if(my.size() != 0){
                cur_exe_name = my.get(0);
                Exercise_name.setText(my.get(0));
                Set_num.setText(my.get(1));
                Repeat_num.setText(my.get(2));
                Rest_time.setText(my.get(3));
            }
        }
    }

    public static class CountsViewHolder extends ViewHolder {

        private final TextView Cur_Exercise_name;
        private final TextView counts;

        public CountsViewHolder(View view) {
            super(view);
            Cur_Exercise_name = view.findViewById(R.id.Cur_Exercise_name);
            counts = view.findViewById(R.id.counts);
            if(cur_exe_name != "") Cur_Exercise_name.setText(cur_exe_name);
        }

        public void setc(String c) {
            if(c != "")
                counts.setText(c);
        }
    }

    public static class RestsViewHolder extends ViewHolder {

        private final TextView Rest_time;

        public RestsViewHolder(View view) {
            super(view);
            Rest_time = view.findViewById(R.id.Rest_time);
        }

        public void setR(String R) {
            if(R != ""){
                int start_s = Integer.parseInt(R) * 1000;
                new CountDownTimer(start_s, 1000) {
                    public void onTick(long millisUntilFinished) {
                        Rest_time.setText(Integer.toString((int)millisUntilFinished / 1000));
                    }
                    public void onFinish() {
                        Rest_time.setText("End!");
                    }
                }.start();
            }

        }
    }

    /**
     * Displays two buttons for querying device capabilities via {@link
     * com.google.android.gms.wearable.CapabilityClient}.
     */
    public static class CapabilityDiscoveryViewHolder extends ViewHolder {

        public CapabilityDiscoveryViewHolder(View view) {
            super(view);
        }
    }
}
