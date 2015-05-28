package com.gdilab.gnews.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gdilab.gnews.R;

import butterknife.ButterKnife;

/**
 * Created by masasdani on 1/22/15.
 */
public class GettingStartedFragment1 extends Fragment {

    public static GettingStartedFragment1 fragment;

    public static Fragment newInstance(Context context) {
        if(fragment == null)
            fragment = new GettingStartedFragment1();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.getting_started_1, container, false);
        ButterKnife.inject(this, view);

        return view;
    }

}
