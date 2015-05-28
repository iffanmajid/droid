package com.hello.iffan.mynavigationdrawer;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends Activity {
    private DrawerLayout drawergue;
    private ListView mylistview;
    private String[] menugue;
    private ActionBarDrawerToggle drawerlistener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawergue = (DrawerLayout) findViewById(R.id.drawer_layout);
        mylistview = (ListView) findViewById(R.id.drawerlist);

        menugue = getResources().getStringArray(R.array.mymenu);
        drawerlistener = new ActionBarDrawerToggle(this,drawergue,R.drawable.ic_drawer,
                R.string.navigation_drawer_open, R.string.navigation_drawer_open);
    }
}
