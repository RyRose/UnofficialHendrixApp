package com.ryan.unofficialhendrixapp.fragments.directory;

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
import android.widget.GridView;

import com.ryan.unofficialhendrixapp.R;
import com.ryan.unofficialhendrixapp.activities.DirectoryDetailActivity;
import com.ryan.unofficialhendrixapp.data.HendrixContract;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PersonGridFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    @InjectView(R.id.directory_grid) GridView mGridView;

    String mDept;
    String mLetter;

    public final static String[] GRID_COLUMNS = {
            HendrixContract.StaffColumn._ID,
            HendrixContract.StaffColumn.COLUMN_NAME,
            HendrixContract.StaffColumn.COLUMN_PICTURE
    };

    private final int DEPARTMENT_LOADER = 0;
    private final int LETTER_LOADER = 1;

    public static PersonGridFragment newInstance( String dept, String letter) {
        Bundle bundle = new Bundle();
        if (dept != null) {
            bundle.putString(DirectoryDetailActivity.LETTER_KEY, dept);
        } else {
            bundle.putString(DirectoryDetailActivity.DEPT_KEY, letter);
        }
        PersonGridFragment fragment = new PersonGridFragment();
        fragment.setArguments(bundle);
        fragment.setRetainInstance(true);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDept = (getArguments().getString(DirectoryDetailActivity.DEPT_KEY, null));
        mLetter = (getArguments().getString(DirectoryDetailActivity.LETTER_KEY, null));
        getActivity().setTitle( (mDept == null ? mLetter : mDept) );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_directory_grid, container);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments().getString(DirectoryDetailActivity.DEPT_KEY, null) != null) {
            getLoaderManager().initLoader(DEPARTMENT_LOADER, null, this);
        } else {
            getLoaderManager().initLoader(LETTER_LOADER, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection;
        String[] selectionArgs;
        if (id == DEPARTMENT_LOADER) {
            selection = HendrixContract.StaffColumn.COLUMN_DEPARTMENT
                    + " == ?";
            selectionArgs = new String[]{mDept};
        } else if (id == LETTER_LOADER) {
            selection = "substr(" + HendrixContract.StaffColumn.COLUMN_NAME +
                    ", 1, 1) = ?";
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

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
