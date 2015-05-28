package com.gdilab.gnews.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import com.devspark.appmsg.AppMsg;
import com.gdilab.gnews.Main;
import com.gdilab.gnews.R;
import com.gdilab.gnews.config.AppPreference;
import com.gdilab.gnews.config.DefaultMessage;
import com.gdilab.gnews.model.api.Credential;
import com.gdilab.gnews.model.api.RegisterBasic;
import com.gdilab.gnews.service.RestService;
import com.gdilab.gnews.service.RestServiceImpl;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Required;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.devland.esperandro.Esperandro;

/**
 * Created by masasdani on 12/18/14.
 */
public class RegisterFragment extends DefaultFragment implements Validator.ValidationListener {

    Validator validator;

    @Required(order = 1)
    @Email(order = 2, message = DefaultMessage.EMAIL_VALIDATION_MESSAGE)
    @InjectView(R.id.email)
    EditText email;

    @Required(order = 3)
    @InjectView(R.id.password)
    EditText password;

    @InjectView(R.id.country)
    Spinner country;

    private AppPreference appPreference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        appPreference = Esperandro.getPreferences(AppPreference.class, getActivity());

        View view = inflater.inflate(R.layout.register, container, false);
        ButterKnife.inject(this, view);

        validator = new Validator(this);
        validator.setValidationListener(this);

        return view;
    }

    @OnClick(R.id.next)
    public void next(){
        validator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        register(email.getText().toString(),
                password.getText().toString(),
                country.getSelectedItem().toString());

        changeView(new InputKeywordFragmentNew());
    }

    private void register(String email, String password, String country){
        RestService restService = new RestServiceImpl();
        RegisterBasic registerBasic = new RegisterBasic();
        registerBasic.setEmail(email);
        registerBasic.setPassword(password);
        registerBasic.setCountry(country);
        registerBasic.setAccessToken(appPreference.accessToken());

        Credential credential = restService.registerBasic(registerBasic);
        appPreference.finishedProfile(true);
        appPreference.email(credential.getEmail());
        appPreference.country(country);

    }

    @Override
    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        String message = failedRule.getFailureMessage();

        if (failedView instanceof EditText) {
            failedView.requestFocus();
            ((EditText) failedView).setError(message);
        } else {
            AppMsg.makeText(getActivity(), message, AppMsg.STYLE_ALERT).show();
        }
    }
}
