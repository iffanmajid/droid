package com.gdilab.gnews.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gdilab.gnews.Helper;
import com.gdilab.gnews.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by masasdani on 12/21/14.
 */
public class SigninFragment extends DefaultFragment {

    public static SigninFragment fragment;

    public static Fragment newInstance(Context context) {
        if(fragment == null)
            fragment = new SigninFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signin, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @OnClick(R.id.twitter_button)
    public void next(){
        changeView(new TwitterLogin());
    }


    @OnClick(R.id.terms_of_service)
    public void termOfService(){
        Intent intent = new Intent(getActivity(), Helper.class);
        Bundle bundle = new Bundle();
        bundle.putString("url", "file:///android_asset/tnc.html");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @OnClick(R.id.privacy_policy)
    public void privacyPolicy(){
        Intent intent = new Intent(getActivity(), Helper.class);
        Bundle bundle = new Bundle();
        bundle.putString("url", "file:///android_asset/pp.html");
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
