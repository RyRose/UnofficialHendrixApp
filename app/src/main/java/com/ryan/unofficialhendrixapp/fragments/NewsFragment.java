package com.ryan.unofficialhendrixapp.fragments;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.ryan.unofficialhendrixapp.R;
import com.ryan.unofficialhendrixapp.adapters.NewsAdapter;
import com.ryan.unofficialhendrixapp.data.HendrixContract.NewsColumn;
import com.ryan.unofficialhendrixapp.data.HendrixDbHelper;
import com.ryan.unofficialhendrixapp.helpers.NewsParser;
import com.ryan.unofficialhendrixapp.helpers.Utility;
import com.ryan.unofficialhendrixapp.models.NewsEntry;

import java.util.ArrayList;

public class NewsFragment extends ListFragment implements LoaderManager.LoaderCallbacks{
    private final String LOG_TAG = getClass().getSimpleName();

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
        setRetainInstance(true);
        NewsAdapter adapter = new NewsAdapter(getActivity(), null, 0);
        new FetchNews(getActivity(), new HendrixDbHelper(getActivity()), adapter).execute();

        setListAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle args = getArguments();
        int name_pos = args.getInt(getResources().getString(R.string.fragment_pos_key));
        getActivity().setTitle(getResources().getStringArray(R.array.drawer_names)[ name_pos ]);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Cursor c = ( (CursorAdapter) getListAdapter()) .getCursor();
        c.moveToPosition(position);
        String link = c.getString(NewsFragment.COL_NEWS_LINK);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(intent);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    /**
     *
     * Fetches the xml document from the rss feed showing the current news on Hendrix College's Website
     * to be displayed in a list view on the main activity
     *
     */
    private class FetchNews extends AsyncTask< Void, Void, Cursor > {

        private Context mContext;
        private NewsAdapter mNewsAdapter;
        private HendrixDbHelper dbHelper;

        private String LOG_TAG = getClass().getSimpleName();

        public FetchNews(Context context, HendrixDbHelper dbHelper, NewsAdapter newsAdapter) {
            mContext = context;
            mNewsAdapter = newsAdapter;
            this.dbHelper = dbHelper;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mNewsAdapter.changeCursor(null);
        }

        @Override
        protected Cursor doInBackground(Void... params ) {
            ArrayList<NewsEntry> newsEntryList;
            SQLiteDatabase database;
            ContentValues row;

            if ( isDatabaseFilled() ) {
                database = dbHelper.getReadableDatabase();
                return database.query(NewsColumn.TABLE_NAME, NEWS_COLUMNS, null, null, null, null, null);
            } else if ( !Utility.isOnline(mContext) ) { // If an error arises, keep the list of entries same
                newsEntryList = new ArrayList<>();
            } else { // If we are starting a clean slate, put all the entries in the rss_url feed into entryList
                newsEntryList = getFilledEntryList();
            }

            database = dbHelper.getWritableDatabase();
            database.delete( NewsColumn.TABLE_NAME, null, null);
            for ( NewsEntry entry : newsEntryList ) {
                row = new ContentValues();
                row.put( NewsColumn.COLUMN_TITLE, entry.getTitle());
                row.put( NewsColumn.COLUMN_DESCRIPTION, entry.getDescription());
                row.put( NewsColumn.COLUMN_DATE, entry.getDate());
                row.put( NewsColumn.COLUMN_LINK, entry.getLink());
                database.insert( NewsColumn.TABLE_NAME, null, row);
            }
            database.close();
            database = dbHelper.getReadableDatabase();

            return database.query(NewsColumn.TABLE_NAME, NEWS_COLUMNS, null, null, null, null, null);
        }

        @Override
        protected void onPostExecute( Cursor c ) {
            super.onPostExecute(c);
            mNewsAdapter.changeCursor(c);
            mNewsAdapter.notifyDataSetChanged();
            mContext = null;
        }

        private boolean isDatabaseFilled() {
            SharedPreferences sharedPreferences = getActivity().getPreferences( Preference.DEFAULT_ORDER );
            String dbFilled = getResources().getString(R.string.db_filled_key);
            return sharedPreferences.getBoolean(dbFilled, false);
        }

        private ArrayList<NewsEntry> getFilledEntryList() {
            NewsParser newsParser = new NewsParser(mContext);
            return newsParser.parse();
        }

    }
}
