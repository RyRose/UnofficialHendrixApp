package com.ryan.unofficialhendrixapp.helpers;

import android.content.Context;
import android.net.ConnectivityManager;

import com.ryan.unofficialhendrixapp.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by ryan on 12/18/14.
 */
public class Utility {

    public static boolean isOnline( Context context ) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public static void closeInputStream ( InputStream inputStream ) {
        try {
            inputStream.close();
        } catch ( IOException e ) {
            e.printStackTrace();
        }

    }

    public static InputStream getRSSStream(Context context) {
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
