package com.ryan.unofficialhendrixapp.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.ryan.unofficialhendrixapp.R;

public class SettingsFragment extends PreferenceFragment {
    private final String LOG_TAG = getClass().getSimpleName();

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}