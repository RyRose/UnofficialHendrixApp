package com.ryan.unofficialhendrixapp.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.ResultReceiver;

import com.ryan.unofficialhendrixapp.data.HendrixContract;
import com.ryan.unofficialhendrixapp.fragments.NewsFragment;
import com.ryan.unofficialhendrixapp.helpers.NewsParser;
import com.ryan.unofficialhendrixapp.helpers.Utility;
import com.ryan.unofficialhendrixapp.models.NewsEntry;

import java.util.ArrayList;

public class NewsRefreshService extends IntentService {
    public static final String FORCE_NEWS_REFRESH_KEY = "news_service_key";
    public static final String RECEIVER_KEY = "news_receiver_key";
    private static final String LOG_TAG = "NewsRefreshService";

    public NewsRefreshService() {
        super(LOG_TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if ( canFillNewsDb(intent.getBooleanExtra(FORCE_NEWS_REFRESH_KEY, false)) ) {
            fillNewsDb();
            ((ResultReceiver) intent.getParcelableExtra(RECEIVER_KEY)).send(0, null); // notifies NewsFragment done
        }
    }

    private boolean canFillNewsDb(boolean forceFill) {
        Cursor c = getContentResolver()
                .query(HendrixContract.NewsColumn.CONTENT_URI, NewsFragment.NEWS_COLUMNS, null, null, null);
        if (c == null) {
            return Utility.isOnline(getApplicationContext()) && forceFill;
        } else {
            boolean bool = !c.moveToFirst();
            c.close();
            return Utility.isOnline(getApplicationContext()) && (bool || forceFill);
        }
    }

    private void fillNewsDb() {
        ContentValues row;
        ArrayList<NewsEntry> newsEntryList = new NewsParser(getApplicationContext()).getList();

        getContentResolver().delete(HendrixContract.NewsColumn.CONTENT_URI, null, null);

        for ( NewsEntry entry : newsEntryList ) {
            row = new ContentValues();
            row.put( HendrixContract.NewsColumn.COLUMN_TITLE, entry.title);
            row.put( HendrixContract.NewsColumn.COLUMN_DESCRIPTION, entry.description);
            row.put( HendrixContract.NewsColumn.COLUMN_DATE, entry.date);
            row.put( HendrixContract.NewsColumn.COLUMN_LINK, entry.link);
            getContentResolver().insert(HendrixContract.NewsColumn.CONTENT_URI, row);
        }
    }
}
