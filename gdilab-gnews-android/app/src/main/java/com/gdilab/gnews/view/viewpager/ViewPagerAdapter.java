package com.gdilab.gnews.view.viewpager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.gdilab.gnews.fragment.GettingStartedFragment1;
import com.gdilab.gnews.fragment.GettingStartedFragment2;
import com.gdilab.gnews.fragment.GettingStartedFragment3;
import com.gdilab.gnews.fragment.SelectLanguageFragment;
import com.gdilab.gnews.fragment.SigninFragment;

/**
 * Created by masasdani on 1/22/15.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    public static int totalPage=4;

    public ViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return GettingStartedFragment1.newInstance(context);
            case 1:
                return GettingStartedFragment2.newInstance(context);
            case 2:
                return GettingStartedFragment3.newInstance(context);
            case 3:
                return SelectLanguageFragment.newInstance(context);
        }
        return new Fragment();
    }

    @Override
    public int getCount() {
        return totalPage;
    }

}
