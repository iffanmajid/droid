package com.gdilab.gnews.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.gdilab.gnews.R;
import com.github.androidprogresslayout.ProgressLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by masasdani on 12/21/14.
 */
public class ArticleDetailFragment extends DefaultFragment {

    @InjectView(R.id.webView)
    WebView webView;

    @InjectView(R.id.crossfade_loading)
    RelativeLayout crossfadeLoading;

    @InjectView(R.id.scroll_view)
    ScrollView scrollView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.article_detail_fragment, container,
                false);
        ButterKnife.inject(this, view);

        Bundle bundle = getActivity().getIntent().getExtras();
        String url = bundle.getString("url");
//        url = "http://api.gnews.io/read?url="+url;
        Log.d("url", url);
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                crossfadeLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                crossfadeLoading.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Toast.makeText(getActivity(), description, Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }

        });

        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        return view;
    }

}
