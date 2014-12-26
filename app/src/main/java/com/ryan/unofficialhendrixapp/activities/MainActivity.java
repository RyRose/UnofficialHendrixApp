package com.ryan.unofficialhendrixapp.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ryan.unofficialhendrixapp.R;
import com.ryan.unofficialhendrixapp.adapters.NewsAdapter;
import com.ryan.unofficialhendrixapp.helpers.Utility;
import com.ryan.unofficialhendrixapp.models.NewsEntry;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NewsAdapter newsAdapter = new NewsAdapter(this, R.id.list_item);
        new FetchNews (this, newsAdapter).execute();
        mListView = (ListView) findViewById(R.id.listViewNews);
        mListView.setAdapter( newsAdapter );
        mListView.setOnItemClickListener( new AdapterView.OnItemClickListener() { // TODO: Add listener for listViewNews
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String link = ((NewsEntry) parent.getAdapter().getItem(position)).getLink();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     *
     * Fetches the xml document from the rss feed showing the current news on Hendrix College's Website
     * to be displayed in a list view on the main activity
     *
     */
    private class FetchNews extends AsyncTask< ArrayList<NewsEntry>, Void, ArrayList<NewsEntry> > {

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
         *
         * @param params ArrayList of entries previously retrieved from rss feed. If not yet retrieved,
         *               it is null.
         * @return       ArrayList of entries of current rss feed unless the internet is offline then
         *               returns an empty ArrayList<Entry>
         */
        @Override
        protected ArrayList<NewsEntry> doInBackground(ArrayList<NewsEntry>... params) {
            XmlPullParser parser;
            ArrayList<NewsEntry> newsEntryList = new ArrayList<NewsEntry>();

            if ( !Utility.isOnline(mContext) ) { // If an error arises, keep the list of entries same
                //entryList = params[0];
            } else if ( params.length != 0 && !params[0].isEmpty() ) { // If there are already entries, update it with new entries
                //entryList = appendNewValues(params[0]);
            } else { // If we are starting a clean slate, put all the entries in the rss_url feed into entryList
                newsEntryList = getFilledEntryList(new ArrayList<NewsEntry>());
            }

            return newsEntryList;
        }

        @Override
        protected void onPostExecute( ArrayList<NewsEntry> newsEntryList) {
            super.onPostExecute(newsEntryList);
            mNewsAdapter.addAll(newsEntryList);
        }

        /**
         * TODO: Set up appending of new entries
         */
        private ArrayList<NewsEntry> appendNewValues(ArrayList<NewsEntry> list) {
            return list;
        }

        private ArrayList<NewsEntry> getFilledEntryList(ArrayList<NewsEntry> list) {
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

            } catch ( IOException |XmlPullParserException e ) {
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
            return new NewsEntry(title, link, description, date.substring(0, 16));
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
}
