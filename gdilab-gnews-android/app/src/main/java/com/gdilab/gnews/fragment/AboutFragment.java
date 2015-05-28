package com.gdilab.gnews.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gdilab.gnews.R;

import butterknife.ButterKnife;

/**
 * Created by masasdani on 12/21/14.
 */
public class AboutFragment extends DefaultFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about, container, false);
        ButterKnife.inject(this, view);

        View header = getActivity().getWindow().getDecorView().findViewById(R.id.toolbar);
        View rightItem = header.findViewById(R.id.right_item);
        rightItem.setVisibility(View.GONE);

        return view;
    }
    
}
