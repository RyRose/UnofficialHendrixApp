package com.ryan.unofficialhendrixapp.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.os.ResultReceiver;

import com.ryan.unofficialhendrixapp.data.HendrixContract;
import com.ryan.unofficialhendrixapp.helpers.StaffParser;
import com.ryan.unofficialhendrixapp.models.Staff;

import java.util.ArrayList;

/**
 * Created by ryan on 1/15/15.
 */
public class StaffDatabaseService extends IntentService {
    private static final String LOG_TAG = "StaffDatabaseService";
    public static final String RECEIVER_KEY = "staff_receiver_key";

    public StaffDatabaseService() {
        super(LOG_TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if ( didFillStaffTable() ) {
            ((ResultReceiver) intent.getParcelableExtra(RECEIVER_KEY)).send(0, null); // notifies ByDept done
        }
    }

    // returns true upon success, false otherwise
    private boolean didFillStaffTable() {
        ArrayList<Staff> staffList = new StaffParser(getApplicationContext()).getList();
        if ( staffList.isEmpty() ) {
            return false;
        }
        addToDatabase(staffList);
        return true;
    }

    private void addToDatabase( ArrayList<Staff> staffList) {
        ContentValues values;

        getContentResolver().delete(HendrixContract.StaffColumn.CONTENT_URI, null, null);
        for ( Staff staff : staffList) {
            values = new ContentValues();
            values.put(HendrixContract.StaffColumn.COLUMN_PICTURE, staff.link);
            values.put(HendrixContract.StaffColumn.COLUMN_NAME, staff.name);
            values.put(HendrixContract.StaffColumn.COLUMN_TITLE, staff.title);
            values.put(HendrixContract.StaffColumn.COLUMN_DEPARTMENT, staff.dept);
            values.put(HendrixContract.StaffColumn.COLUMN_PHONE, staff.phone);
            values.put(HendrixContract.StaffColumn.COLUMN_EMAIL, staff.email);
            values.put(HendrixContract.StaffColumn.COLUMN_LOCATION_LINE_1, staff.location_line_1);
            values.put(HendrixContract.StaffColumn.COLUMN_LOCATION_LINE_2, staff.location_line_2);
            getContentResolver().insert(HendrixContract.StaffColumn.CONTENT_URI, values);
        }
    }
}
