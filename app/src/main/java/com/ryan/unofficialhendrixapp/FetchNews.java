package com.ryan.unofficialhendrixapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import com.ryan.unofficialhendrixapp.adapters.NewsAdapter;
import com.ryan.unofficialhendrixapp.helpers.Utility;
import com.ryan.unofficialhendrixapp.models.Entry;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class FetchNews extends AsyncTask< ArrayList<Entry>, Void, ArrayList<Entry> > {

    private Context mContext;
    private NewsAdapter mNewsAdapter;
    private String LOG_TAG = getClass().getSimpleName();

    public FetchNews( Context context, NewsAdapter newsAdapter ) {
        mContext = context;
        mNewsAdapter = newsAdapter;
    }

    /**
     * Parses xml at R.string.rss_url to return an ArrayList of each item
     * in the rss feed.
     */
    @Override
    protected ArrayList<Entry> doInBackground(ArrayList<Entry>... params) {
        XmlPullParser parser;
        ArrayList<Entry> entryList = new ArrayList<Entry>();

        if ( !Utility.isOnline(mContext) ) { // If an error arises, keep the list of entries same
            //entryList = params[0];
        } else if ( params.length != 0 && !params[0].isEmpty() ) { // If there are already entries, update it with new entries
            //entryList = appendNewValues(params[0]);
        } else { // If we are starting a clean slate, put all the entries in the rss_url feed into entryList
            entryList = getFilledEntryList(new ArrayList<Entry>());
        }

        return entryList;
    }

    @Override
    protected void onPostExecute( ArrayList<Entry> entryList) {
        super.onPostExecute(entryList);
        mNewsAdapter.addAll( entryList );
    }

    /**
     * TODO: Set up appending of new entries
     */
    private ArrayList<Entry> appendNewValues(ArrayList<Entry> list) {
        return list;
    }

    private ArrayList<Entry> getFilledEntryList(ArrayList<Entry> list) {
        InputStream inputStream = Utility.getRSSStream(mContext);
        XmlPullParser parser;
        ArrayList<Entry> entryList = new ArrayList<Entry>();

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
                    entryList.add(getEntry(parser));
                }
            }

        } catch ( IOException|XmlPullParserException e ) {
            Log.v(LOG_TAG, "RSS link stopped working");
            return new ArrayList<Entry>();
        } finally {
            Utility.closeInputStream( inputStream );
        }

        return entryList;
    }

    private Entry getEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "item");
        String title = null;
        String link = null;
        String description = null;
        String date = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
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
        return new Entry(title, link, description, date.substring(0, 16));
    }

    // Processes title tags in the feed.
    private String readCategory(XmlPullParser parser, String category) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, category);
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, category);
        return title;
    }

    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
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
