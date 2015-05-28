package com.gdilab.gnews.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.devspark.appmsg.AppMsg;
import com.gdilab.gnews.R;
import com.gdilab.gnews.config.AppPreference;
import com.gdilab.gnews.config.DefaultMessage;
import com.gdilab.gnews.model.api.CreateKeywordForm;
import com.gdilab.gnews.service.RestService;
import com.gdilab.gnews.service.RestServiceImpl;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Regex;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.mobsandgeeks.saripaar.annotation.TextRule;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.devland.esperandro.Esperandro;

/**
 * Created by masasdani on 12/18/14.
 */
public class InputKeywordFragment extends DefaultFragment implements Validator.ValidationListener {

    Validator validator;

    private ProgressDialog progressDialog;

    @Required(order = 1)
    @TextRule(order = 2, minLength = 3, message = DefaultMessage.MIN_KEYOWRD_LENGHT_ERROR)
    @Regex(order = 3, pattern = "[a-zA-z0-9\\s]*", message = "Use only alphanumeric and space")
    @InjectView(R.id.keyword)
    EditText keywordEditText;

    private AppPreference appPreference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        appPreference = Esperandro.getPreferences(AppPreference.class, getActivity());

        View view = inflater.inflate(R.layout.input_keyword, container, false);
        ButterKnife.inject(this, view);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        validator = new Validator(this);
        validator.setValidationListener(this);

        return view;
    }

    @OnClick(R.id.next)
    public void next(){
        validator.validate();
    }

    @OnClick(R.id.back)
    public void back(){
        getFragmentManager().popBackStack();
    }

    @Override
    public void onValidationSucceeded() {
        validateKeyword();
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

    private void saveKeyword(){
        CreateKeywordForm createKeywordForm = new CreateKeywordForm();
        createKeywordForm.setAccessToken(appPreference.accessToken());
        createKeywordForm.setName(keywordEditText.getText().toString());
        RestService restService = new RestServiceImpl();
        boolean result = restService.addKeyword(createKeywordForm);
        if(result){
            getFragmentManager().popBackStack();
        }else{
            AppMsg.makeText(
                    getActivity(),
                    DefaultMessage.ERROR_MESSAGE,
                    AppMsg.STYLE_ALERT).show();
        }
    }

    private void validateKeyword(){
        RestService restService = new RestServiceImpl();
        boolean result = restService.validateKeyword(appPreference.accessToken(), keywordEditText.getText().toString());
        if(result){
            progressDialog.show();
            new SaveDataTask().execute();
        }else{
            keywordEditText.setError(DefaultMessage.ERROR_VALIDATE_KEYWORD);
        }
    }

    private class SaveDataTask extends AsyncTask<Void, Boolean, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                if(isCancelled()){
                    return false;
                }
                saveKeyword();
                return true;
            }catch (Exception e){
                onDestroy();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                progressDialog.dismiss();
                super.onPostExecute(result);
            }
        }

    }

}
