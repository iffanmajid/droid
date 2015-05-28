package com.gdilab.gnews.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.devspark.appmsg.AppMsg;
import com.gdilab.gnews.Main;
import com.gdilab.gnews.R;
import com.gdilab.gnews.config.AppPreference;
import com.gdilab.gnews.config.DefaultMessage;
import com.gdilab.gnews.model.api.NewChannel;
import com.gdilab.gnews.service.RestService;
import com.gdilab.gnews.service.RestServiceImpl;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.devland.esperandro.Esperandro;

/**
 * Created by masasdani on 12/18/14.
 */
public class InputChannelFragment extends DefaultFragment {

    String keyword;

    @InjectView(R.id.channel_name)
    EditText channelNameEditText;

    @InjectView(R.id.channel_desc)
    EditText channelDescEditText;

    private AppPreference appPreference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        appPreference = Esperandro.getPreferences(AppPreference.class, getActivity());

        View view = inflater.inflate(R.layout.input_channel, container, false);
        ButterKnife.inject(this, view);

        Bundle bundle = getArguments();
        keyword = bundle.getString("keyword");

        return view;

    }

    @OnClick(R.id.next)
    public void next(){
        NewChannel newChannel = new NewChannel();
        newChannel.setAccessToken(appPreference.accessToken());
        newChannel.setKeywords(keyword.split(","));
        newChannel.setName(channelNameEditText.getText().toString());
        newChannel.setDescription(channelDescEditText.getText().toString());

        RestService restService = new RestServiceImpl();
        boolean success = restService.ownChannel(newChannel);
        if(success)
            switchToMainActivity();
        else
            AppMsg.makeText(getActivity(), DefaultMessage.ERROR_MESSAGE, AppMsg.STYLE_ALERT).show();
    }

    private void switchToMainActivity() {
        getActivity().finish();
        startActivity(new Intent(getActivity(), Main.class));
    }

    @OnClick(R.id.back)
    public void back(){
        getFragmentManager().popBackStack();
    }

}
