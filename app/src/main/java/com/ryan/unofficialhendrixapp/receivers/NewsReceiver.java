package com.ryan.unofficialhendrixapp.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ryan.unofficialhendrixapp.services.NewsRefreshService;

public class NewsReceiver extends BroadcastReceiver {
    public final String LOG_TAG = getClass().getSimpleName();

    public static final int REQUEST_CODE = 0xDEADBEEF;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, NewsRefreshService.class);
        serviceIntent.putExtra(NewsRefreshService.DISPLAY_NOTIFICATION_KEY, true);
        context.startService(serviceIntent);
    }

}
