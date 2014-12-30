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
import java.util.ArrayList;

/**
 * Created by ryan on 12/29/14.
 */
public class NewsParser {

    private final String LOG_TAG = getClass().getSimpleName();
    Context mContext;

    public NewsParser( Context context) {
        mContext = context;
    }

    public ArrayList<NewsEntry> parse() {
        InputStream inputStream = Utility.getRSSStream(mContext);
        XmlPullParser parser;
        ArrayList<NewsEntry> newsEntryList = new ArrayList<NewsEntry>();

        try {
            parser = Xml.newPullParser();
            parser.setInput(inputStream, null);
            parser.nextTag();
            parser.require( XmlPullParser.START_TAG, null, "rss");

            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                if (name.equals("item")) {
                    newsEntryList.add(getEntry(parser));
                }
            }

            ( (Activity) mContext ).getPreferences(0)
                    .edit().putBoolean( mContext.getResources().getString(R.string.db_filled), true)
                    .commit();

        } catch ( IOException | XmlPullParserException e ) {
            Log.v(LOG_TAG, "RSS link stopped working");
            return new ArrayList<NewsEntry>();
        } finally {
            Utility.closeInputStream( inputStream );
        }

        return newsEntryList;
    }

    private NewsEntry getEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "item");
        String title = null;
        String link = null;
        String description = null;
        String date = null;
        String name;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            name = parser.getName();
            if (name.equals("title")) {
                title = readCategory(parser, "title");
            } else if (name.equals("link")) {
                link = readCategory(parser, "link");
            } else if (name.equals("description")) {
                description = readCategory(parser, "description");
            } else if (name.equals("pubDate")) {
                date = readCategory(parser, "pubDate");
            } else {
                skip(parser);
            }
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
}
