package com.ryan.unofficialhendrixapp.helpers;

import android.content.Context;
import android.net.ConnectivityManager;

import java.io.IOException;
import java.io.InputStream;

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

}
