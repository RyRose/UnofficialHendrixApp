package com.ryan.unofficialhendrixapp.helpers;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.ryan.unofficialhendrixapp.R;
import com.ryan.unofficialhendrixapp.models.NewsEntry;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class NewsParser extends BaseParser {

    private final String LOG_TAG = getClass().getSimpleName();
    private XmlPullParser mParser;
    private Context mContext;

    public NewsParser( Context context) {
        mContext = context;
    }

    @Override
    public XmlPullParser getParser() {
        return  mParser;
    }

    public ArrayList<NewsEntry> getList() {
        String [] keys = mContext.getResources().getStringArray(R.array.rss_keys);
        ArrayList<NewsEntry> newsEntryList = new ArrayList<>();

        try {
            setUpParser();
            while (mParser.next() != XmlPullParser.END_DOCUMENT) {
                if (mParser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = mParser.getName();
                if (name.equals( keys[2] )) {
                    String [] entry = getEntry(Arrays.copyOfRange(keys, 2, keys.length));
                    newsEntryList.add( new NewsEntry(entry) );
                }
            }

        } catch ( IOException | XmlPullParserException e ) {
            Log.e(LOG_TAG, "RSS link stopped working: " + e.getMessage());
            return new ArrayList<>();
        }

        return newsEntryList;
    }

    private void setUpParser() throws IOException, XmlPullParserException  {
        mParser = Xml.newPullParser();
        mParser.setInput( new StringReader( getRSSStream()));
        mParser.nextTag();
        mParser.require(XmlPullParser.START_TAG, null, mContext.getResources().getStringArray(R.array.rss_keys)[0]);
    }

    private String getRSSStream() throws IOException {
        URL url = new URL(mContext.getResources().getString(R.string.rss_url));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        BufferedReader reader = new BufferedReader( new InputStreamReader(connection.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String line;
        while ( (line = reader.readLine()) != null) {
            builder.append(line).append("\n");
        }
        reader.close();
        connection.disconnect();
        return builder.toString();
    }
}
