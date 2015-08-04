package com.ryan.unofficialhendrixapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ryan.unofficialhendrixapp.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.action_settings);
        setUpActionBar();
        setContentView(R.layout.activities_settingsactivity);
    }

    private void setUpActionBar() {
        if ( getSupportActionBar() != null )
            getSupportActionBar().setElevation(0);
    }
}