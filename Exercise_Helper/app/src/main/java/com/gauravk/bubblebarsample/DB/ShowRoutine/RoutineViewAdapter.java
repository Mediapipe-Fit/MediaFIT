package com.gauravk.bubblebarsample.DB.ShowRoutine;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.gauravk.bubblebarsample.BottomBarActivity;
import com.gauravk.bubblebarsample.DB.CreateRoutine.Routine;
import com.gauravk.bubblebarsample.DB.QueryClass;
import com.gauravk.bubblebarsample.DB.UpdateRoutine.RoutineUpdateDialogF;
import com.gauravk.bubblebarsample.R;
import com.gauravk.bubblebarsample.cfg.Config;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;


public class RoutineViewAdapter extends RecyclerView.Adapter<CustomViewHolder> {

    private Context context;
    private List<Routine> RoutineList;
    private QueryClass queryClass;

    public RoutineViewAdapter(Context context, List<Routine> RoutineList) {
        this.context = context;
        this.RoutineList = RoutineList;
        queryClass = new QueryClass(context);
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.routine_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        final int itemPosition = position;
        //Logger.d(RoutineList.size());
        //Logger.d(position);
        Routine routine = RoutineList.get(position);
        Log.i("Holder_routine",Integer.toString(position));
        holder.Exercise_nameTextView.setText(String.format("%d. %s",routine.getRegNO(),routine.getName()));
        holder.Set_numTextView.setText(String.valueOf(routine.getSet_num()));
        holder.Repeat_numTextView.setText(String.valueOf(routine.getRepeat_num()));
        holder.Rest_timeTextView.setText(String.valueOf(routine.getRest_time()));
        Log.i("Holder_routine", String.format("ID = %d, Reg_no = %d, name = %s, Set_num = %d, Repeat_num = %d, Rest_time = %d", routine.getId() , routine.getRegNO(), routine.getName() , routine.getSet_num(), routine.getRepeat_num(), routine.getRest_time()));


        holder.crossButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Are you sure, You wanted to delete this Routine?");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                deleteRoutine(itemPosition);
                            }
                        });

                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        holder.editButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Config.selected_ID = routine.getId();
                RoutineUpdateDialogF routineUpdateDialogFragment = RoutineUpdateDialogF.newInstance(routine.getId(), itemPosition, (Routine, position) -> {
                    RoutineList.set(position, Routine);
                    notifyDataSetChanged();
                });
                routineUpdateDialogFragment.show(((BottomBarActivity) context).getSupportFragmentManager(), Config.UPDATE_Routine);
            }
        });
    }

    private void deleteRoutine(int position) {
        Routine Routine = RoutineList.get(position);
        long count = queryClass.deleteRoutineByRegNum(Routine.getId());

        if(count>0){
            RoutineList.remove(position);
            notifyDataSetChanged();
            ((BottomBarActivity) context).viewVisibility();
            Toast.makeText(context, "Routine deleted successfully", Toast.LENGTH_LONG).show();
        } else
            Toast.makeText(context, "Routine not deleted. Something wrong!", Toast.LENGTH_LONG).show();

    }

    @Override
    public int getItemCount() {
        return RoutineList.size();
    }

}
