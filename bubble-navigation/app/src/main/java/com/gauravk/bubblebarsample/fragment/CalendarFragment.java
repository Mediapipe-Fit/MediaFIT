package com.gauravk.bubblebarsample.fragment;

import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.gauravk.bubblebarsample.DB.QueryClass;
import com.gauravk.bubblebarsample.Decorators.EventDecorator;
import com.gauravk.bubblebarsample.Decorators.OneDayDecorator;
import com.gauravk.bubblebarsample.Decorators.SaturdayDecorator;
import com.gauravk.bubblebarsample.Decorators.SundayDecorator;
import com.gauravk.bubblebarsample.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

public class CalendarFragment extends Fragment {
    private QueryClass databaseQueryClass;
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    String time,kcal,menu;
    Cursor cursor;
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
        Mycalendar = (MaterialCalendarView) getView().findViewById(R.id.mycal);
        Mycalendar.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 0, 1)) // 달력의 시작
                .setMaximumDate(CalendarDay.from(2030, 11, 31)) // 달력의 끝
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        Date start = new Date();
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdformat.format(start);
        if(databaseQueryClass.isOkay()){
            Date before3Mon = addMonth(start,-3);
            Date next3Mon = addMonth(start,3);
            String before3Months = sdformat.format(before3Mon);
            String next3Mons = sdformat.format(next3Mon);
            ArrayList<String> Mydays = new ArrayList<>();

            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();

            c1.setTime( before3Mon );
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
                int Year = date.getYear();
                int Month = date.getMonth() + 1;
                int Day = date.getDay();

                Log.i("Year test", Year + "");
                Log.i("Month test", Month + "");
                Log.i("Day test", Day + "");

                String shot_Day = Year + "-" + Month + "-" + Day;
                int status = databaseQueryClass.Get_Status(shot_Day);
                Log.i("shot_Day test", shot_Day + "");
                Mycalendar.clearSelection();
                String Myre = "";
                if(status==0)
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
