package com.fenchtose.toolbarcalendar;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Jay Rambhia on 14/04/15.
 */
public class CalendarFragment extends Fragment {

    private CalendarAdapter calendarAdapter;
    private DayAdapter dayAdapter;

    private GridView calendarView;
    private Calendar monthCalendar;

    private GridView daysView;

    private Context context;

    private static final String TAG = "CalendarFragment";

    public CalendarFragment() {

    }

    public static CalendarFragment newInstance() {
        return new CalendarFragment();
    }

    public void setCalendar(Context context, Calendar month) {
        monthCalendar = month;
        calendarAdapter = new CalendarAdapter(context, R.layout.date_object_layout, monthCalendar);
    }

    public int getDatesInMonth() {
        if (calendarAdapter != null) {
            return calendarAdapter.getCount();
        }

        return 0;
    }

    public String getMonthName() {
        if (monthCalendar != null) {
            return monthCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH);
        }

        return "";
    }

    public int getYear() {
        if (monthCalendar != null) {
            return monthCalendar.get(Calendar.YEAR);
        }

        return -1;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity.getApplicationContext();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.i(TAG, "onCreateView");

        View fragmentView = inflater.inflate(R.layout.cal_fragment_layout, container, false);

        if (monthCalendar == null) {
            monthCalendar = Calendar.getInstance();
        }

        if (calendarAdapter == null) {
            calendarAdapter = new CalendarAdapter(context, R.layout.date_object_layout, monthCalendar);
        }

        calendarView = (GridView)fragmentView.findViewById(R.id.gridview);
        calendarView.setAdapter(calendarAdapter);

        daysView = (GridView)fragmentView.findViewById(R.id.gridview_days);
        dayAdapter = new DayAdapter(context);
        daysView.setAdapter(dayAdapter);

        return fragmentView;
    }

}
