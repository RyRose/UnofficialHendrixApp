package com.ryan.unofficialhendrixapp.helpers;

import android.content.Context;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public abstract class BaseParser {
    protected abstract XmlPullParser getParser();

    private final String LOG_TAG = getClass().getSimpleName();

    protected Context mContext;

    public BaseParser(Context context){
        mContext = context;
    }

    protected String [] getEntry(String [] keys) throws XmlPullParserException, IOException {

        getParser().require(XmlPullParser.START_TAG, null, keys[0]);
        String[] attributes = new String[keys.length - 1];
        while (getParser().next() != XmlPullParser.END_TAG) {
            if (getParser().getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String tag_name = getParser().getName();
            for(int i = 1; i < keys.length; i++) {
                if (tag_name.equals(keys[i])) {
                    attributes[i-1]= readCategory(tag_name);
                    break;
                } else if ( i == keys.length - 1) {
                    skip();
                }
            }
        }
        return attributes;
    }

    private String readCategory(String category) throws IOException, XmlPullParserException {
        String data = "";
        getParser().require(XmlPullParser.START_TAG, null, category);

        if (getParser().next() == XmlPullParser.TEXT) {
            data = getParser().getText();
            getParser().nextTag();
        }

        getParser().require(XmlPullParser.END_TAG, null, category);
        return data;
    }

    private void skip() throws XmlPullParserException, IOException {
        if (getParser().getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (getParser().next()) {
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
