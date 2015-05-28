package com.ryan.unofficialhendrixapp.fragments.staff_categories;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.ryan.unofficialhendrixapp.activities.StaffDetailActivity;
import com.ryan.unofficialhendrixapp.data.HendrixContract.StaffColumn;

public class ByDeptFragment extends BaseByCategoryFragment {
    private final String LOG_TAG = getClass().getSimpleName();


    public static final String[] DEPARTMENT_COLUMNS = {
            StaffColumn._ID,
            StaffColumn.COLUMN_DEPARTMENT,
    };

    public static final int COL_DEPARTMENT_DEPT = 1;

    public static ByDeptFragment newInstance() {
        ByDeptFragment fragment = new ByDeptFragment();
        return fragment;
    }

    @Override
    int getColumnNumber() {
        return COL_DEPARTMENT_DEPT;
    }

    @Override
    String getStaffDetailKey() {
        return StaffDetailActivity.DEPT_KEY;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                StaffColumn.CONTENT_URI_WITH_DISTINCT_DEPARTMENT,
                DEPARTMENT_COLUMNS,
                null,
                null,
                StaffColumn.COLUMN_DEPARTMENT + " ASC");

    }
}
