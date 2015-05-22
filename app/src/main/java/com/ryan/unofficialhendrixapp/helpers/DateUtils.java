package com.ryan.unofficialhendrixapp.helpers;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    public static final String LOG_TAG = "DateUtils";

    private static final String RSS_DATE_FORMAT = "EEE, dd MMM yyyy kk:mm:ss zzz";

    public static Date convertToDate( String date )  {
        Date ret;
        SimpleDateFormat sdf = new SimpleDateFormat(RSS_DATE_FORMAT, Locale.US);

        try {
            ret =  (Date) sdf.parseObject(date);
        } catch (ParseException e) {
            try {
                Log.e(LOG_TAG, "Date in feed is invalid. Switching to use java.util.Date.", e);
                ret =  convertToDate(Date.parse(date));
            } catch (Exception e1) {
                Log.e(LOG_TAG, "Date in feed is very invalid. Returning today's date.", e1);
                ret = Calendar.getInstance().getTime();
            }
        }

        return ret;
    }

    public static Date convertToDate(long unixTime) {
        return new Date(unixTime);
    }

    public static String getPresentableDate(Date date) {
        return SimpleDateFormat.getDateInstance().format(date);
    }
}
