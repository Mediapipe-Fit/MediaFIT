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

import android.graphics.Bitmap;

import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.util.ArrayList;

/**
 * Classes representing data used for each custom {@link ViewHolder} in {@link
 * CustomRecyclerAdapter}.
 */
public class DataLayerScreen {

    //public static final int TYPE_IMAGE_ASSET = 0;
    public static final int TYPE_GET_INFO = 0;
    public static final int TYPE_GET_COUNT = 1;
    public static final int TYPE_start_REST_TIME = 2;
    public static final int TYPE_CAPABILITY_DISCOVERY = 3;
    public static final int TYPE_EVENT_LOGGING = 4;


    /**
     * All classes representing data for {@link ViewHolder} must implement this interface so {@link
     * CustomRecyclerAdapter} knows what type of {@link ViewHolder} to inflate.
     */
    public interface DataLayerScreenData {
        int getType();
    }

    /**
     * Represents message event logs passed to Wear device via {@link
     * com.google.android.gms.wearable.MessageClient} data layer API.
     */
    public static class EventLoggingData implements DataLayerScreenData {

        private StringBuilder mLogBuilder;

        EventLoggingData() {
            mLogBuilder = new StringBuilder();
        }

        @Override
        public int getType() {
            return TYPE_EVENT_LOGGING;
        }

        public String getLog() {
            return mLogBuilder.toString();
        }

        public void addEventLog(String eventName, String data) {
            mLogBuilder.append("\n" + eventName + "\n" + data);
        }
    }


    /**
     * No extra data needed as the {@link ViewHolder} only contains buttons checking capabilities of
     * devices via the {@link com.google.android.gms.wearable.CapabilityClient} data layer API.
     */
    public static class CapabilityDiscoveryData implements DataLayerScreenData {

        @Override
        public int getType() {
            return TYPE_CAPABILITY_DISCOVERY;
        }
    }


    public static class Info implements DataLayerScreenData {
        private ArrayList<String> infos;
        Info(){infos = new ArrayList<>();}
        public void set_info(ArrayList<String> get){
            infos.addAll(get);
        }
        public ArrayList<String> get_info(){
            return infos;
        }
        @Override
        public int getType() {
            return TYPE_GET_INFO;
        }
    }

    public static class Counts implements DataLayerScreenData {
        private String Counts;
        private String Exercise;
        Counts(){Counts = ""; Exercise = "";}

        public void Set_Count(String s,String exercise){
            Counts = s;
            Exercise = exercise;
        }

        public String get_Counts(){
            return Counts;
        }
        public String get_Exercise(){
            return Exercise;
        }
        @Override
        public int getType() {
            return TYPE_GET_COUNT;
        }
    }

    public static class Rests implements DataLayerScreenData {
        private String start_s;
        Rests(){start_s = "";}

        public void set_Rest(String s){
            start_s = s;
        }
        public String get_Rest(){
            return start_s;
        }
        @Override
        public int getType() {
            return TYPE_start_REST_TIME;
        }
    }
}
