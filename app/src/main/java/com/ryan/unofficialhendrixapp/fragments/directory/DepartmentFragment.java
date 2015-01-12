package com.ryan.unofficialhendrixapp.fragments.directory;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import com.ryan.unofficialhendrixapp.activities.DirectoryDetailActivity;
import com.ryan.unofficialhendrixapp.adapters.directory.DepartmentAdapter;
import com.ryan.unofficialhendrixapp.data.HendrixContract;
import com.ryan.unofficialhendrixapp.helpers.DirectoryParser;
import com.ryan.unofficialhendrixapp.models.Person;

import java.util.ArrayList;

import icepick.Icepick;
import icepick.Icicle;

public class DepartmentFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private final String LOG_TAG = getClass().getSimpleName();

    @Icicle int mPosition = 0;

    private static final String[] DEPARTMENT_COLUMNS = {
            HendrixContract.StaffColumn._ID,
            HendrixContract.StaffColumn.COLUMN_DEPARTMENT,
    };

    public static final int COL_DEPARTMENT_ID = 0;
    public static final int COL_DEPARTMENT_NAME = 1;

    public static DepartmentFragment newInstance() {
        DepartmentFragment fragment = new DepartmentFragment();
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        new FillStaffTable(this).execute(DEPARTMENT_COLUMNS);

        setListAdapter( new DepartmentAdapter(getActivity(), null) );
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
        setSelection(mPosition);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPosition = getListView().getFirstVisiblePosition();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Cursor c = ( (DepartmentAdapter) getListAdapter()).getCursor();
        String dept = c.getString(COL_DEPARTMENT_NAME);
        Intent intent = new Intent(getActivity(), DirectoryDetailActivity.class);
        intent.putExtra(DirectoryDetailActivity.DEPT_KEY, dept);
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
        ( (DepartmentAdapter) getListAdapter() ).swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ( (DepartmentAdapter) getListAdapter() ).swapCursor(null);
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
            ArrayList<Person> personList = new DirectoryParser(getActivity()).parse();

            getActivity().getContentResolver().delete(HendrixContract.StaffColumn.CONTENT_URI, null, null);
            for ( Person person : personList ) {
                values = new ContentValues();
                values.put(HendrixContract.StaffColumn.COLUMN_PICTURE, person.getLink());
                values.put(HendrixContract.StaffColumn.COLUMN_NAME, person.getName());
                values.put(HendrixContract.StaffColumn.COLUMN_TITLE, person.getTitle());
                values.put(HendrixContract.StaffColumn.COLUMN_DEPARTMENT, person.getDept());
                values.put(HendrixContract.StaffColumn.COLUMN_PHONE, person.getPhone());
                values.put(HendrixContract.StaffColumn.COLUMN_EMAIL, person.getEmail());
                values.put(HendrixContract.StaffColumn.COLUMN_LOCATION_LINE_1, person.getline1());
                values.put(HendrixContract.StaffColumn.COLUMN_LOCATION_LINE_2, person.getline2());
                mFragment.getActivity().getContentResolver().insert(HendrixContract.StaffColumn.CONTENT_URI, values);
            }

        }
    }
}
