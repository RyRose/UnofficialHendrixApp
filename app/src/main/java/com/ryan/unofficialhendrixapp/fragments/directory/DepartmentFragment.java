package com.ryan.unofficialhendrixapp.fragments.directory;

import android.app.Fragment;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.ryan.unofficialhendrixapp.R;
import com.ryan.unofficialhendrixapp.activities.DirectoryDetailActivity;
import com.ryan.unofficialhendrixapp.adapters.directory.DepartmentAdapter;
import com.ryan.unofficialhendrixapp.data.HendrixContract;
import com.ryan.unofficialhendrixapp.helpers.DirectoryParser;
import com.ryan.unofficialhendrixapp.models.Person;

import java.util.ArrayList;

import icepick.Icicle;

public class DepartmentFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private final String LOG_TAG = getClass().getSimpleName();

    @Icicle int mPosition;

    private static final String[] DEPARTMENT_COLUMNS = {
            HendrixContract.StaffColumn._ID,
            HendrixContract.StaffColumn.COLUMN_DEPARTMENT,
    };

    private final String POS_KEY = "dep_key";
    public static final int COL_DEPARTMENT_ID = 0;
    public static final int COL_DEPARTMENT_DEPT = 1;

    public static DepartmentFragment newInstance(int pos, Context context) {
        Bundle bundle = new Bundle();
        bundle.putInt( context.getResources().getString(R.string.fragment_pos_key), pos);
        DepartmentFragment fragment = new DepartmentFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.getInt(POS_KEY, 0) != 0) {
            mPosition = savedInstanceState.getInt(POS_KEY, 0);
        } else {
            mPosition = getActivity().getPreferences(Context.MODE_PRIVATE).getInt(POS_KEY, 0);
        }
        DepartmentAdapter adapter = new DepartmentAdapter(getActivity(), null, DepartmentAdapter.NO_SELECTION);
        new FillStaffTable(this).execute(DEPARTMENT_COLUMNS);

        setListAdapter(adapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
        int name_pos = getArguments().getInt( getResources().getString(R.string.fragment_pos_key) );
        getActivity().setTitle(getResources().getStringArray(R.array.drawer_names)[ name_pos ]);
    }

    @Override
    public void onResume() {
        super.onResume();
        getListView().smoothScrollToPosition(mPosition);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().getPreferences(Context.MODE_PRIVATE).edit().putInt(POS_KEY, mPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POS_KEY, mPosition);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Cursor c = ( (DepartmentAdapter) getListAdapter()).getCursor();
        c.moveToPosition(position);
        String dept = c.getString(COL_DEPARTMENT_DEPT);
        mPosition = getListView().getFirstVisiblePosition();
        Intent intent = new Intent(getActivity(), DirectoryDetailActivity.class);
        intent.putExtra(DirectoryDetailActivity.DEPT_KEY, dept);
        getActivity().startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                HendrixContract.StaffColumn.CONTENT_URI_WITH_DISTINCT,
                DEPARTMENT_COLUMNS,
                null,
                null,
                HendrixContract.StaffColumn.COLUMN_DEPARTMENT + " ASC"
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ( (CursorAdapter) getListAdapter() ).swapCursor(data);
        //getListView().smoothScrollToPosition(mPosition);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ( (CursorAdapter) getListAdapter() ).swapCursor(null);
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
