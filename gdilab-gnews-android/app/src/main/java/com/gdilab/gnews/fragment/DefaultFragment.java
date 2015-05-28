package com.gdilab.gnews.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.gdilab.gnews.R;

/**
 * Created by masasdani on 12/18/14.
 */
public class DefaultFragment extends Fragment {

    public void changeView(Fragment fragment){
        changeView(fragment, null);
    }

    public void changeView(Fragment fragment, Bundle bundle){
        if(bundle != null){
            fragment.setArguments(bundle);
        }
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in,
                        R.anim.slide_out,
                        R.anim.pop_slide_in,
                        R.anim.pop_slide_out)
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void finishLoading(final View showView, final View hideView) {
        try{
            int mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
            showView.setAlpha(0f);
            showView.setVisibility(View.VISIBLE);

            showView.animate()
                    .alpha(1f)
                    .setDuration(mShortAnimationDuration)
                    .setListener(null);

            hideView.animate()
                    .alpha(0f)
                    .setDuration(mShortAnimationDuration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            hideView.setVisibility(View.GONE);
                        }
                    });
        }catch (Exception e){
            Log.e("finish loading", e.getMessage());
        }

    }


    public void startLoading(final View showView, final View hideView) {
        try{
            int mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
            showView.setAlpha(0f);
            showView.setVisibility(View.VISIBLE);

            showView.animate()
                    .alpha(1f)
                    .setDuration(mShortAnimationDuration)
                    .setListener(null);

            hideView.animate()
                    .alpha(0f)
                    .setDuration(mShortAnimationDuration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            hideView.setVisibility(View.GONE);
                        }
                    });
        }catch (Exception e){
            Log.e("start loading", e.getMessage());
        }

    }
}
