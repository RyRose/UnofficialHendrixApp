package com.ryan.unofficialhendrixapp.fragments.directory;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;

import com.ryan.unofficialhendrixapp.R;
import com.ryan.unofficialhendrixapp.adapters.directory.DepartmentAdapter;
import com.ryan.unofficialhendrixapp.asynctasks.FetchStaff;
import com.ryan.unofficialhendrixapp.data.HendrixContract;
import com.ryan.unofficialhendrixapp.data.HendrixDbHelper;

public class DepartmentFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private final String LOG_TAG = getClass().getSimpleName();

    private static final String[] DEPARTMENT_COLUMNS = {
            HendrixContract.StaffColumn.COLUMN_DEPARTMENT,
    };

    public static final int COL_DEPARTMENT_DEPT = 0;

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
        setRetainInstance(true);
        DepartmentAdapter adapter = new DepartmentAdapter(getActivity(), null, DepartmentAdapter.NO_SELECTION);
        new FetchStaff(getActivity(), new HendrixDbHelper(getActivity()), adapter).execute(DEPARTMENT_COLUMNS);
        setListAdapter(adapter);
        getLoaderManager().initLoader(0, null, this);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        int name_pos = getArguments().getInt( getResources().getString(R.string.fragment_pos_key) );
        getActivity().setTitle(getResources().getStringArray(R.array.drawer_names)[ name_pos ]);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
