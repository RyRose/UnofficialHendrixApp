package com.ryan.unofficialhendrixapp.fragments.staff_categories;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import com.ryan.unofficialhendrixapp.activities.StaffDetailActivity;
import com.ryan.unofficialhendrixapp.adapters.staff.ByDeptAdapter;
import com.ryan.unofficialhendrixapp.data.HendrixContract.StaffColumn;

public class ByDeptFragment extends BaseByCategoryFragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private final String LOG_TAG = getClass().getSimpleName();


    public static final String[] DEPARTMENT_COLUMNS = {
            StaffColumn._ID,
            StaffColumn.COLUMN_DEPARTMENT,
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
        setListAdapter(new ByDeptAdapter(getActivity(), null));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Cursor c = ( (ByDeptAdapter) getListAdapter()).getCursor();
        String dept = c.getString(COL_DEPARTMENT_NAME);
        Intent intent = new Intent(getActivity(), StaffDetailActivity.class);
        intent.putExtra(StaffDetailActivity.DEPT_KEY, dept);
        getActivity().startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                StaffColumn.CONTENT_URI_WITH_DISTINCT,
                DEPARTMENT_COLUMNS,
                null,
                null,
                StaffColumn.COLUMN_DEPARTMENT + " ASC");

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ( (ByDeptAdapter) getListAdapter() ).swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ((ByDeptAdapter) getListAdapter()).swapCursor(null);
    }
}
