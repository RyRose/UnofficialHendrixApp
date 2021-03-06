package com.ryan.unofficialhendrixapp.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ryan.unofficialhendrixapp.R;
import com.ryan.unofficialhendrixapp.fragments.nav_drawer.BaseNavDrawerFragment;
import com.ryan.unofficialhendrixapp.fragments.nav_drawer.MapFragment;
import com.ryan.unofficialhendrixapp.fragments.nav_drawer.NewsFragment;
import com.ryan.unofficialhendrixapp.fragments.nav_drawer.TabbedStaffFragment;
import com.ryan.unofficialhendrixapp.fragments.nav_drawer.WebFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

// Tablet support
// TODO: Setup  navigation drawer as always open for tablet landscape
// TODO: Setup dual pane b/w department/letter and StaffDetail fragment for tablet landscape. Put this into onPersonSelected().

public class MainActivity extends BaseActivity {

    private final int NEWS_LOCATION = 0;
    private final int STAFF_DIRECTORY_LOCATION = 1;
    private final int CAMPUS_WEB_LOCATION = 2;
    private final int CAMPUS_MAP_LOCATION = 3;

    @InjectView(R.id.left_drawer)
    ListView mDrawerView;

    @InjectView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    private ActionBarDrawerToggle mActionBarToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_mainactivity);
        ButterKnife.inject(this);
        setUpNavDrawer();

        if ( savedInstanceState == null ) {
            setUpDefaultFragment();
        }

    }

    private void setUpNavDrawer() {
        mActionBarToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
        String[] drawerItems = getResources().getStringArray(R.array.drawer_names);
        ArrayAdapter navDrawerAdapter = new ArrayAdapter<>(this, R.layout.mainactivity_nav_item, R.id.drawer_item, drawerItems);
        mDrawerView.setAdapter(navDrawerAdapter);
        mDrawerView.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout.setDrawerListener(mActionBarToggle);
    }

    private void setUpDefaultFragment() {
        mDrawerView.setItemChecked(NEWS_LOCATION, true);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_main_container, BaseNavDrawerFragment.newInstance( new NewsFragment(), NEWS_LOCATION))
                .commit();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mActionBarToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mActionBarToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActionBarToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        BaseNavDrawerFragment frag = (BaseNavDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_main_container);
        if (frag.canGoBack()) {
            frag.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    private class DrawerItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Fragment fragment;
            switch ( position ) {
                case NEWS_LOCATION:
                    fragment = BaseNavDrawerFragment.newInstance( new NewsFragment(), position);
                    break;
                case STAFF_DIRECTORY_LOCATION:
                    fragment = BaseNavDrawerFragment.newInstance( new TabbedStaffFragment(), position);
                    break;
                case CAMPUS_WEB_LOCATION:
                    fragment = BaseNavDrawerFragment.newInstance( new WebFragment(), position);
                    break;
                case CAMPUS_MAP_LOCATION:
                    fragment = BaseNavDrawerFragment.newInstance( new MapFragment(), position);
                    break;
                default:
                    throw new IllegalArgumentException("Option not accounted for in Nav Drawer");
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_main_container, fragment)
                    .commit();

            mDrawerView.setItemChecked(position, true);
            mDrawerLayout.closeDrawer(mDrawerView);
        }
    }
}
