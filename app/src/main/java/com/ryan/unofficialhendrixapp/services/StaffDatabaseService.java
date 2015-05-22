package com.ryan.unofficialhendrixapp.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.ryan.unofficialhendrixapp.R;
import com.ryan.unofficialhendrixapp.data.HendrixContract.StaffColumn;
import com.ryan.unofficialhendrixapp.parse.StaffParser;
import com.ryan.unofficialhendrixapp.models.Staff;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class StaffDatabaseService extends IntentService {
    private static final String LOG_TAG = "StaffDatabaseService";
    public static final String INITIAL_STAFF_FILL_KEY = "isFirstRun";

    public StaffDatabaseService() {
        super(LOG_TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            ArrayList<Staff> staffList = new StaffParser(getApplicationContext()).getList();
            addToDatabase(staffList);
            getSharedPreferences( getString(R.string.prefs), MODE_PRIVATE).edit().putBoolean(INITIAL_STAFF_FILL_KEY, false).apply();
            Log.d(LOG_TAG, "finished pulling staff");
        } catch (XmlPullParserException | IOException e) {
            Toast.makeText(getApplicationContext(), getString(R.string.staff_database_error), Toast.LENGTH_LONG).show();
        }
    }

    private void addToDatabase( ArrayList<Staff> staffList) {
        ContentValues values;

        getContentResolver().delete(StaffColumn.CONTENT_URI, null, null);
        for ( Staff staff : staffList) {
            values = new ContentValues();
            values.put(StaffColumn.COLUMN_PICTURE, staff.link);
            values.put(StaffColumn.COLUMN_FULL_NAME, staff.full_name);
            values.put(StaffColumn.COLUMN_LAST_NAME, staff.last_name);
            values.put(StaffColumn.COLUMN_TITLE, staff.title);
            values.put(StaffColumn.COLUMN_DEPARTMENT, staff.dept);
            values.put(StaffColumn.COLUMN_PHONE, staff.phone);
            values.put(StaffColumn.COLUMN_EMAIL, staff.email);
            values.put(StaffColumn.COLUMN_LOCATION_LINE_1, staff.location_line_1);
            values.put(StaffColumn.COLUMN_LOCATION_LINE_2, staff.location_line_2);
            getContentResolver().insert(StaffColumn.CONTENT_URI, values);
        }
    }
}
