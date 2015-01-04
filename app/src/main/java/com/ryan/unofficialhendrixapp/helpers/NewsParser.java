package com.ryan.unofficialhendrixapp.helpers;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.ryan.unofficialhendrixapp.R;
import com.ryan.unofficialhendrixapp.models.NewsEntry;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class NewsParser {

    private final String LOG_TAG = getClass().getSimpleName();
    private Context mContext;

    public NewsParser( Context context) {
        mContext = context;
    }

    public ArrayList<NewsEntry> parse() {
        InputStream inputStream = getRSSStream();
        XmlPullParser parser;
        ArrayList<NewsEntry> newsEntryList = new ArrayList<NewsEntry>();

        try {
            parser = Xml.newPullParser();
            parser.setInput(inputStream, null);
            parser.nextTag();
            parser.require( XmlPullParser.START_TAG, null, mContext.getResources().getStringArray(R.array.rss_keys)[0]);

            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                if (name.equals( mContext.getResources().getStringArray(R.array.rss_keys)[2] )) {
                    newsEntryList.add(getEntry(parser));
                }
            }

            ( (Activity) mContext ).getPreferences(0)
                    .edit().putBoolean( mContext.getResources().getString(R.string.db_filled_key), true)
                    .commit();

        } catch ( IOException | XmlPullParserException e ) {
            Log.v(LOG_TAG, "RSS link stopped working: " + e.getMessage());
            return new ArrayList<NewsEntry>();
        } finally {
            Utility.closeInputStream( inputStream );
        }

        return newsEntryList;
    }

    private NewsEntry getEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        String item_key = mContext.getResources().getStringArray(R.array.rss_keys)[2];
        String title_key = mContext.getResources().getStringArray(R.array.rss_keys)[3];
        String link_key = mContext.getResources().getStringArray(R.array.rss_keys)[4];
        String description_key = mContext.getResources().getStringArray(R.array.rss_keys)[5];
        String pubDate_key = mContext.getResources().getStringArray(R.array.rss_keys)[6];
        String name;
        String title = "";
        String link = "";
        String description = "";
        String date = "";

        parser.require(XmlPullParser.START_TAG, null, item_key);

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            name = parser.getName();
            if      ( name.equals(title_key))       title = readCategory(parser, title_key);
            else if ( name.equals(link_key))        link = readCategory(parser, link_key);
            else if ( name.equals(description_key)) description = readCategory(parser, description_key);
            else if ( name.equals(pubDate_key))     date = readCategory(parser, pubDate_key);
            else skip(parser);

        }
        return new NewsEntry(title, link, description, date.substring(0, 16));
    }

    // Processes title tags in the feed.
    private String readCategory(XmlPullParser parser, String category) throws IOException, XmlPullParserException {
        String data = "";
        parser.require(XmlPullParser.START_TAG, null, category);

        if (parser.next() == XmlPullParser.TEXT) {
            data = parser.getText();
            parser.nextTag();
        }

        parser.require(XmlPullParser.END_TAG, null, category);
        return data;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    private InputStream getRSSStream() {
        URL url;
        InputStream inputStream;
        try {
            url = new URL(mContext.getResources().getString(R.string.rss_url));
            inputStream = url.openStream();
        } catch (IOException e) {
            inputStream = null;
            Log.v(LOG_TAG, "Site not available or RSS link down: " + e.getMessage());
        }
        return inputStream;
    }
}
