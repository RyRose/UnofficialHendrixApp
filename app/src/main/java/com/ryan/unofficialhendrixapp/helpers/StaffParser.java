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

public class StaffParser extends BaseParser{
    private final String LOG_TAG = getClass().getSimpleName();

    private Context mContext;

    public StaffParser(Context context) {
        mContext = context;
    }

    public ArrayList<Staff> parse() {
        XmlResourceParser parser = mContext.getResources().getXml(R.xml.staff);
        ArrayList<Staff> staffList = new ArrayList<Staff>();

        try {
            parser.next();
            parser.nextTag();
            parser.require( XmlPullParser.START_TAG, null, mContext.getResources().getStringArray(R.array.dir_keys)[0]);

            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                if (name.equals( mContext.getResources().getStringArray(R.array.dir_keys)[1] )) {
                    staffList.add(getEntry(parser));
                }
            }

        } catch ( IOException | XmlPullParserException e ) {
            Log.e(LOG_TAG, "Cannot access data: " + e.getMessage());
            e.printStackTrace();
            staffList = new ArrayList<Staff>();
        }

        return staffList;
    }

    private Staff getEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        String item_key  = mContext.getResources().getStringArray(R.array.dir_keys)[1];
        String link_key  = mContext.getResources().getStringArray(R.array.dir_keys)[2];
        String name_key  = mContext.getResources().getStringArray(R.array.dir_keys)[3];
        String title_key = mContext.getResources().getStringArray(R.array.dir_keys)[4];
        String dept_key  = mContext.getResources().getStringArray(R.array.dir_keys)[5];
        String phone_key = mContext.getResources().getStringArray(R.array.dir_keys)[6];
        String email_key = mContext.getResources().getStringArray(R.array.dir_keys)[7];
        String line1_key = mContext.getResources().getStringArray(R.array.dir_keys)[8];
        String line2_key = mContext.getResources().getStringArray(R.array.dir_keys)[9];

        String tag_name;
        String link = "";
        String name = "";
        String title = "";
        String dept = "";
        String phone = "";
        String email = "";
        String line1 = "";
        String line2 = "";

        parser.require(XmlPullParser.START_TAG, null, item_key);

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            tag_name = parser.getName();
            if      (tag_name.equals(link_key) ) link = readCategory(parser, link_key);
            else if (tag_name.equals(name_key) ) name = readCategory(parser, name_key);
            else if (tag_name.equals(title_key)) title = readCategory(parser, title_key);
            else if (tag_name.equals(dept_key) ) {
                dept = readCategory(parser, dept_key);
                if ( dept.isEmpty() ) {
                    dept = "Other";
                }
            }
            else if (tag_name.equals(phone_key)) phone = readCategory(parser, phone_key);
            else if (tag_name.equals(email_key)) email = readCategory(parser, email_key);
            else if (tag_name.equals(line1_key)) line1 = readCategory(parser, line1_key);
            else if (tag_name.equals(line2_key)) line2 = readCategory(parser, line2_key);
            else skip(parser);

        }
        return new Staff(link, name, title, dept, phone, email, line1, line2);
    }
}
