package com.gauravk.bubblebarsample.fragment;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.gauravk.bubblebarsample.DB.QueryClass;
import com.gauravk.bubblebarsample.Decorators.EventDecorator;
import com.gauravk.bubblebarsample.Decorators.OneDayDecorator;
import com.gauravk.bubblebarsample.Decorators.SaturdayDecorator;
import com.gauravk.bubblebarsample.Decorators.SundayDecorator;
import com.gauravk.bubblebarsample.R;
import com.gauravk.bubblebarsample.cfg.Config;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.shawnlin.numberpicker.NumberPicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class CalendarFragment extends Fragment {
    private QueryClass databaseQueryClass;
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    private static String TAG = "NumberPicker";
    String time,kcal,menu;
    Cursor cursor;
    SimpleDateFormat sdformat;
    MaterialCalendarView Mycalendar;
    public static CalendarFragment newInstance() {
        CalendarFragment fragment = new CalendarFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        databaseQueryClass = new QueryClass(getActivity());
        return inflater.inflate(R.layout.calendarview, container, false);
    }
    @Override
    public void onStart() {
        super.onStart();

        final NumberPicker numberPicker = getView().findViewById(R.id.number_picker);

        // Set divider color
        numberPicker.setDividerColor(ContextCompat.getColor(getActivity(), R.color.Spinner));
        numberPicker.setDividerColorResource(R.color.Spinner);

        // Set formatter
        numberPicker.setFormatter(getString(R.string.number_picker_formatter));
        numberPicker.setFormatter(R.string.number_picker_formatter);

        // Set selected text color
        numberPicker.setSelectedTextColor(ContextCompat.getColor(getActivity(), R.color.Spinner));
        numberPicker.setSelectedTextColorResource(R.color.Spinner);

        // Set selected text size
        numberPicker.setSelectedTextSize(getResources().getDimension(R.dimen.selected_text_size));
        numberPicker.setSelectedTextSize(R.dimen.selected_text_size);

        // Set selected typeface
        numberPicker.setSelectedTypeface(Typeface.create(getString(R.string.roboto_light), Typeface.NORMAL));
        numberPicker.setSelectedTypeface(getString(R.string.roboto_light), Typeface.NORMAL);
        numberPicker.setSelectedTypeface(getString(R.string.roboto_light));
        numberPicker.setSelectedTypeface(R.string.roboto_light, Typeface.NORMAL);
        numberPicker.setSelectedTypeface(R.string.roboto_light);

        // Set text color
        numberPicker.setTextColor(ContextCompat.getColor(getActivity(), R.color.dark_grey));
        numberPicker.setTextColorResource(R.color.dark_grey);

        // Set text size
        numberPicker.setTextSize(getResources().getDimension(R.dimen.text_size));
        numberPicker.setTextSize(R.dimen.text_size);

        // Set typeface
        numberPicker.setTypeface(Typeface.create(getString(R.string.roboto_light), Typeface.NORMAL));
        numberPicker.setTypeface(getString(R.string.roboto_light), Typeface.NORMAL);
        numberPicker.setTypeface(getString(R.string.roboto_light));
        numberPicker.setTypeface(R.string.roboto_light, Typeface.NORMAL);
        numberPicker.setTypeface(R.string.roboto_light);

        // Set value
        numberPicker.setMaxValue(59);
        numberPicker.setMinValue(0);
        numberPicker.setValue(3);

        // Set string values
//        String[] data = {"A", "B", "C", "D", "E", "F", "G", "H", "I"};
//        numberPicker.setMinValue(1);
//        numberPicker.setMaxValue(data.length);
//        numberPicker.setDisplayedValues(data);

        // Set fading edge enabled
        numberPicker.setFadingEdgeEnabled(true);

        // Set scroller enabled
        numberPicker.setScrollerEnabled(true);

        // Set wrap selector wheel
        numberPicker.setWrapSelectorWheel(true);

        // Set accessibility description enabled
        numberPicker.setAccessibilityDescriptionEnabled(true);

        // OnClickListener
        numberPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Click on current value");
            }
        });

        // OnValueChangeListener
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Log.d(TAG, String.format(Locale.US, "oldVal: %d, newVal: %d", oldVal, newVal));
            }
        });

        // OnScrollListener
        numberPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker picker, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    Log.d(TAG, String.format(Locale.US, "newVal: %d", picker.getValue()));
                }
            }
        });
        Mycalendar = (MaterialCalendarView) getView().findViewById(R.id.mycal);
        Mycalendar.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 0, 1)) // 달력의 시작
                .setMaximumDate(CalendarDay.from(2030, 11, 31)) // 달력의 끝
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        Date start = new Date();
        sdformat = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdformat.format(start);
        if(databaseQueryClass.isOkay()){
            Date before3Mon = addMonth(start,-3);
            Date next3Mon = addMonth(start,3);
            String before3Months = sdformat.format(before3Mon);
            String next3Mons = sdformat.format(next3Mon);
            ArrayList<String> Mydays = new ArrayList<>();

            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();

            c1.setTime( start );
            c2.setTime( next3Mon );
            while( c1.compareTo( c2 ) !=1 ) {
                System.out.printf("%tF\n", c1.getTime());
                Mydays.add(sdformat.format(c1.getTime()));
                c1.add(Calendar.DATE, 1);
            }
            databaseQueryClass.CreateNewDay(Mydays);
        }
        else{
            databaseQueryClass.Update_Status(today);
        }

        Mycalendar.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                oneDayDecorator);

        String[] result = {"2017,03,18","2017,04,18","2017,05,18","2017,06,18"};

        new ApiSimulator(result).executeOnExecutor(Executors.newSingleThreadExecutor());

        Mycalendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                Date shot_Day = date.getDate();

                String str_Day = sdformat.format(shot_Day);

                Log.i("shot_Day test", str_Day + "");
                Mycalendar.clearSelection();
                String Myre = "";
                int Status = databaseQueryClass.Get_Status(str_Day);
                if(Status == -1)
                    Myre = "Not Created";
                else if(Status==0)
                    Myre = "Not Complete";
                else
                    Myre = "Complete";
                Toast.makeText(getContext(), Myre , Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static Date addMonth(Date date, int months) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }

    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        String[] Time_Result;

        ApiSimulator(String[] Time_Result){
            this.Time_Result = Time_Result;
        }

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            ArrayList<CalendarDay> dates = new ArrayList<>();

            /*특정날짜 달력에 점표시해주는곳*/
            /*월은 0이 1월 년,일은 그대로*/
            //string 문자열인 Time_Result 을 받아와서 ,를 기준으로짜르고 string을 int 로 변환
            for(int i = 0 ; i < Time_Result.length ; i ++){
                CalendarDay day = CalendarDay.from(calendar);
                String[] time = Time_Result[i].split(",");
                int year = Integer.parseInt(time[0]);
                int month = Integer.parseInt(time[1]);
                int dayy = Integer.parseInt(time[2]);

                dates.add(day);
                calendar.set(year,month-1,dayy);
            }



            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            Mycalendar.addDecorator(new EventDecorator(Color.GREEN, calendarDays,getActivity()));
        }
    }

}
