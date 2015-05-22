package com.ryan.unofficialhendrixapp;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import com.ryan.unofficialhendrixapp.data.HendrixContract;
import com.ryan.unofficialhendrixapp.data.HendrixContract.NewsColumn;
import com.ryan.unofficialhendrixapp.data.HendrixProvider;
import com.ryan.unofficialhendrixapp.models.NewsEntry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowContentResolver;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(manifest="/app/src/main/AndroidManifest.xml")
public class StaffProviderTest {

    HendrixProvider mProvider;
    ContentResolver mContentResolver;

    String [] NEWS_COLUMNS = { NewsColumn.COLUMN_TITLE, NewsColumn.COLUMN_LINK, NewsColumn.COLUMN_DESCRIPTION, NewsColumn.COLUMN_DATE };

    final int COL_TITLE = 0;
    final int COL_LINK = 1;
    final int COL_DESCRIPTION = 2;
    final int COL_DATE = 3;

    @Before
    public void setup() {
        mProvider = new HendrixProvider();
        Activity activity = new Activity();
        mContentResolver = activity.getContentResolver();
        mProvider.onCreate();
        ShadowContentResolver.registerProvider(HendrixContract.CONTENT_AUTHORITY, mProvider);
    }

    @Test
    public void testProviderInsertion() {
        NewsEntry entry = new NewsEntry("title", "link", "description", "Thu, 21 May 2015 02:10:54 GMT");
        mContentResolver.insert(NewsColumn.CONTENT_URI, getContentValue(entry));
        NewsEntry transformedEntry = getNewsEntry(mContentResolver.query(NewsColumn.CONTENT_URI, NEWS_COLUMNS, null, null, null));
        assertEquals(entry, transformedEntry);
    }

    private NewsEntry getNewsEntry( Cursor cursor ) {
        cursor.moveToNext();
        return new NewsEntry( cursor.getString(COL_TITLE),
                cursor.getString(COL_LINK),
                cursor.getString(COL_DESCRIPTION),
                cursor.getLong(COL_DATE));
    }

    private ContentValues getContentValue( NewsEntry entry ) {
        ContentValues row = new ContentValues();
        row.put( NewsColumn.COLUMN_TITLE, entry.title);
        row.put( NewsColumn.COLUMN_DESCRIPTION, entry.description);
        row.put( NewsColumn.COLUMN_DATE, entry.date.getTime() );
        row.put( NewsColumn.COLUMN_LINK, entry.link);
        return row;
    }
}
