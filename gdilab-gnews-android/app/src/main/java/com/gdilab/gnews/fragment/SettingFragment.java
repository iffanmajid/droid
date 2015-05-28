package com.gdilab.gnews.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.gdilab.gnews.R;
import com.gdilab.gnews.Splash;
import com.gdilab.gnews.config.AppPreference;
import com.gdilab.gnews.service.AppService;
import com.gdilab.gnews.view.RoundedImageView;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.devland.esperandro.Esperandro;

/**
 * Created by masasdani on 1/12/15.
 */
public class SettingFragment extends DefaultFragment {

    @InjectView(R.id.country)
    Spinner countrySpinner;

    @InjectView(R.id.fullname)
    TextView fullnameTextView;

    @InjectView(R.id.username)
    TextView usernameTextView;

    @InjectView(R.id.thumbnail)
    RoundedImageView thumbnail;

    private AppPreference appPreference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        appPreference = Esperandro.getPreferences(AppPreference.class, getActivity());

        View view = inflater.inflate(R.layout.setting, container, false);
        ButterKnife.inject(this, view);

        View header = getActivity().getWindow().getDecorView().findViewById(R.id.toolbar);
        View rightItem = header.findViewById(R.id.right_item);
        rightItem.setVisibility(View.GONE);


        if(appPreference.loggedIn()){
            Picasso.with(getActivity())
                    .load(appPreference.profilePicture())
                    .into(thumbnail);
            fullnameTextView.setText(appPreference.fullname());
            usernameTextView.setText("@" + appPreference.twitterUsername());
        }

        if(appPreference.country().equalsIgnoreCase("Indonesia"))
            countrySpinner.setSelection(0);
        else
            countrySpinner.setSelection(1);

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    appPreference.country("Indonesia");
                } else {
                    appPreference.country("United States");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    @OnClick(R.id.logout)
    public void logout(){
        AppService appService = new AppService(appPreference);
        appService.logout();
        getActivity().finish();
        startActivity(new Intent(getActivity(), Splash.class));
    }

}
