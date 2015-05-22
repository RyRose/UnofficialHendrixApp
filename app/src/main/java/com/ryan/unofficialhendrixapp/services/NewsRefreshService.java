package com.ryan.unofficialhendrixapp.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.ryan.unofficialhendrixapp.R;
import com.ryan.unofficialhendrixapp.data.HendrixContract.NewsColumn;
import com.ryan.unofficialhendrixapp.fragments.nav_drawer.NewsFragment;
import com.ryan.unofficialhendrixapp.models.NewsEntry;
import com.ryan.unofficialhendrixapp.models.NewsEvent;
import com.ryan.unofficialhendrixapp.parse.NewsParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;

// TODO: Set up a periodic refreshing of news when application is not open. Look up BroadCastService.
public class NewsRefreshService extends IntentService {
    public static final String INITIAL_REFRESH_KEY = "initialNewsPull";
    private static final String LOG_TAG = "NewsRefreshService";

    public NewsRefreshService() {
        super(LOG_TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            fillNewsDb();
            getSharedPreferences( getString(R.string.prefs), MODE_PRIVATE).edit().putBoolean(INITIAL_REFRESH_KEY, false).apply();
        } catch (IOException | XmlPullParserException | ParseException e) {
            Toast.makeText(getApplicationContext(), getString(R.string.news_refresh_error), Toast.LENGTH_SHORT).show();
        } finally {
            EventBus.getDefault().post(new NewsEvent());
        }
    }

    private void fillNewsDb() throws IOException, XmlPullParserException, ParseException {
        ArrayList<NewsEntry> newsEntryList = new NewsParser(getApplicationContext()).getList();

        Cursor c = getContentResolver().query(NewsColumn.CONTENT_URI, NewsFragment.NEWS_COLUMNS, null, null, null);
        ArrayList<NewsEntry> databaseList = makeEntryList(c);
        c.close();

        for ( NewsEntry entry : newsEntryList ) {
            if ( !databaseList.contains(entry) )
                addToDatabase(entry);
        }
    }

    private ArrayList<NewsEntry> makeEntryList( Cursor c ) {
        ArrayList<NewsEntry> entryList = new ArrayList<>();
        while (c.moveToNext())
            entryList.add( new NewsEntry( c.getString(NewsFragment.COL_NEWS_TITLE),
                                          c.getString(NewsFragment.COL_NEWS_LINK),
                                          c.getString(NewsFragment.COL_NEWS_DESCRIPTION),
                                          c.getLong(NewsFragment.COL_NEWS_DATE) ) );
        return entryList;
    }

    private void addToDatabase( NewsEntry entry ) {
        Log.d(LOG_TAG, entry.toString() );
        ContentValues row = new ContentValues();
        row.put(NewsColumn.COLUMN_TITLE, entry.title);
        row.put(NewsColumn.COLUMN_DESCRIPTION, entry.description);
        row.put(NewsColumn.COLUMN_DATE, entry.date.getTime());
        row.put(NewsColumn.COLUMN_LINK, entry.link);
        getContentResolver().insert(NewsColumn.CONTENT_URI, row);
    }
}
