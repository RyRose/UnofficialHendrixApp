package com.ryan.unofficialhendrixapp.fragments.staff_categories;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.ryan.unofficialhendrixapp.activities.StaffDetailActivity;
import com.ryan.unofficialhendrixapp.data.HendrixContract.StaffColumn;

public class ByNameFragment extends BaseByCategoryFragment{
    public final String LOG_TAG = getClass().getSimpleName();

    public static final String[] LETTER_COLUMNS = {
            StaffColumn._ID,
            "substr(" + StaffColumn.COLUMN_LAST_NAME + ", 1, 1)",
    };

    public static final int COLUMN_LAST_NAME = 1;

    public static ByNameFragment newInstance() {
        return new ByNameFragment();
    }

    @Override
    int getColumnNumber() {
        return COLUMN_LAST_NAME;
    }

    @Override
    String getStaffDetailKey() {
        return StaffDetailActivity.NAME_KEY;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                StaffColumn.CONTENT_URI_WITH_GROUPED_LNAME_AND_FIRST_LETTER,
                LETTER_COLUMNS,
                null,
                null,
                StaffColumn.COLUMN_LAST_NAME + " ASC");

    }
}
