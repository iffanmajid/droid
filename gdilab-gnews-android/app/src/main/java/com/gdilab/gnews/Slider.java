package com.gdilab.gnews;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import com.flurry.android.FlurryAgent;
import com.gdilab.gnews.view.viewpager.ViewPagerAdapter;
import com.viewpagerindicator.CirclePageIndicator;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by masasdani on 12/17/14.
 */
public class Slider extends ActionBarActivity {

    @InjectView(R.id.viewPager)
    ViewPager viewPager;

    @InjectView(R.id.indicator)
    CirclePageIndicator indicator;

    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.slider);
        ButterKnife.inject(this);

        adapter = new ViewPagerAdapter(getApplicationContext(), getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FlurryAgent.init(this, Gnews.FLURRY_API);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
    }

}
