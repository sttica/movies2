package com.example.android.movies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.movies.data.MovieContract.movieEntry;

public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        /*
         * This String will contain a simple SQL statement that will create a table that will
         * cache our weather data.
         */
        final String SQL_CREATE_WEATHER_TABLE =

                "CREATE TABLE " + movieEntry.TABLE_NAME + " (" +
                        movieEntry.COLUMN_ID               + " INTEGER PRIMARY KEY NOT NULL, " +
                        movieEntry.COLUMN_TITLE       + " STRING NOT NULL, "                 +
                        movieEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL,"                  +
                        movieEntry.COLUMN_RELEASE_DATE   + " DATE NOT NULL, "                    +
                        movieEntry.COLUMN_SYNOPSIS   + " STRING NOT NULL, "                    +
                        movieEntry.COLUMN_POSTER_PATH   + " STRING NOT NULL"                    +
                        ")";

        db.execSQL(SQL_CREATE_WEATHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + movieEntry.TABLE_NAME);
        onCreate(db);
    }
}
