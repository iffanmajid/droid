package com.gdilab.gnews.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.gdilab.gnews.Main;
import com.gdilab.gnews.R;
import com.gdilab.gnews.Signin;
import com.gdilab.gnews.config.AppConfig;
import com.gdilab.gnews.config.AppPreference;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.devland.esperandro.Esperandro;

/**
 * Created by masasdani on 3/18/15.
 */
public class SelectNewsTypeFragment extends DefaultFragment {

    private AppPreference appPreference;

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        appPreference = Esperandro.getPreferences(AppPreference.class, getActivity());

        View view = inflater.inflate(R.layout.select_news_type, container, false);
        ButterKnife.inject(this, view);

        return view;
    }

    @OnClick(R.id.keyword)
    public void keyword(){
        appPreference.firstTimeOpen(false);
        Intent intent = new Intent(getActivity(), Signin.class);
        startActivityForResult(intent, AppConfig.SIGNIN_REQUEST_CODE);
    }

    @OnClick(R.id.popular)
    public void popular(){
        appPreference.firstTimeOpen(false);
        getActivity().finish();
        startActivity(new Intent(getActivity(), Main.class));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AppConfig.SIGNIN_REQUEST_CODE && resultCode == AppConfig.RESULT_SUCCESS){
            getActivity().finish();
            startActivity(new Intent(getActivity(), Main.class));
        }
    }
}
