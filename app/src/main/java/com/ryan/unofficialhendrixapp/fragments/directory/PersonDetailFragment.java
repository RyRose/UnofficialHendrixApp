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
import android.widget.ImageView;
import android.widget.TextView;

import com.ryan.unofficialhendrixapp.R;
import com.ryan.unofficialhendrixapp.activities.DirectoryDetailActivity;
import com.ryan.unofficialhendrixapp.data.HendrixContract.StaffColumn;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;

public class PersonDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String[] PERSON_COLUMNS = {
            StaffColumn.COLUMN_NAME,
            StaffColumn.COLUMN_TITLE,
            StaffColumn.COLUMN_EMAIL,
            StaffColumn.COLUMN_PHONE,
            StaffColumn.COLUMN_LOCATION_LINE_1,
            StaffColumn.COLUMN_LOCATION_LINE_2,
            StaffColumn.COLUMN_PICTURE,
            StaffColumn._ID,
    };

    private static int COL_PERSON_NAME = 0;
    private static int COL_PERSON_TITLE = 1;
    private static int COL_PERSON_EMAIL = 2;
    private static int COL_PERSON_PHONE = 3;
    private static int COL_PERSON_LOCLINE1 = 4;
    private static int COL_PERSON_LOCLINE2 = 5;
    private static int COL_PERSON_PICTURE = 6;
    private static int COL_PERSON_ID = 7;

    @InjectView (R.id.person_ImageView) ImageView mImageView;
    @InjectViews({R.id.person_textView1, R.id.person_textView2,
                  R.id.person_textView4,  R.id.person_textView5,
                  R.id.person_textView6,  R.id.person_textView7})
    TextView[] mPersonViews;


    public static PersonDetailFragment newInstance (int id) {
        Bundle bundle = new Bundle();
        bundle.putInt(DirectoryDetailActivity.ID_KEY, id);
        PersonDetailFragment fragment = new PersonDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_directory_person, container, false);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                StaffColumn.CONTENT_URI,
                PERSON_COLUMNS,
                "_ID == ?",
                new String[] {String.valueOf(getArguments().getInt(DirectoryDetailActivity.ID_KEY))},
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        getActivity().setTitle( data.getString(COL_PERSON_NAME) );

        Picasso.with(getActivity())
                .load( data.getString(COL_PERSON_PICTURE))
                .fit()
                .centerCrop()
                .into(mImageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(getActivity())
                                .load( R.drawable.placeholder_person)
                                .fit()
                                .centerInside()
                                .into(mImageView);
                    }
                });

        for ( int i = 0; i < mPersonViews.length; i++ ) {
            mPersonViews[i].setText( data.getString(i) );
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
