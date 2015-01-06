package com.ryan.unofficialhendrixapp.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.ryan.unofficialhendrixapp.R;
import com.ryan.unofficialhendrixapp.fragments.directory.PersonDetailFragment;
import com.ryan.unofficialhendrixapp.fragments.directory.PersonGridFragment;

public class DirectoryDetailActivity extends ActionBarActivity {
    private final String LOG_TAG = getClass().getSimpleName();

    public static final String DEPT_KEY = "dept_key";
    public static final String LETTER_KEY = "ltr_key";
    public static final String NAME_KEY = "name_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory_detail);

        if ( savedInstanceState == null) {
            Fragment fragment;
            if (getIntent().hasExtra(DEPT_KEY)) {
                String dept = getIntent().getStringExtra(DEPT_KEY);
                fragment = PersonGridFragment.newInstance(dept, null);

            } else if (getIntent().hasExtra(LETTER_KEY)) {
                String letter = getIntent().getStringExtra(LETTER_KEY);
                fragment = PersonGridFragment.newInstance(null, letter);

            } else {
                String name = getIntent().getStringExtra(NAME_KEY);
                fragment = PersonDetailFragment.newInstance(name);
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_directory_detail_container, fragment)
                    .commit();
        }
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
}
