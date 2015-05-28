package com.gdilab.gnews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

import com.flurry.android.FlurryAgent;
import com.gdilab.gnews.fragment.RegisterFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by masasdani on 12/3/14.
 */
public class Register extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.blank);
        ButterKnife.inject(this);

        if(savedInstanceState == null){
            changeView(new RegisterFragment());
        }
    }

    private void changeView(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
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
