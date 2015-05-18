package com.ryan.unofficialhendrixapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ryan.unofficialhendrixapp.data.HendrixContract.NewsColumn;
import com.ryan.unofficialhendrixapp.data.HendrixContract.StaffColumn;

public class HendrixDbHelper extends SQLiteOpenHelper {

    final static String NAME = "HENDRIXDB";
    final static int VERSION = 1;

    public HendrixDbHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_NEWS_TABLE = "CREATE TABLE " + NewsColumn.TABLE_NAME +
                "("  + NewsColumn._ID + " INTEGER PRIMARY KEY AUTOINCREMENT" +
                ", " + NewsColumn.COLUMN_TITLE+ " TEXT NOT NULL" +
                ", " + NewsColumn.COLUMN_DESCRIPTION + " TEXT NOT NULL" +
                ", " + NewsColumn.COLUMN_DATE + " TEXT NOT NULL" +
                ", " + NewsColumn.COLUMN_LINK + " TEXT NOT NULL" +
                ")";

        final String CREATE_STAFF_TABLE = "CREATE TABLE " + StaffColumn.TABLE_NAME + "( " +
                StaffColumn._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                StaffColumn.COLUMN_PICTURE + " TEXT NOT NULL, " +
                StaffColumn.COLUMN_NAME + " TEXT NOT NULL, " +
                StaffColumn.COLUMN_TITLE + " TEXT NOT NULL, " +
                StaffColumn.COLUMN_DEPARTMENT + " TEXT NOT NULL, " +
                StaffColumn.COLUMN_PHONE + " TEXT NOT NULL, " +
                StaffColumn.COLUMN_EMAIL + " TEXT NOT NULL, " +
                StaffColumn.COLUMN_LOCATION_LINE_1 + " TEXT NOT NULL, " +
                StaffColumn.COLUMN_LOCATION_LINE_2 + " TEXT NOT NULL)";

        db.execSQL( CREATE_NEWS_TABLE );
        db.execSQL( CREATE_STAFF_TABLE );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "DROP TABLE IF EXISTS " + NewsColumn.TABLE_NAME);
        db.execSQL( "DROP TABLE IF EXISTS " + StaffColumn.TABLE_NAME);
        onCreate(db);
    }
}
