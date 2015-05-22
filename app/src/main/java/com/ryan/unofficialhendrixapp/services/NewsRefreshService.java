package com.ryan.unofficialhendrixapp.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.widget.Toast;

import com.ryan.unofficialhendrixapp.R;
import com.ryan.unofficialhendrixapp.data.HendrixContract.NewsColumn;
import com.ryan.unofficialhendrixapp.parse.NewsParser;
import com.ryan.unofficialhendrixapp.models.NewsEntry;
import com.ryan.unofficialhendrixapp.models.NewsEvent;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;

// TODO: Set up a periodic refreshing of news when application is not open. Look up BroadCastService.
// TODO: Set it so it only adds new articles based on if it is not in the database.
public class NewsRefreshService extends IntentService {
    public static final String INITIAL_REFRESH_KEY = "initialNewsPull";
    public static final String RECEIVER_KEY = "news_receiver_key";
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
        ContentValues row;
        ArrayList<NewsEntry> newsEntryList = new NewsParser(getApplicationContext()).getList();

        getContentResolver().delete(NewsColumn.CONTENT_URI, null, null);

        for ( NewsEntry entry : newsEntryList ) {
            row = new ContentValues();
            row.put( NewsColumn.COLUMN_TITLE, entry.title);
            row.put( NewsColumn.COLUMN_DESCRIPTION, entry.description);
            row.put( NewsColumn.COLUMN_DATE, entry.date.getTime() );
            row.put( NewsColumn.COLUMN_LINK, entry.link);
            getContentResolver().insert(NewsColumn.CONTENT_URI, row);
        }
    }
}
