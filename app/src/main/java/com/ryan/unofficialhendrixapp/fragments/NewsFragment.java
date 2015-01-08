package com.ryan.unofficialhendrixapp.fragments;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ryan.unofficialhendrixapp.R;
import com.ryan.unofficialhendrixapp.adapters.NewsAdapter;
import com.ryan.unofficialhendrixapp.data.HendrixContract.NewsColumn;
import com.ryan.unofficialhendrixapp.helpers.NewsParser;
import com.ryan.unofficialhendrixapp.helpers.Utility;
import com.ryan.unofficialhendrixapp.models.NewsEntry;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;

public class NewsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private final String LOG_TAG = getClass().getSimpleName();

    @InjectView(R.id.fragment_news_listView) ListView mListView;
    @InjectView(R.id.fragment_news_swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;
    NewsAdapter mNewsAdapter;
    private static final String[] NEWS_COLUMNS = {
        NewsColumn._ID,
        NewsColumn.COLUMN_TITLE,
        NewsColumn.COLUMN_DESCRIPTION,
        NewsColumn.COLUMN_DATE,
        NewsColumn.COLUMN_LINK
    };

    public static final int COL_NEWS_ID = 0;
    public static final int COL_NEWS_TITLE = 1;
    public static final int COL_NEWS_DESCRIPTION = 2;
    public static final int COL_NEWS_DATE = 3;
    public static final int COL_NEWS_LINK = 4;

    public static NewsFragment newInstance(int pos, Context context) {
        Bundle bundle = new Bundle();
        bundle.putInt( context.getResources().getString(R.string.fragment_pos_key), pos);
        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new FillNews(this).execute(false);
        mNewsAdapter = new NewsAdapter(getActivity(), null, NewsAdapter.NO_SELECTION);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.inject(this, rootView);

        mListView.setAdapter(mNewsAdapter);
        mSwipeRefreshLayout.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
        Bundle args = getArguments();
        int name_pos = args.getInt(getResources().getString(R.string.fragment_pos_key));
        getActivity().setTitle(getResources().getStringArray(R.array.drawer_names)[ name_pos ]);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_news, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.action_refresh:
                refresh();
                break;
            default:
                return false;
        }
        return true;
    }

    public void refresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        new FillNews(this).execute(true);
        getLoaderManager().getLoader(0).reset();
    }

    @OnItemClick(R.id.fragment_news_listView)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor c = mNewsAdapter.getCursor();
        c.moveToPosition(position);
        String link = c.getString(NewsFragment.COL_NEWS_LINK);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                NewsColumn.CONTENT_URI,
                NEWS_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mNewsAdapter.changeCursor(data);

    }

    @Override
    public void onLoaderReset(Loader loader) {
        mNewsAdapter.changeCursor(null);
    }

    /**
     *
     * Fetches the xml document from the rss feed showing the current news on Hendrix College's Website
     * to be displayed in a list view on the main activity
     *
     */
    private class FillNews extends AsyncTask< Boolean, Void, Boolean > {

        private Fragment mFragment;
        private String LOG_TAG = getClass().getSimpleName();

        public FillNews(Fragment fragment) {
            mFragment = fragment;
        }

        @Override
        protected Boolean doInBackground(Boolean... params ) {

            if ( canFillNewsDb(params[0]) ) {
                fillNewsDb();
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean bool) {
            super.onPostExecute(bool);
            if (bool) {
                getLoaderManager().restartLoader(0, null, (LoaderManager.LoaderCallbacks) mFragment);
            }
            mSwipeRefreshLayout.setRefreshing(false);
            mFragment = null;
        }

        private boolean canFillNewsDb(boolean forceFill) {
            Cursor c = getActivity().getContentResolver()
                    .query(NewsColumn.CONTENT_URI, NEWS_COLUMNS, null, null, null, null);
            if (c == null) {
                return Utility.isOnline(getActivity()) && forceFill;
            } else {
                boolean bool = !c.moveToFirst();
                c.close();
                return Utility.isOnline(getActivity()) && (bool || forceFill);
            }
        }

        private void fillNewsDb() {
            ContentValues row;
            ArrayList<NewsEntry> newsEntryList = new NewsParser(getActivity()).parse();

            getActivity().getContentResolver().delete(NewsColumn.CONTENT_URI, null, null);
            for ( NewsEntry entry : newsEntryList ) {
                row = new ContentValues();
                row.put( NewsColumn.COLUMN_TITLE, entry.getTitle());
                row.put( NewsColumn.COLUMN_DESCRIPTION, entry.getDescription());
                row.put( NewsColumn.COLUMN_DATE, entry.getDate());
                row.put( NewsColumn.COLUMN_LINK, entry.getLink());
                getActivity().getContentResolver().insert(NewsColumn.CONTENT_URI, row);
            }
        }
    }
}
