package com.ryan.unofficialhendrixapp.helpers;

import android.content.Context;
import android.net.ConnectivityManager;


public class Utility {
    private static final String LOG_TAG = "Utility";

    public static boolean isOnline( Context context ) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
