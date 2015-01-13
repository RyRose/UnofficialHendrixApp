package com.ryan.unofficialhendrixapp.fragments.staff;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import com.ryan.unofficialhendrixapp.activities.StaffDetailActivity;
import com.ryan.unofficialhendrixapp.adapters.staff.ByDeptAdapter;
import com.ryan.unofficialhendrixapp.data.HendrixContract;
import com.ryan.unofficialhendrixapp.helpers.StaffParser;
import com.ryan.unofficialhendrixapp.models.Staff;

import java.util.ArrayList;

public class ByDeptFragment extends BaseByCategoryFragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private final String LOG_TAG = getClass().getSimpleName();


    private static final String[] DEPARTMENT_COLUMNS = {
            HendrixContract.StaffColumn._ID,
            HendrixContract.StaffColumn.COLUMN_DEPARTMENT,
    };

    public static final int COL_DEPARTMENT_ID = 0;
    public static final int COL_DEPARTMENT_NAME = 1;

    public static ByDeptFragment newInstance() {
        ByDeptFragment fragment = new ByDeptFragment();
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new FillStaffTable(this).execute(DEPARTMENT_COLUMNS);
        setListAdapter( new ByDeptAdapter(getActivity(), null) );
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Cursor c = ( (ByDeptAdapter) getListAdapter()).getCursor();
        String dept = c.getString(COL_DEPARTMENT_NAME);
        Intent intent = new Intent(getActivity(), StaffDetailActivity.class);
        intent.putExtra(StaffDetailActivity.DEPT_KEY, dept);
        getActivity().startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new android.support.v4.content.CursorLoader(
                getActivity(),
                HendrixContract.StaffColumn.CONTENT_URI_WITH_DISTINCT,
                DEPARTMENT_COLUMNS,
                null,
                null,
                HendrixContract.StaffColumn.COLUMN_DEPARTMENT + " ASC");

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ( (ByDeptAdapter) getListAdapter() ).swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ( (ByDeptAdapter) getListAdapter() ).swapCursor(null);
    }

    private class FillStaffTable extends AsyncTask<String, Void, Boolean> {

        Fragment mFragment;

        public FillStaffTable(Fragment fragment) {
            mFragment = fragment;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            if ( canFillStaffTable(params) ) {
                fillStaffTable();
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean bool) {
            super.onPostExecute(bool);
            if(bool) {
                getLoaderManager().restartLoader(0, null, (LoaderManager.LoaderCallbacks) mFragment);
            }
            mFragment = null;
        }

        private boolean canFillStaffTable(String... params) {
            Cursor c = getActivity().getContentResolver().query(HendrixContract.StaffColumn.CONTENT_URI,
                    params,
                    null,
                    null,
                    null);

            if (c == null) {
                return true;
            } else {
                boolean bool = c.moveToFirst();
                c.close();
                return !bool;
            }
        }

        private void fillStaffTable() {
            ContentValues values;
            ArrayList<Staff> staffList = new StaffParser(getActivity()).parse();

            getActivity().getContentResolver().delete(HendrixContract.StaffColumn.CONTENT_URI, null, null);
            for ( Staff staff : staffList) {
                values = new ContentValues();
                values.put(HendrixContract.StaffColumn.COLUMN_PICTURE, staff.getLink());
                values.put(HendrixContract.StaffColumn.COLUMN_NAME, staff.getName());
                values.put(HendrixContract.StaffColumn.COLUMN_TITLE, staff.getTitle());
                values.put(HendrixContract.StaffColumn.COLUMN_DEPARTMENT, staff.getDept());
                values.put(HendrixContract.StaffColumn.COLUMN_PHONE, staff.getPhone());
                values.put(HendrixContract.StaffColumn.COLUMN_EMAIL, staff.getEmail());
                values.put(HendrixContract.StaffColumn.COLUMN_LOCATION_LINE_1, staff.getline1());
                values.put(HendrixContract.StaffColumn.COLUMN_LOCATION_LINE_2, staff.getline2());
                mFragment.getActivity().getContentResolver().insert(HendrixContract.StaffColumn.CONTENT_URI, values);
            }

        }
    }
}
