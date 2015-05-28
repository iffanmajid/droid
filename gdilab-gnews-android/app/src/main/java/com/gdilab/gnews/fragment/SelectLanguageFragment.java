package com.gdilab.gnews.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.gdilab.gnews.Main;
import com.gdilab.gnews.R;
import com.gdilab.gnews.config.AppPreference;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.devland.esperandro.Esperandro;

/**
 * Created by masasdani on 3/18/15.
 */
public class SelectLanguageFragment extends DefaultFragment {

    public static SelectLanguageFragment fragment;

    public static Fragment newInstance(Context context) {
        if(fragment == null)
            fragment = new SelectLanguageFragment();
        return fragment;
    }

    @InjectView(R.id.select_language)
    RadioGroup languageGroup;

    private AppPreference appPreference;

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        appPreference = Esperandro.getPreferences(AppPreference.class, getActivity());

        View view = inflater.inflate(R.layout.select_language, container, false);
        ButterKnife.inject(this, view);

        return view;
    }

    @OnClick(R.id.next)
    public void next(){
        String country =
                languageGroup.getCheckedRadioButtonId() == R.id.indonesia ? "Indonesia" : "United States";
        appPreference.country(country);
        changeView(new SelectNewsTypeFragment());
    }

}
