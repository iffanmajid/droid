package com.fenchtose.toolbarcalendar;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Jay Rambhia on 16/04/15.
 */
public class MonthPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<CalendarFragment> fragments;

    private static final int NUMBER_OF_PAGES = 4;

    public MonthPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return null;
    }

    public Fragment getItem(int i, Context context, Calendar monthCalendar) {
        CalendarFragment fragment = getFragments().get(i);
        fragment.setCalendar(context, monthCalendar);
        return fragment;
    }

    @Override
    public int getCount() {
        return NUMBER_OF_PAGES;
    }

    public ArrayList<CalendarFragment> getFragments() {
        if (fragments == null) {
            fragments = new ArrayList<>();
            for (int i=0; i<getCount(); i++) {
                fragments.add(CalendarFragment.newInstance());
            }
        }

        return fragments;
    }
}
