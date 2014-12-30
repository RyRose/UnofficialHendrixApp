package com.ryan.unofficialhendrixapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ryan.unofficialhendrixapp.data.HendrixContract.NewsColumn;
import com.ryan.unofficialhendrixapp.data.HendrixContract.StaffColumn;
/**
 * Created by ryan on 12/27/14.
 */
public class HendrixDbHelper extends SQLiteOpenHelper {

    final static String NAME = "HENDRIXDB";
    final static int VERSION = 1;

    public HendrixDbHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
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
                StaffColumn.COLUMN_LOCATION_LINE_1 + " TEXT NOT NULL, " +
                StaffColumn.COLUMN_LOCATION_LINE_2 + " TEXT NOT NULL)";

        db.execSQL( CREATE_NEWS_TABLE );
        db.execSQL( CREATE_STAFF_TABLE );

    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p/>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "DROP TABLE IF EXISTS " + NewsColumn.TABLE_NAME);
        db.execSQL( "DROP TABLE IF EXISTS " + StaffColumn.TABLE_NAME);
        onCreate(db);
    }
}
