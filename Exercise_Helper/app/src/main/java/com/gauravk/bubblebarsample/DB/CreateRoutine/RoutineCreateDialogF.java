package com.gauravk.bubblebarsample.DB.CreateRoutine;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.DialogFragment;

import com.gauravk.bubblebarsample.DB.QueryClass;
import com.gauravk.bubblebarsample.R;
import com.gauravk.bubblebarsample.cfg.Config;


public class RoutineCreateDialogF extends DialogFragment {

    private static RoutineCreateListener RoutineCreateListener;

    //private EditText Exercise_nameEditText;
    private EditText Set_numEditText;
    private EditText Repeat_numEditText;
    private EditText Rest_timeEditText;
    private Button createButton;
    private Button cancelButton;

    private Spinner Excercise_name;
    private Spinner RegNo;
    private int Set_num = -1;
    private int Repeat_num = -1;
    private int Rest_time = -1;
    private int Regno = -1;
    private String temp = "";

    private QueryClass DBQueryClass; //DB 소환


    public RoutineCreateDialogF() {
        // Required empty public constructor
    }

    public static RoutineCreateDialogF newInstance(String title, RoutineCreateListener listener){
        RoutineCreateListener = listener;
        RoutineCreateDialogF RoutineCreateDialogF = new RoutineCreateDialogF();
        Bundle args = new Bundle();
        args.putString("title", title);
        RoutineCreateDialogF.setArguments(args);

        RoutineCreateDialogF.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);

        return RoutineCreateDialogF;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.routine_create_dialog_f, container, false);
        DBQueryClass = new QueryClass(getActivity());
        //Exercise_nameEditText = view.findViewById(R.id.Exercise_nameEditText);
        Set_numEditText = view.findViewById(R.id.Set_numEditText);
        Repeat_numEditText = view.findViewById(R.id.Repeat_numEditText);
        Rest_timeEditText = view.findViewById(R.id.Rest_timeEditText);
        createButton = view.findViewById(R.id.createButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        Excercise_name = view.findViewById(R.id.Exercise);
        ArrayAdapter Exercise_Adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.Exercises, android.R.layout.simple_spinner_item);
        Exercise_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Excercise_name.setAdapter(Exercise_Adapter);

        RegNo = view.findViewById(R.id.Spinner_RegNO);
        ArrayAdapter RegNo_Adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,DBQueryClass.getDaysRegNo(Config.selected_weekday));
        RegNo.setAdapter(RegNo_Adapter);


        String title = getArguments().getString(Config.TITLE);
        getDialog().setTitle(title);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temp = Excercise_name.getSelectedItem().toString();
                Regno = Integer.parseInt(RegNo.getSelectedItem().toString());
                Set_num = Integer.parseInt(Set_numEditText.getText().toString());
                Repeat_num = Integer.parseInt(Repeat_numEditText.getText().toString());
                Rest_time = Integer.parseInt(Rest_timeEditText.getText().toString());
                //Log.i("DB_Insert_Routine_in_EditText", String.format("ID = %d, name = %s, Set_num = %d, Repeat_num = %d, Rest_time = %d", -1 , temp , Set_num, Repeat_num, Repeat_num));

                //만들때는 0으로
                Routine Routine = new Routine(-1, temp,Regno, Set_num, Repeat_num, Rest_time,0,0);

                QueryClass databaseQueryClass = new QueryClass(getContext());

                long id = databaseQueryClass.insertRoutine(Routine);

                if(id>0){
                    Routine.setId(id);
                    RoutineCreateListener.onRoutineCreated(Routine);
                    getDialog().dismiss();
                }
                //Log.i("DB_Insert_Routine_in_D", String.format("ID = %d, name = %s, Set_num = %d, Repeat_num = %d, Rest_time = %d", id , Routine.getName() , Routine.getSet_num(), Routine.getRepeat_num(), Routine.getRepeat_num()));

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });


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