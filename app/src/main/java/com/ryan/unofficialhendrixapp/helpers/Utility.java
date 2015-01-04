package com.ryan.unofficialhendrixapp.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;


public class Utility {
    private static final String LOG_TAG = "Utility";

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
            Log.v(LOG_TAG, "Input Stream could not be closed: " + e.getMessage());
        }

    }

}
