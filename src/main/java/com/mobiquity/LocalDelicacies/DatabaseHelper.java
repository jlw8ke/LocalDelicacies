package com.mobiquity.LocalDelicacies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.mobiquity.LocalDelicacies.delicacies.Delicacy;
import com.mobiquity.LocalDelicacies.location.Location;

import java.util.ArrayList;

/**
 * Created by dalexander on 7/25/14.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TAG = "com.mobiquity.LocalDelicacies.SQLiteOpenHelper";

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "LocalDelicacies.db";
    private static DatabaseHelper instance;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHelper getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataContract.SQL_CREATE_LOCATION_TABLE);
        db.execSQL(DataContract.SQL_CREATE_DELICACY_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(DataContract.SQL_DELETE_DELICACY_TABLE);
        db.execSQL(DataContract.SQL_DELETE_LOCATION_TABLE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public static Cursor getLocationCursor(Context context) {
        SQLiteDatabase db = new DatabaseHelper(context).getReadableDatabase();

        return getLocationCursor(db);
    }

    static String[] cityColumns = {
            DataContract.LocationEntry._ID,
            DataContract.LocationEntry.COLUMN_NAME_NAME,
            DataContract.LocationEntry.COLUMN_NAME_DESCRIPTION,
            DataContract.LocationEntry.COLUMN_NAME_IMAGE_URL,
            DataContract.LocationEntry.COLUMN_NAME_BOOKMARKED
    };

    public static Cursor getLocationCursor(SQLiteDatabase db){


        Cursor locCursor = db.query(
                DataContract.LocationEntry.TABLE_NAME,  // The table to query
                cityColumns,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        return locCursor;
    }

    public static Cursor getDelicacyCursor(Context context) {

        SQLiteDatabase db = new DatabaseHelper(context).getReadableDatabase();

        return getDelicacyCursor(db);
    }

    private static String[] delicacyColumns = {
            DataContract.DelicacyEntry._ID,
            DataContract.DelicacyEntry.COLUMN_NAME_NAME,
            DataContract.DelicacyEntry.COLUMN_NAME_DESCRIPTION,
            DataContract.DelicacyEntry.COLUMN_NAME_IMAGE_URL,
            DataContract.DelicacyEntry.COLUMN_NAME_BOOKMARKED,
            DataContract.DelicacyEntry.COLUMN_NAME_CITY_ID,
            DataContract.DelicacyEntry.COLUMN_NAME_RATING
    };

    public static Cursor getDelicacyCursor(SQLiteDatabase db){

        Cursor delCursor = db.query(
                DataContract.DelicacyEntry.TABLE_NAME,  // The table to query
                delicacyColumns,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        return delCursor;
    }

    public static ArrayList<Location> getLocations(Context context) {
        Cursor cursor = getLocationCursor(context);

        ArrayList<Location> data = new ArrayList<Location>();

        for(int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            Location loc = new Location(cursor);

            data.add(loc);
        }
        cursor.close();
        return data;
    }

    public static ArrayList<Delicacy> getDelicacies(Context context, Location location) {
        Cursor cursor;
        SQLiteDatabase db = new DatabaseHelper(context).getReadableDatabase();

        if(location == null) {
            cursor = getDelicacyCursor(context);
        } else {
            cursor = getDelicacyCursorByCity(db, location.getId());
        }

        ArrayList<Delicacy> data = new ArrayList<Delicacy>();

        for(int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            Delicacy del = new Delicacy(cursor);

            data.add(del);
        }
        cursor.close();

        return data;
    }

    public static Cursor getDelicacyCursorByCity(SQLiteDatabase db, int argid){

        Cursor delCursor = db.query(
                DataContract.DelicacyEntry.TABLE_NAME,  // The table to query
                delicacyColumns,                        // The columns to return
                "cityid=?",                                 // The columns for the WHERE clause
                new String[]{"" + argid},                 // The values for the WHERE clause
                null,                                   // don't group the rows
                null,                                   // don't filter by row groups
                null                                    // The sort order
        );
        return delCursor;
    }

    public static Location getLocationData(Context context, int cityid) {
        SQLiteDatabase db = new DatabaseHelper(context).getReadableDatabase();

        Cursor delCursor = db.query(
                DataContract.LocationEntry.TABLE_NAME,  // The table to query
                cityColumns,                        // The columns to return
                DataContract.LocationEntry._ID+"=?",                                 // The columns for the WHERE clause
                new String[]{"" + cityid},                 // The values for the WHERE clause
                null,                                   // don't group the rows
                null,                                   // don't filter by row groups
                null                                    // The sort order
        );

        delCursor.moveToFirst();

        Location l = new Location(delCursor);

        db.close();
        delCursor.close();

        return l;
    }

    public static Delicacy getDelicacyData(Context context, int id) {
        SQLiteDatabase db = new DatabaseHelper(context).getReadableDatabase();

        Cursor delCursor = db.query(
                DataContract.DelicacyEntry.TABLE_NAME,  // The table to query
                delicacyColumns,                        // The columns to return
                DataContract.DelicacyEntry._ID+"=?",                                 // The columns for the WHERE clause
                new String[]{"" + id},                 // The values for the WHERE clause
                null,                                   // don't group the rows
                null,                                   // don't filter by row groups
                null                                    // The sort order
        );

        delCursor.moveToFirst();

        Delicacy del = new Delicacy(delCursor);

        db.close();
        delCursor.close();

        return del;
    }

    @Override
    public void close() {
        Log.d(TAG, "close called on helper");
        super.close();
    }

}