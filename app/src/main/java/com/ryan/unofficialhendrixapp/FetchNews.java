package com.ryan.unofficialhendrixapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Xml;

import com.ryan.unofficialhendrixapp.helpers.Utility;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class FetchNews extends AsyncTask< ArrayList<Entry>, Void, ArrayList<Entry> > {

    private Context context;

    public FetchNews( Context context ) {
        this.context = context;
    }

    /**
     * Parses xml at R.string.rss_url to return an ArrayList of each item
     * in the rss feed.
     */
    @Override
    protected ArrayList<Entry> doInBackground(ArrayList<Entry>... params) {
        XmlPullParser parser;
        ArrayList<Entry> entryList;

        if ( !Utility.isOnline( context ) ) { // If an error arises, keep the list of entries same
            entryList = params[0];
        } else if ( !params[0].isEmpty() ) { // If there are already entries, update it with new entries
            entryList = appendNewValues(params[0]);
        } else { // If we are starting a clean slate, put all the entries in the rss_url feed into entryList
            entryList = getFilledEntryList(params[0]);
        }

        return entryList;
    }

    /**
     * TODO: Set up appending of new entries
     */
    private ArrayList<Entry> appendNewValues(ArrayList<Entry> list) {
        return list;
    }

    private ArrayList<Entry> getFilledEntryList(ArrayList<Entry> list) {

    }

    private ArrayList<Entry> parse(ArrayList<Entry> list) {
        InputStream inputStream = getInputStream();
        XmlPullParser parser;
        ArrayList<Entry> entryList = new ArrayList<Entry>();

        try {
            parser = Xml.newPullParser();
            parser.setInput(inputStream, null);
            parser.nextTag();


            for( int i = 0; )
        } catch ( IOException|XmlPullParserException e ) {
            e.printStackTrace();
            return list;
        } finally {
            Utility.closeInputStream( inputStream );
        }

        return parser;
    }

    private InputStream getInputStream() {
        URL url;
        InputStream inputStream;
        try {
            url = new URL(context.getResources().getString(R.string.rss_url));
            inputStream = url.openStream();
        } catch (IOException e) {
            inputStream = null;
            e.printStackTrace();
        }
        return inputStream;
    }
}
