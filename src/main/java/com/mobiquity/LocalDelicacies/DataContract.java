package com.mobiquity.LocalDelicacies;

import android.provider.BaseColumns;

/**
 * Created by dalexander on 7/25/14.
 */
public class DataContract {
    public static abstract class LocationEntry implements BaseColumns {
        public static final String TABLE_NAME = "locations";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_IMAGE_URL = "imageurl";
        public static final String COLUMN_NAME_BOOKMARKED = "bookmarked";
    }

    public static abstract class DelicacyEntry implements BaseColumns {
        public static final String TABLE_NAME = "delicacies";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_IMAGE_URL = "imageurl";
        public static final String COLUMN_NAME_BOOKMARKED = "bookmarked";
        public static final String COLUMN_NAME_RATING = "rating";
        public static final String COLUMN_NAME_CITY_ID = "cityid";
    }

    public static final String TEXT_TYPE = " TEXT";

    public static final String SQL_CREATE_LOCATION_TABLE =
            "CREATE TABLE " + LocationEntry.TABLE_NAME + " (" +
                    LocationEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    LocationEntry.COLUMN_NAME_NAME + " TEXT," +
                    LocationEntry.COLUMN_NAME_DESCRIPTION + " TEXT," +
                    LocationEntry.COLUMN_NAME_IMAGE_URL + " TEXT," +
                    LocationEntry.COLUMN_NAME_BOOKMARKED + " INTEGER" +
            " )";

    public static final String SQL_DELETE_LOCATION_TABLE =
            "DROP TABLE IF EXISTS " + LocationEntry.TABLE_NAME;


    public static final String SQL_CREATE_DELICACY_TABLE =
            "CREATE TABLE " + DelicacyEntry.TABLE_NAME + " (" +
                    DelicacyEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DelicacyEntry.COLUMN_NAME_NAME + " TEXT, " +
                    DelicacyEntry.COLUMN_NAME_DESCRIPTION + " TEXT, " +
                    DelicacyEntry.COLUMN_NAME_IMAGE_URL + " TEXT, " +
                    DelicacyEntry.COLUMN_NAME_BOOKMARKED + " INTEGER, " +
                    DelicacyEntry.COLUMN_NAME_RATING + " INTEGER, " +
                    DelicacyEntry.COLUMN_NAME_CITY_ID + " INTEGER, " +
                    "FOREIGN KEY ("+ DelicacyEntry.COLUMN_NAME_CITY_ID + ") REFERENCES "+LocationEntry.TABLE_NAME+"("+LocationEntry._ID+")" +
                    " )";

    public static final String SQL_DELETE_DELICACY_TABLE =
            "DROP TABLE IF EXISTS " + DelicacyEntry.TABLE_NAME;
}
