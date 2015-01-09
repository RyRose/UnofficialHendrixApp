package com.ryan.unofficialhendrixapp.fragments.directory;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.ryan.unofficialhendrixapp.R;
import com.ryan.unofficialhendrixapp.activities.DirectoryDetailActivity;
import com.ryan.unofficialhendrixapp.adapters.directory.DirectoryGridAdapter;
import com.ryan.unofficialhendrixapp.data.HendrixContract;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;

public class PersonGridFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public final static String[] GRID_COLUMNS = {
            HendrixContract.StaffColumn._ID,
            HendrixContract.StaffColumn.COLUMN_NAME,
            HendrixContract.StaffColumn.COLUMN_PICTURE
    };

    public final static int COL_GRID_ID = 0;
    public final static int COL_GRID_NAME = 1;
    public final static int COL_GRID_PIC = 2;

    private final static int DEPT_LOADER = 0;
    private final static int LETTER_LOADER = 1;

    private String mDept;
    private String mLetter;


    @InjectView(R.id.directory_grid) GridView mGridView;
    private DirectoryGridAdapter mAdapter;
    private OnPersonSelectedListener mCallback;

    public interface OnPersonSelectedListener{
        public void onPersonSelected(int id);
    }

    public static PersonGridFragment newInstance( String dept, String letter) {
        Bundle bundle = new Bundle();
        if (letter != null) {
            bundle.putString(DirectoryDetailActivity.LETTER_KEY, letter);
        } else {
            bundle.putString(DirectoryDetailActivity.DEPT_KEY, dept);
        }
        PersonGridFragment fragment = new PersonGridFragment();
        fragment.setArguments(bundle);
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallback = (OnPersonSelectedListener) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new DirectoryGridAdapter(getActivity(), null, DirectoryGridAdapter.NO_SELECTION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_directory_grid, container, false);
        ButterKnife.inject(this, rootView);
        mGridView.setAdapter(mAdapter);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDept = getArguments().getString(DirectoryDetailActivity.DEPT_KEY, null);
        mLetter = getArguments().getString(DirectoryDetailActivity.LETTER_KEY, null);
        getActivity().setTitle( (mDept == null) ? mLetter : mDept );
        if (mDept != null) {
            getLoaderManager().initLoader(DEPT_LOADER, null, this);
        } else {
            getLoaderManager().initLoader(LETTER_LOADER, null, this);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @OnItemClick(R.id.directory_grid)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mCallback.onPersonSelected( (int) id);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection;
        String[] selectionArgs;
        if (id == DEPT_LOADER) {
            selection = HendrixContract.StaffColumn.COLUMN_DEPARTMENT
                    + " == ?";
            selectionArgs = new String[]{mDept};
        } else if (id == LETTER_LOADER) {
            selection = "substr(" + HendrixContract.StaffColumn.COLUMN_NAME +
                    ", 1, 1) == ?";
            selectionArgs = new String[]{mLetter};
        } else {
            return null;
        }

        return new CursorLoader(
                getActivity(),
                HendrixContract.StaffColumn.CONTENT_URI,
                GRID_COLUMNS,
                selection,
                selectionArgs,
                HendrixContract.StaffColumn.COLUMN_NAME + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}