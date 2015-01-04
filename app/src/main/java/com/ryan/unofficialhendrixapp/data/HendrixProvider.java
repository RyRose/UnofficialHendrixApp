package com.ryan.unofficialhendrixapp.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

public class HendrixProvider extends ContentProvider {

    private static HendrixDbHelper mDbHelper;
    private static UriMatcher sUriMatcher;

    static  {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(HendrixContract.CONTENT_AUTHORITY, HendrixContract.PATH_NEWS, 1);
        sUriMatcher.addURI(HendrixContract.CONTENT_AUTHORITY, HendrixContract.PATH_STAFF, 2);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new HendrixDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        String table_name;
        SQLiteDatabase db;
        Cursor c;

        if ( ( table_name = getTableName(uri) ) == null) {
            return new CursorWrapper(null);
        }

        db = mDbHelper.getReadableDatabase();
        c = db.query(table_name, projection, selection, selectionArgs, null, null, null);
        db.close();
        return c;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String table_name;
        SQLiteDatabase db;

        if ( ( table_name = getTableName(uri) ) == null) {
            return null;
        }

        db = mDbHelper.getWritableDatabase();
        db.insert(table_name, null, values);
        db.close();
        return null;
    }

    @Override
    public int delete(Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        String table_name;
        SQLiteDatabase db;

        if ( ( table_name = getTableName(uri) ) == null) {
            return 0;
        }
        db = mDbHelper.getWritableDatabase();
        db.delete(table_name, selection, selectionArgs);
        db.close();
        return 1;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    private String getTableName ( Uri uri ) {
        switch( sUriMatcher.match(uri) ) {
            case 1:
                return HendrixContract.NewsColumn.TABLE_NAME;
            case 2:
                return HendrixContract.StaffColumn.TABLE_NAME;
            default:
                return null;
        }
    }
}
