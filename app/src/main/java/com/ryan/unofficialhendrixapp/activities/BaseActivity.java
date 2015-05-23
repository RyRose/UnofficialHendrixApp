package com.ryan.unofficialhendrixapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.ryan.unofficialhendrixapp.R;
import com.ryan.unofficialhendrixapp.services.StaffDatabaseService;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpActionBar();

        SharedPreferences prefs = getSharedPreferences( getString(R.string.prefs), MODE_PRIVATE);
        boolean isFirstPull = prefs.getBoolean(StaffDatabaseService.INITIAL_STAFF_FILL_KEY, true);
        if ( isFirstPull )
            setUpStaffTable();
    }

    private void setUpActionBar() {
        if( getSupportActionBar() != null ) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    private void setUpStaffTable() {
        Intent intent = new Intent(this, StaffDatabaseService.class);
        getApplicationContext().startService(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { // TODO: Create settings menu and allow changing the rss link in case Hendrix changes it.
        if (item.getItemId() == R.id.action_about) {
            new MaterialDialog.Builder(this)
                    .title(R.string.about_title)
                    .content(R.string.about_content)
                    .theme(Theme.LIGHT)
                    .titleColor(R.color.primary_text_default_material_light)
                    .contentColor(R.color.primary_text_default_material_light)
                    .positiveText(R.string.dialog_close)
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
