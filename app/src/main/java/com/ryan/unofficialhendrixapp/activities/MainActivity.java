package com.ryan.unofficialhendrixapp.activities;

import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ryan.unofficialhendrixapp.R;
import com.ryan.unofficialhendrixapp.fragments.NewsFragment;
import com.ryan.unofficialhendrixapp.fragments.directory.DepartmentFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.left_drawer) ListView mDrawerView;
    @InjectView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mActionBarToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        mActionBarToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);

        mDrawerView.setAdapter(new ArrayAdapter<String>(this, R.layout.activity_main_drawer_list_item, R.id.drawer_item, getResources().getStringArray(R.array.drawer_names)));
        mDrawerView.setOnItemClickListener( new DrawerItemClickListener() );

        mDrawerLayout.setDrawerListener(mActionBarToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if ( savedInstanceState == null ) {
            mDrawerView.setItemChecked(1, true);
            getFragmentManager().beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.fragment_main_container, DepartmentFragment.newInstance(1, getApplicationContext()))
                    .commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mActionBarToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (mActionBarToggle.onOptionsItemSelected(item)) {
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mActionBarToggle.syncState();
    }


    private class DrawerItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Fragment fragment;
            switch ( position ) {
                case 0:
                    fragment = NewsFragment.newInstance(position, getApplicationContext());
                    break;
                case 1:
                    fragment = DepartmentFragment.newInstance(position, getApplicationContext());
                    break;
                default:
                    fragment = NewsFragment.newInstance(position, getApplicationContext());
                    break;
            }

            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_main_container, fragment)
                    .commit();

            mDrawerView.setItemChecked(position, true);
            setTitle( getResources().getStringArray(R.array.drawer_names)[position] );
            mDrawerLayout.closeDrawer(mDrawerView);
        }
    }

}
