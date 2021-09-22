package com.example.myfit_navi.DB.ShowRoutine;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfit_navi.DB.CreateRoutine.Routine;
import com.example.myfit_navi.DB.CreateRoutine.RoutineCreateDialogF;
import com.example.myfit_navi.DB.CreateRoutine.RoutineCreateListener;
import com.example.myfit_navi.DB.QueryClass;
import com.example.myfit_navi.R;
import com.example.myfit_navi.cfg.Config;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class RoutineListActivity extends AppCompatActivity implements RoutineCreateListener{

    private QueryClass databaseQueryClass = new QueryClass(this);

    private List<Routine> routineList = new ArrayList<>();

    private TextView routineListEmptyTextView;
    private RecyclerView recyclerView;
    private RoutineViewAdapter routineListRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.routine_list);
        Logger.addLogAdapter(new AndroidLogAdapter());

        recyclerView = (RecyclerView) findViewById(R.id.RoutineRecyclerView);
        routineListEmptyTextView = (TextView) findViewById(R.id.emptyRoutineListTextView);

        routineList.addAll(databaseQueryClass.getAllRoutine());

        routineListRecyclerViewAdapter = new RoutineViewAdapter(this, routineList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(routineListRecyclerViewAdapter);

        viewVisibility();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRoutineCreateDialog();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bottom_nav_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.action_delete){

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure, You wanted to delete all routines?");
            alertDialogBuilder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            boolean isAllDeleted = databaseQueryClass.deleteAllRoutines();
                            if(isAllDeleted){
                                routineList.clear();
                                routineListRecyclerViewAdapter.notifyDataSetChanged();
                                viewVisibility();
                            }
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

        return super.onOptionsItemSelected(item);
    }

    public void viewVisibility() {
        if(routineList.isEmpty())
            routineListEmptyTextView.setVisibility(View.VISIBLE);
        else
            routineListEmptyTextView.setVisibility(View.GONE);
    }

    private void openRoutineCreateDialog() {
        RoutineCreateDialogF routineCreateDialogFragment = RoutineCreateDialogF.newInstance("Create routine", this);
        routineCreateDialogFragment.show(getSupportFragmentManager(), Config.CREATE_Routine);
    }

    @Override
    public void onRoutineCreated(Routine routine) {
        routineList.add(routine);
        routineListRecyclerViewAdapter.notifyDataSetChanged();
        viewVisibility();
        Logger.d(routine.getName());
    }
}
