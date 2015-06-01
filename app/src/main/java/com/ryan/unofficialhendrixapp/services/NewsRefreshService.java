package com.ryan.unofficialhendrixapp.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.ryan.unofficialhendrixapp.R;
import com.ryan.unofficialhendrixapp.activities.MainActivity;
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

public class NewsRefreshService extends IntentService {
    private static final String LOG_TAG = "NewsRefreshService";

    public static final String INITIAL_REFRESH_KEY = "initialNewsPull";
    public static final String DISPLAY_NOTIFICATION_KEY = "displayNotificationKey";

    private Handler toastHandler;
    private ArrayList<NewsEntry> notificationList;

    public NewsRefreshService() {
        super(LOG_TAG);
        toastHandler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        notificationList = new ArrayList<>();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            fillNewsDb();
            prefs.edit().putBoolean(INITIAL_REFRESH_KEY, false).apply();
        } catch (IOException | XmlPullParserException | ParseException e) {
            if ( !intent.getBooleanExtra(DISPLAY_NOTIFICATION_KEY, false) )
                toastHandler.post( () -> Toast.makeText(getApplicationContext(), getString(R.string.news_refresh_error), Toast.LENGTH_LONG).show() );
        } finally {
            EventBus.getDefault().post(new NewsEvent());
        }

        if ( prefs.getBoolean( getString(R.string.pref_notifications), true) && intent.getBooleanExtra(DISPLAY_NOTIFICATION_KEY, false) )
            showNotifications();
    }

    private void fillNewsDb() throws IOException, XmlPullParserException, ParseException {
        ArrayList<NewsEntry> newsEntryList = new NewsParser(getApplicationContext()).getList();

        Cursor c = getContentResolver().query(NewsColumn.CONTENT_URI, NewsFragment.NEWS_COLUMNS, null, null, null);
        ArrayList<NewsEntry> databaseList = makeEntryList(c);
        c.close();

        for ( NewsEntry entry : newsEntryList )
            if ( !databaseList.contains(entry) )
                addToDatabase(entry);
    }

    private ArrayList<NewsEntry> makeEntryList( Cursor c ) {
        ArrayList<NewsEntry> entryList = new ArrayList<>();
        while (c.moveToNext())
            entryList.add( new NewsEntry( c.getString(NewsFragment.COL_NEWS_TITLE),
                                          c.getString(NewsFragment.COL_NEWS_LINK),
                                          c.getString(NewsFragment.COL_NEWS_DESCRIPTION),
                                          c.getLong(NewsFragment.COL_NEWS_DATE) ) );
        c.moveToFirst();
        return entryList;
    }

    private void addToDatabase( NewsEntry entry ) {
        ContentValues row = new ContentValues();
        row.put(NewsColumn.COLUMN_TITLE, entry.title);
        row.put(NewsColumn.COLUMN_DESCRIPTION, entry.description);
        row.put(NewsColumn.COLUMN_DATE, entry.date.getTime());
        row.put(NewsColumn.COLUMN_LINK, entry.link);
        getContentResolver().insert(NewsColumn.CONTENT_URI, row);

        notificationList.add(entry);
    }

    private void showNotifications() {
        String contentTitle = getString(R.string.notification_title);
        String contentText;
        Intent resultIntent;
        NotificationCompat.Builder builder =  new NotificationCompat.Builder(this);

        if (notificationList.size() == 0 ) {
            return;
        } else if ( notificationList.size() == 1 ) {
            contentText =  notificationList.get(0).title;
            resultIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(notificationList.get(0).link));
        } else {
            contentText = notificationList.size() + " " + getString(R.string.notification_content_plural);
            resultIntent = new Intent(this, MainActivity.class);

            NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
            style.setBigContentTitle(getString(R.string.notification_big_content_plural));

            String summaryText;

            if (notificationList.size() > 5)
                summaryText = "+" + (notificationList.size() - 5) + " " + getString(R.string.notification_summary_text_more);
            else
                summaryText = "";

            style.setSummaryText(summaryText);

            for (int i = 0; i < 5 && i < notificationList.size(); i++)
                style.addLine(notificationList.get(i).title);
            builder.setStyle(style);
        }

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,
                builder.setContentTitle(contentTitle)
                        .setContentText(contentText)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setCategory(NotificationCompat.CATEGORY_RECOMMENDATION)
                        .setPriority(NotificationCompat.PRIORITY_LOW)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setContentIntent(resultPendingIntent)
                        .setAutoCancel(true)
                        .build());

        Log.d(LOG_TAG, "Displaying notifications");
    }
}
