package com.ryan.unofficialhendrixapp.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ryan.unofficialhendrixapp.R;
import com.ryan.unofficialhendrixapp.adapters.NewsAdapter;
import com.ryan.unofficialhendrixapp.data.HendrixContract.NewsColumn;
import com.ryan.unofficialhendrixapp.data.HendrixDbHelper;
import com.ryan.unofficialhendrixapp.helpers.NewsParser;
import com.ryan.unofficialhendrixapp.helpers.Utility;
import com.ryan.unofficialhendrixapp.models.NewsEntry;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by ryan on 12/28/14.
 */

public class NewsFragment extends Fragment {

    @InjectView(R.id.listViewNews) ListView mListView;
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HendrixDbHelper hendrixDbHelper = new HendrixDbHelper(getActivity());
        mNewsAdapter = new NewsAdapter(getActivity(), null, 0);
        new FetchNews(getActivity(), hendrixDbHelper, mNewsAdapter).execute();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news, null);
        ButterKnife.inject(this, rootView);

        mListView.setAdapter(mNewsAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = ( (NewsAdapter) parent.getAdapter() ).getCursor();
                c.moveToPosition( position );
                String link = c.getString( NewsFragment.COL_NEWS_LINK );
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
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
            database = dbHelper.getReadableDatabase();

            return database.query(NewsColumn.TABLE_NAME, NEWS_COLUMNS, null, null, null, null, null);
        }

        @Override
        protected void onPostExecute( Cursor c ) {
            super.onPostExecute(c);
            mNewsAdapter.changeCursor(c);
            mNewsAdapter.notifyDataSetChanged();
        }

        private boolean isDatabaseFilled() {
            SharedPreferences sharedPreferences = getActivity().getPreferences( Preference.DEFAULT_ORDER );
            String dbFilled = getResources().getString(R.string.db_filled);
            return sharedPreferences.getBoolean(dbFilled, false);
        }

        private ArrayList<NewsEntry> getFilledEntryList() {
            NewsParser newsParser = new NewsParser(mContext);
            return newsParser.parse();
        }

    }
}
