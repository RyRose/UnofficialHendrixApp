package com.ryan.unofficialhendrixapp.data;

import android.provider.BaseColumns;

/**
 * Created by ryan on 12/27/14.
 */
public class HendrixContract {

    public static final class NewsColumn implements BaseColumns {

        public static final String TABLE_NAME = "news";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_LINK = "link";

    }

    public static final class StaffColumn implements BaseColumns {

        public static final String TABLE_NAME = "staff";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DEPARTMENT = "department";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_PHONE = "phone";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_LOCATION_LINE_1 = "location_line_1";
        public static final String COLUMN_LOCATION_LINE_2 = "location_line_2";
        public static final String COLUMN_PICTURE = "picture";

    }
}
