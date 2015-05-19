package com.ryan.unofficialhendrixapp.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.os.ResultReceiver;
import android.widget.Toast;

import com.ryan.unofficialhendrixapp.R;
import com.ryan.unofficialhendrixapp.data.HendrixContract.NewsColumn;
import com.ryan.unofficialhendrixapp.helpers.NewsParser;
import com.ryan.unofficialhendrixapp.models.NewsEntry;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

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
        } catch (IOException | XmlPullParserException e) {
            Toast.makeText(getApplicationContext(), getString(R.string.news_refresh_error), Toast.LENGTH_SHORT).show();
        } finally {
            ((ResultReceiver) intent.getParcelableExtra(RECEIVER_KEY)).send(0, null);
        }
    }

    private void fillNewsDb() throws IOException, XmlPullParserException{
        ContentValues row;
        ArrayList<NewsEntry> newsEntryList = new NewsParser(getApplicationContext()).getList();

        getContentResolver().delete(NewsColumn.CONTENT_URI, null, null);

        for ( NewsEntry entry : newsEntryList ) {
            row = new ContentValues();
            row.put( NewsColumn.COLUMN_TITLE, entry.title);
            row.put( NewsColumn.COLUMN_DESCRIPTION, entry.description);
            row.put( NewsColumn.COLUMN_DATE, entry.date);
            row.put( NewsColumn.COLUMN_LINK, entry.link);
            getContentResolver().insert(NewsColumn.CONTENT_URI, row);
        }
    }
}
