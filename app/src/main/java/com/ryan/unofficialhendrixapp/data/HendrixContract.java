package com.ryan.unofficialhendrixapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class HendrixContract {

    public static final String CONTENT_AUTHORITY = "com.ryan.unofficialhendrixapp.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_NEWS = "news";
    public static final String PATH_STAFF = "staff";

    public static final class NewsColumn implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_NEWS).build();

        public static final String TABLE_NAME = "news";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_LINK = "link";

    }

    public static final class StaffColumn implements BaseColumns {

        public static final String DISTINCT_DEPARTMENT = "dist_dept";
        public static final String DISTINCT_GROUPED_LNAME = "group_lname";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_STAFF).build();

        public static final Uri CONTENT_URI_WITH_DISTINCT_DEPARTMENT = CONTENT_URI.buildUpon()
                .appendPath(DISTINCT_DEPARTMENT).build();

        public static final Uri CONTENT_URI_WITH_GROUPED_LNAME_AND_FIRST_LETTER = CONTENT_URI.buildUpon()
                .appendPath(DISTINCT_GROUPED_LNAME).build();

        public static final String TABLE_NAME = "staff";
        public static final String COLUMN_FULL_NAME = "f_name";
        public static final String COLUMN_LAST_NAME = "l_name";
        public static final String COLUMN_DEPARTMENT = "department";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_PHONE = "phone";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_LOCATION_LINE_1 = "location_line_1";
        public static final String COLUMN_LOCATION_LINE_2 = "location_line_2";
        public static final String COLUMN_PICTURE = "picture";

    }
}
