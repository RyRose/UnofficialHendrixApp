package com.ryan.unofficialhendrixapp.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
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
        sUriMatcher.addURI(HendrixContract.CONTENT_AUTHORITY, HendrixContract.PATH_STAFF + "/" + HendrixContract.DISTINCT, 3);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new HendrixDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        boolean distinct = false;
        String table;
        String groupBy = null;
        String having = null;
        String limit = null;

        SQLiteDatabase db;
        Cursor c;

        switch ( sUriMatcher.match(uri) ) {
            case 1:
                table = HendrixContract.NewsColumn.TABLE_NAME;
                break;
            case 3:
                groupBy = HendrixContract.StaffColumn.COLUMN_DEPARTMENT;
            case 2:
                table = HendrixContract.StaffColumn.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("URI not valid");
        }

        db = mDbHelper.getReadableDatabase();
        c = db.query(distinct, table, projection, selection, selectionArgs, groupBy, having, sortOrder, limit);
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
        return 1;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String table_name;
        if ( ( table_name = getTableName(uri) ) == null) {
            return 0;
        }
        return 1;
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
