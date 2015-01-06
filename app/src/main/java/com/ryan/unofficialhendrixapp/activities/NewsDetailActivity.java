package com.ryan.unofficialhendrixapp.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.ryan.unofficialhendrixapp.R;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * TODO Implement article extraction
 *
 * Displays the article linked to in each item of the listViewNews
 *
 */

public class NewsDetailActivity extends ActionBarActivity {

    /* Keywords for receiving intent */
    public static final String LINK = "link";
    public static final String TITLE = "title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        Intent intent = getIntent();
        String title = intent.getStringExtra(TITLE);
        String link = intent.getStringExtra(LINK);
        ( (TextView) findViewById(R.id.newsDetailTitleView)).setText(title);
        TextView descriptionView = ( (TextView) findViewById(R.id.newsDetailDescriptionView));
        new FetchArticle( descriptionView ).execute(link);
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
     * Extracts the article from the web page linked in each NewsItem
     *
     * TODO Implement extraction or get rid of the entire activity
     */

    private class FetchArticle extends AsyncTask<String, String, String> {

        private String LOG_TAG = getClass().getSimpleName();
        private TextView mView;

        public FetchArticle( TextView view ) {
            mView = view;
        }

        @Override
        protected String doInBackground(String... params) {
            String text = "";
            if(params.length==0) {
                return text;
            }

            try {
                URL url = new URL( params[0] );
            } catch ( MalformedURLException e) {
                Log.e(LOG_TAG, "Bad URL to article.");
                text = "";
            }

            return text;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mView.setText(s);
        }
    }
}
