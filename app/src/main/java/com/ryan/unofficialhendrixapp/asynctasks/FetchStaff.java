package com.ryan.unofficialhendrixapp.asynctasks;

/**
 * Created by ryan on 1/3/15.
 */

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.preference.Preference;
import android.widget.CursorAdapter;

import com.ryan.unofficialhendrixapp.R;
import com.ryan.unofficialhendrixapp.data.HendrixContract;
import com.ryan.unofficialhendrixapp.data.HendrixDbHelper;
import com.ryan.unofficialhendrixapp.helpers.DirectoryParser;
import com.ryan.unofficialhendrixapp.models.Person;

import java.util.ArrayList;

public class FetchStaff extends AsyncTask<String, Void, Cursor> {

    Context mContext;
    HendrixDbHelper mDbHelper;
    CursorAdapter mAdapter;

    public FetchStaff( Context context, HendrixDbHelper dbHelper, CursorAdapter adapter) {
        this.mContext = context;
        this.mDbHelper = dbHelper;
        this.mAdapter = adapter;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mAdapter.changeCursor(null);
    }

    @Override
    protected Cursor doInBackground(String... params) {
        ContentValues values;
        SQLiteDatabase db;

        if ( !isXmlRetrieved() ) {
            return mDbHelper.getReadableDatabase().query(HendrixContract.StaffColumn.TABLE_NAME,
                    params, null, null, null, null, null);
        }

        db = mDbHelper.getWritableDatabase();
        db.delete(HendrixContract.StaffColumn.TABLE_NAME, null, null);
        for ( Person person : getFilledList() ) {
            values = new ContentValues();
            values.put(HendrixContract.StaffColumn.COLUMN_PICTURE, person.getLink());
            values.put(HendrixContract.StaffColumn.COLUMN_NAME, person.getName());
            values.put(HendrixContract.StaffColumn.COLUMN_TITLE, person.getTitle());
            values.put(HendrixContract.StaffColumn.COLUMN_DEPARTMENT, person.getDept());
            values.put(HendrixContract.StaffColumn.COLUMN_PHONE, person.getPhone());
            values.put(HendrixContract.StaffColumn.COLUMN_EMAIL, person.getEmail());
            values.put(HendrixContract.StaffColumn.COLUMN_LOCATION_LINE_1, person.getline1());
            values.put(HendrixContract.StaffColumn.COLUMN_LOCATION_LINE_2, person.getline2());
            db.insert(HendrixContract.StaffColumn.TABLE_NAME, null, values);
        }
        db.close();
        db = mDbHelper.getReadableDatabase();
        return db.query(HendrixContract.StaffColumn.TABLE_NAME, params, null, null, null, null, null);
    }

    @Override
    protected void onPostExecute(Cursor cursor) {
        super.onPostExecute(cursor);
        mAdapter.changeCursor(cursor);
        mAdapter.notifyDataSetChanged();
        mContext = null;
    }

    private ArrayList<Person> getFilledList() {
        return new DirectoryParser(mContext).parse();
    }

    private boolean isXmlRetrieved(){
        SharedPreferences p =( (Activity) mContext ).getPreferences(Preference.DEFAULT_ORDER);
        return p.getBoolean(mContext.getResources().getString(R.string.directory_filled_key), false);
    }
}
