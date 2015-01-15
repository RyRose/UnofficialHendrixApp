package com.ryan.unofficialhendrixapp.helpers;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class BaseParser {

    protected String readCategory(XmlPullParser parser, String category) throws IOException, XmlPullParserException {
        String data = "";
        parser.require(XmlPullParser.START_TAG, null, category);

        if (parser.next() == XmlPullParser.TEXT) {
            data = parser.getText();
            parser.nextTag();
        }

        parser.require(XmlPullParser.END_TAG, null, category);
        return data;
    }

    protected void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
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
