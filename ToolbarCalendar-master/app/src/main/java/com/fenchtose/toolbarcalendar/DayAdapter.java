package com.fenchtose.toolbarcalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Jay Rambhia on 16/04/15.
 */
public class DayAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private int res = R.layout.date_object_layout;

    private String[] days = {"S", "M", "T", "W", "T", "F", "S"};

    public DayAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            workingView = inflater.inflate(res, null);
        }

        TextView dateView = (TextView)workingView.findViewById(R.id.date_textview);
        dateView.setText(getItem(position));

        return workingView;
    }
}
