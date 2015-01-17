package com.ryan.unofficialhendrixapp.helpers;


import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.Log;

import com.ryan.unofficialhendrixapp.R;
import com.ryan.unofficialhendrixapp.models.Staff;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class StaffParser extends BaseParser {
    private final String LOG_TAG = getClass().getSimpleName();

    private Context mContext;
    XmlResourceParser mParser;

    public StaffParser(Context context) {
        mContext = context;
    }

    @Override
    public XmlPullParser getParser() {
        return mParser;
    }

    public ArrayList<Staff> getList() {
        String [] keys = mContext.getResources().getStringArray(R.array.dir_keys);
        ArrayList<Staff> staffList = new ArrayList<>();

        try {
            setUpParser();
            while ( mParser.next() != XmlPullParser.END_DOCUMENT ) {
                if (mParser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = mParser.getName();
                if (name.equals( keys[1] )) {
                    String [] entry = getEntry(Arrays.copyOfRange(keys, 1, keys.length));
                    staffList.add( new Staff(entry) );
                }
            }

        } catch ( IOException | XmlPullParserException e ) {
            Log.e(LOG_TAG, "Cannot access data: " + e.getMessage());
            staffList = new ArrayList<>();
        }

        return staffList;
    }

    private void setUpParser() throws IOException, XmlPullParserException{
        mParser = mContext.getResources().getXml(R.xml.staff);
        mParser.next();
        mParser.nextTag();
        mParser.require(XmlPullParser.START_TAG, null, mContext.getResources().getStringArray(R.array.dir_keys)[0]);
    }
}
