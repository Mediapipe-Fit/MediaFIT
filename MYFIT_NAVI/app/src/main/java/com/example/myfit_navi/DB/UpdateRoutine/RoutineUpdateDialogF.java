package com.example.myfit_navi.DB.UpdateRoutine;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.example.myfit_navi.DB.CreateRoutine.Routine;
import com.example.myfit_navi.DB.QueryClass;
import com.example.myfit_navi.R;
import com.example.myfit_navi.cfg.Config;

public class RoutineUpdateDialogF extends DialogFragment {

    private static long routineRegNo;
    private static int routineItemPosition;
    private static RoutineUpdateListener routineUpdateListener;

    private Routine mroutine;

    private EditText nameEditText;
    private EditText Set_numEditText;
    private EditText Repeat_numEditText;
    private EditText Rest_timeEditText;
    private Button updateButton;
    private Button cancelButton;

    private String nameString = "";
    private long Set_num = -1;
    private long Repeat_num = -1;
    private long Rest_time = -1;

    private QueryClass DBQueryClass;

    public RoutineUpdateDialogF() {
        // Required empty public constructor
    }

    public static RoutineUpdateDialogF newInstance(long Set_num, int position, RoutineUpdateListener listener){
        routineRegNo = Set_num;
        routineItemPosition = position;
        routineUpdateListener = listener;
        RoutineUpdateDialogF routineUpdateDialogFragment = new RoutineUpdateDialogF();
        Bundle args = new Bundle();
        args.putString("title", "Update routine information");
        routineUpdateDialogFragment.setArguments(args);

        routineUpdateDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);

        return routineUpdateDialogFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.routine_update_dialog_f, container, false);

        DBQueryClass = new QueryClass(getContext());

        nameEditText = view.findViewById(R.id.Exercise_nameEditText);
        Set_numEditText = view.findViewById(R.id.Set_numEditText);
        Repeat_numEditText = view.findViewById(R.id.Repeat_numEditText);
        Rest_timeEditText = view.findViewById(R.id.Rest_timeEditText);
        updateButton = view.findViewById(R.id.updateRoutineInfoButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        String title = getArguments().getString(Config.TITLE);
        getDialog().setTitle(title);

        mroutine = DBQueryClass.getRoutineByRegNum(routineRegNo);

        if(mroutine!=null){
            nameEditText.setText(mroutine.getName());
            Set_numEditText.setText(String.valueOf(mroutine.getSet_num()));
            Repeat_numEditText.setText(String.valueOf(mroutine.getRepeat_num()));
            Rest_timeEditText.setText(String.valueOf(mroutine.getRest_time()));

            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nameString = nameEditText.getText().toString();
                    Set_num = Integer.parseInt(Set_numEditText.getText().toString());
                    Repeat_num = Integer.parseInt(Repeat_numEditText.getText().toString());
                    Rest_time = Integer.parseInt(Rest_timeEditText.getText().toString());

                    mroutine.setName(nameString);
                    mroutine.setSet_num(Set_num);
                    mroutine.setRepeat_num(Repeat_num);
                    mroutine.setRest_time(Rest_time);

                    long id = DBQueryClass.updateRoutineInfo(mroutine);

                    if(id>0){
                        routineUpdateListener.onRoutineUpdateListener(mroutine, routineItemPosition);
                        getDialog().dismiss();
                    }
                }
            });

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getDialog().dismiss();
                }
            });

        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            //noinspection ConstantConditions
            dialog.getWindow().setLayout(width, height);
        }
    }

}
