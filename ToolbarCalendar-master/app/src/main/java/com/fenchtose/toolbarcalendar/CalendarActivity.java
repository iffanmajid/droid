package com.fenchtose.toolbarcalendar;

import android.app.Fragment;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;


public class CalendarActivity extends ActionBarActivity {

    private static final String TAG = "CalendarActivity";
    private Toolbar toolbar;

    private CalendarViewPager viewPager;
    private PagerAdapter pagerAdapter;

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        setupToolbar();

        viewPager = (CalendarViewPager)findViewById(R.id.cal_pager);
        pagerAdapter = new CalendarPagerAdapter(getApplicationContext(),
                                        getSupportFragmentManager(), Calendar.getInstance());
        viewPager.setAdapter(pagerAdapter);
//        viewPager.collapse();

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                Log.i(TAG, "current month: " + viewPager.getCurrentMonth());
                getSupportActionBar().setTitle(viewPager.getCurrentMonth() + " " + viewPager.getCurrentYear());
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        button = (Button)findViewById(R.id.switch_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!viewPager.isCollapsed()) {
                    viewPager.collapse();
                    button.setText("Expand");
                } else {
                    viewPager.expand();
                    button.setText("Collapse");
                }
            }
        });
        /*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                viewPager.expand();
            }
        }, 4000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                viewPager.collapse();
            }
        }, 8000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "Expanding");
                viewPager.expand();
            }
        }, 12000);
        */

    }

    private void setupToolbar() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Calendar Activity");
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Calendar");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calendar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
