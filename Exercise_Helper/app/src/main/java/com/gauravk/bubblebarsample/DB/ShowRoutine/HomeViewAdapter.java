package com.gauravk.bubblebarsample.DB.ShowRoutine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gauravk.bubblebarsample.DB.CreateRoutine.Routine;
import com.gauravk.bubblebarsample.DB.QueryClass;
import com.gauravk.bubblebarsample.R;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.List;


public class RoutineViewAdapter2 extends RecyclerView.Adapter<CustomViewHolder> {

    private Context context;
    private List<Routine> RoutineList;
    private QueryClass queryClass;

    public RoutineViewAdapter2(Context context, List<Routine> RoutineList) {
        this.context = context;
        this.RoutineList = RoutineList;
        queryClass = new QueryClass(context);
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.routine_item2, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        final int itemPosition = position;
        final Routine routine = RoutineList.get(position);

        holder.Exercise_nameTextView.setText(String.format("%d. %s",routine.getRegNO(),routine.getName()));
        holder.Set_numTextView.setText(String.valueOf(routine.getSet_num()-routine.getCounts()));
        holder.Repeat_numTextView.setText(String.valueOf(routine.getRepeat_num()));
        holder.Rest_timeTextView.setText(String.valueOf(routine.getRest_time()));
        Logger.d(routine.getRepeat_num());
        Logger.d(routine.getRest_time());



    }



    @Override
    public int getItemCount() {
        return RoutineList.size();
    }

}
