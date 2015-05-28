package com.fenchtose.toolbarcalendar;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Jay Rambhia on 14/04/15.
 */
public class CalendarPagerAdapter extends FragmentStatePagerAdapter {

    private FragmentManager mFragmentManager;

    private int NUM_PAGES = CalendarViewPager.OFFSET * 2;
    private Calendar fixedCalendar;
    private Context context;

    private int datesInMonth;
    private String monthName;
    private String year;

    private Map<Integer, String> fragmentTags;

    private static final String TAG = "CalendarPagerAdapter";

    private ArrayList<CalendarFragment> calendarFragments;

    private int count = CalendarViewPager.OFFSET * 2;

    public CalendarPagerAdapter(Context context, FragmentManager fm, Calendar month) {
        super(fm);
        this.context = context;
        fixedCalendar = month;
        calendarFragments = new ArrayList<>();
        mFragmentManager = fm;

        fragmentTags = new HashMap<>();
    }

    @Override
    public Fragment getItem(int i) {

        Log.i(TAG, "getItem");
        Log.i(TAG, "item: " + String.valueOf(i));

        int monthDiff = i - CalendarViewPager.OFFSET;

        Log.i(TAG, "monthDiff: " + String.valueOf(monthDiff));

        Calendar monthCalendar = (Calendar)fixedCalendar.clone();
        monthCalendar.set(Calendar.MONTH, fixedCalendar.get(Calendar.MONTH) + monthDiff);

        Log.i(TAG, "fixed: " + fixedCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH));
        Log.i(TAG, "month: " + monthCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH));

        CalendarFragment calendarFragment = CalendarFragment.newInstance();
        calendarFragment.setCalendar(context, monthCalendar);

//        mFragmentManager.ge

        return calendarFragment;
    }

    public void hide(int position) {
        Log.i(TAG, "hide: " + String.valueOf(position));
        Fragment fragment = getItem(position);

        List<Fragment> fragments = mFragmentManager.getFragments();
        if (fragments != null) {
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            for (Fragment f: fragments) {
                if (f != null) {
                    ft.hide(f);
                }
            }

            ft.commit();
        }
        count = 0 ;
        notifyDataSetChanged();

        /*
        if (fragment != null) {


            FragmentTransaction ft = mFragmentManager.beginTransaction().hide(fragment);

            count = 0;
            notifyDataSetChanged();

            ft.commit();

        } else {
            Log.e(TAG, "fragment is null");
        }
        */
    }

    public void show(int position) {
        Log.i(TAG, "show: " + String.valueOf(position));
        Fragment fragment = getItem(position);
        /*
        if (fragment != null) {
            mFragmentManager.beginTransaction().show(fragment).commit();
        } else {
            Log.e(TAG, "fragment is null");
        }*/

        List<Fragment> fragments = mFragmentManager.getFragments();
        if (fragments != null) {
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            for (Fragment f: fragments) {
                if (f!=null) {
                    ft.show(f);
                }
            }

            ft.commit();
        }

        count = CalendarViewPager.OFFSET * 2;
        notifyDataSetChanged();
    }

    /*
    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        Log.i(TAG, "instantiateItem: " + String.valueOf(position));

        Object obj = super.instantiateItem(container, position);
        if (obj instanceof  Fragment) {
            Fragment f = (Fragment)obj;
            String tag = f.getTag();

            Log.i(TAG, "tag: " + tag);

            fragmentTags.put(position, tag);
        }

        return obj;
    }

    public Fragment getFragment(int position) {
        String tag = fragmentTags.get(position);
        if (tag == null) {
            return null;
        }

        return mFragmentManager.findFragmentByTag(tag);
    }
    */
    /*
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int virtualPosition = position % getRealCount();
        return monthPagerAdapter.instantiateItem(container, virtualPosition);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        int virtualPosition = position % getRealCount();
        monthPagerAdapter.destroyItem(container, virtualPosition, object);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        monthPagerAdapter.finishUpdate(container);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return monthPagerAdapter.isViewFromObject(view, object);
    }

    @Override
    public void restoreState(Parcelable bundle, ClassLoader classLoader) {
        monthPagerAdapter.restoreState(bundle, classLoader);
    }

    @Override
    public Parcelable saveState() {
        return monthPagerAdapter.saveState();
    }

    @Override
    public void startUpdate(ViewGroup container) {
        monthPagerAdapter.startUpdate(container);
    }
    */

    @Override
    public int getCount() {
        return count;
    }


}
