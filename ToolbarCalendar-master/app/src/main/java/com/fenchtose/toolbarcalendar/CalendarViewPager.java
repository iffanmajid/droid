package com.fenchtose.toolbarcalendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Jay Rambhia on 14/04/15.
 */
public class CalendarViewPager extends ViewPager {

    private int datesInMonth = 0;
    private int rowHeight = 0;
    private int topPadding;

    public static int OFFSET = 1000;

    private CalendarPagerAdapter mPagerAdapter;
    private CalendarFragment currentFragment;

    private int width;

    private boolean collapsed = false;
    private boolean switched = false;

    private final String TAG = "CalendarViewPager";

    public CalendarViewPager(Context context) {
        super(context);
    }

    public CalendarViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        int[] attributes = {android.R.attr.paddingTop};
        TypedArray arr = context.obtainStyledAttributes(attrs, attributes);
        topPadding = arr.getDimensionPixelOffset(0, 130);

    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
        if (adapter instanceof CalendarPagerAdapter) {
            mPagerAdapter = (CalendarPagerAdapter)adapter;
//            mPagerAdapter.get
        }

        setCurrentItem(OFFSET);
        collapse();
    }

    public void setDatesInMonth(int num) {
        datesInMonth = num;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = widthMeasureSpec;
        currentFragment = null;
        getCurrentFragment();

        /*
        if (isCollapsed()) {
            Log.i(TAG, "isCollapsed: " + String.valueOf(topPadding));
            width = getMeasuredWidth();
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(topPadding, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        } else {
            Log.i(TAG, "expanded");
        }*/

        datesInMonth = getCurrentFragment().getDatesInMonth();

        int rows = datesInMonth/7;

        int mod = datesInMonth % 7;
        if (mod > 0) {
            rows++;
        }

        boolean wrapHeight = MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST;

        if (isCollapsed()) {
            rows = 0;
        }

        Log.i(TAG, "wrapHeight: " + String.valueOf(wrapHeight));
        Log.i(TAG, "rowHeight: " + String.valueOf(rowHeight));
        Log.i(TAG, "rows: " + String.valueOf(rows));

        int height = getMeasuredHeight();
//        rowHeight = 0;
        if ((wrapHeight || switched) && rowHeight == 0 && rows != 0) {

            if (switched) {
                switched = false;
            }

            int width = getMeasuredWidth();

            widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);

            if (getChildCount() > 0) {
                View firstChild = getChildAt(0);

                firstChild.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height,
                                        MeasureSpec.AT_MOST));

                height = firstChild.getMeasuredHeight();

                Log.i(TAG, "child measured height: " + String.valueOf(height));

                rowHeight = height/rows;
            } else {
                Log.e(TAG, "child count is 0");
            }
        }

        int calHeight = rowHeight * rows;
        calHeight += topPadding; // add top padding.

//        Log.i(TAG, "calHeight = " + String.valueOf(calHeight));

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(calHeight, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public String getCurrentMonth() {
        return getCurrentFragment().getMonthName();
    }

    public String getCurrentYear() {
        return String.valueOf(getCurrentFragment().getYear());
    }

    public CalendarFragment getCurrentFragment() {
//        if (currentFragment == null) {
            currentFragment = (CalendarFragment)mPagerAdapter.getItem(getCurrentItem());
//        }

        return currentFragment;
    }

    public void collapse() {
        collapsed = true;
        mPagerAdapter.hide(getCurrentItem());
        measure(width, topPadding);

//        onMeasure(width, topPadding);
    }

    public void expand() {
        Log.i(TAG, "expand");
        collapsed = false;
        rowHeight = 0;
        switched = true;
        mPagerAdapter.show(getCurrentItem());
        measure(width, topPadding);
    }

    public boolean isCollapsed() {
        return collapsed;
    }
}
