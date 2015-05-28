package com.fenchtose.toolbarcalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Jay Rambhia on 14/04/15.
 */
public class CalendarAdapter extends BaseAdapter {

    private static final int FIRST_DAY_OF_WEEK = 1;

    private Context context;
    private int res;
    private Calendar month;
    private LayoutInflater layoutInflater;

    private String[] days;

    public CalendarAdapter(Context context, int res, Calendar monthCalendar) {
        this.context = context;
        this.res = res;
        this.month = monthCalendar;

        layoutInflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        refreshDays();
    }

    @Override
    public int getCount() {
        return days.length;
    }

    @Override
    public String getItem(int position) {
        return days[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View workingView = convertView;
        if (workingView == null) {
            workingView = layoutInflater.inflate(res, null);
        }

        TextView dateView = (TextView)workingView.findViewById(R.id.date_textview);
        dateView.setText(getItem(position));

        return workingView;
    }

    public void refreshDays() {

        int lastDay = month.getActualMaximum(Calendar.DAY_OF_MONTH);
        int firstDay = (int)month.get(Calendar.DAY_OF_WEEK);

        if (firstDay == 1) {
            days = new String[lastDay + (FIRST_DAY_OF_WEEK*6)];
        } else {
            days = new String[lastDay+firstDay-(FIRST_DAY_OF_WEEK+1)];
        }

        int j = FIRST_DAY_OF_WEEK;

        if (firstDay > 1) {
            for (j=0; j<firstDay-FIRST_DAY_OF_WEEK; j++) {
                days[j] = "";
            }
        } else {
            for (j=0; j<FIRST_DAY_OF_WEEK*6; j++) {
                days[j] = "";
            }
            j = FIRST_DAY_OF_WEEK*6 + 1;
        }

        int dayNumber = 1;
        for (int i = j-1; i < days.length; i++) {
            days[i] = String.valueOf(dayNumber);
            dayNumber++;
        }

//		Log.i(TAG, "len = " + String.valueOf(days.length));
    }
}
