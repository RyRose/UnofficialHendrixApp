package com.ryan.unofficialhendrixapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.ryan.unofficialhendrixapp.R;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpActionBar();
    }

    private void setUpActionBar() {
        if( getSupportActionBar() != null ) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                new MaterialDialog.Builder(this) // TODO: Move into SettingsFragment
                        .title(R.string.about_title)
                        .content(R.string.about_content)
                        .theme(Theme.LIGHT)
                        .titleColor(R.color.primary_text_default_material_light)
                        .contentColor(R.color.primary_text_default_material_light)
                        .positiveText(R.string.dialog_close)
                        .show();
                return true;
            case R.id.action_settings:
                startActivity( new Intent(this, SettingsActivity.class) );
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
