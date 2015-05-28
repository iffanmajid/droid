package com.gdilab.gnews;

import android.app.Application;

import com.flurry.android.FlurryAgent;

/**
 * Created by masasdani on 4/23/15.
 */
public class Gnews extends Application {

    public static final String FLURRY_API= "HWD2PSXMZYT7HWBXXWPK";

    @Override
    public void onCreate() {
        super.onCreate();
        FlurryAgent.init(this, FLURRY_API);
    }
}
