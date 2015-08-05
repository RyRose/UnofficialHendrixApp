package com.ryan.unofficialhendrixapp.fragments.staff_categories;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import com.ryan.unofficialhendrixapp.activities.StaffDetailActivity;
import com.ryan.unofficialhendrixapp.adapters.staff.StaffCategoryAdapter;

public abstract class BaseByCategoryFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private String mPosition_key = "position";
    private int mPosition = 0;

    abstract int getColumnNumber();
    abstract String getStaffDetailKey();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new StaffCategoryAdapter(getActivity(), getColumnNumber()));


        if (savedInstanceState != null) {
            mPosition = savedInstanceState.getInt(mPosition_key);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onResume() {
        super.onResume();
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
        outState.putInt(mPosition_key, mPosition);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Cursor c = ( (StaffCategoryAdapter) getListAdapter()).getCursor();
        String text = c.getString(getColumnNumber());
        Intent intent = new Intent(getActivity(), StaffDetailActivity.class);
        intent.putExtra(getStaffDetailKey(), text);
        getActivity().startActivity(intent);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ( (StaffCategoryAdapter) getListAdapter() ).swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ((StaffCategoryAdapter) getListAdapter()).swapCursor(null);
    }
}
