package com.ryan.unofficialhendrixapp.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.ryan.unofficialhendrixapp.R;
import com.ryan.unofficialhendrixapp.fragments.staff.StaffDetailFragment;
import com.ryan.unofficialhendrixapp.fragments.staff.StaffGridFragment;

public class StaffDetailActivity extends ActionBarActivity implements StaffGridFragment.OnPersonSelectedListener{
    private final String LOG_TAG = getClass().getSimpleName();

    public static final String DEPT_KEY = "dept_key";
    public static final String LETTER_KEY = "ltr_key";
    public static final String ID_KEY = "ID_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dir_detail);
        getSupportActionBar().setElevation(0);

        if ( savedInstanceState == null ) {
            setUpFirstFragment();
        }
    }

    private void setUpFirstFragment() {
        Fragment fragment;
        if (getIntent().hasExtra(DEPT_KEY)) {
            fragment = getDepartmentFragment();

        } else if (getIntent().hasExtra(LETTER_KEY)) {
            fragment = getGridFragment();

        } else {
            fragment = getStaffDetailFragment();
        }
        startFragment(fragment);
    }

    private Fragment getDepartmentFragment(){
        String dept = getIntent().getStringExtra(DEPT_KEY);
        return StaffGridFragment.newInstance(dept, null);
    }

    private Fragment getGridFragment(){
        String letter = getIntent().getStringExtra(LETTER_KEY);
        return StaffGridFragment.newInstance(null, letter);
    }

    private Fragment getStaffDetailFragment(){
        int id = getIntent().getIntExtra(ID_KEY, 0);
        return StaffDetailFragment.newInstance(id);
    }

    private void startFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_directory_detail_container, fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPersonSelected(long id) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_directory_detail_container, StaffDetailFragment.newInstance( (int) id) )
                .addToBackStack(null)
                .commit();
    }
}
