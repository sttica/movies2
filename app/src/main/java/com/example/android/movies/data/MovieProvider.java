package com.example.android.movies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class MovieProvider extends ContentProvider {


    public static final int CODE_ADD_MOVIE = 100;
    public static final int CODE_SHOW_MOVIE = 110;
    public static final int CODE_SHOW_MOVIES = 111;
    public static final int CODE_DELETE_MOVIE = 120;

    public static final String LOG_TAG = MovieProvider.class.getSimpleName();

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mOpenHelper;




    public static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_FAVORITES + "/a", CODE_ADD_MOVIE);
        matcher.addURI(authority, MovieContract.PATH_FAVORITES + "/s", CODE_SHOW_MOVIE);
        matcher.addURI(authority, MovieContract.PATH_FAVORITES, CODE_SHOW_MOVIES);
        matcher.addURI(authority, MovieContract.PATH_FAVORITES + "/d", CODE_DELETE_MOVIE);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase database = mOpenHelper.getReadableDatabase();

        Cursor cursor;

        switch (sUriMatcher.match(uri)) {

            case CODE_SHOW_MOVIES:
                Log.e(LOG_TAG, "CODE_SHOW_MOVIES: " + uri);

                cursor = database.query(
                        MovieContract.movieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case CODE_SHOW_MOVIE:
                Log.e(LOG_TAG, "CODE_SHOW_MOVIE: " + uri);
                //selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri))};
                selection = MovieContract.movieEntry.COLUMN_ID + "=?";
                cursor = database.query(
                        MovieContract.movieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case CODE_ADD_MOVIE:
                return MovieContract.movieEntry.CONTENT_MOVIE_TYPE;
            case CODE_SHOW_MOVIE:
                return MovieContract.movieEntry.CONTENT_MOVIE_TYPE;
            case CODE_SHOW_MOVIES:
                return MovieContract.movieEntry.CONTENT_TYPE;
            case CODE_DELETE_MOVIE:
                return MovieContract.movieEntry.CONTENT_MOVIE_TYPE;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        long id = -1;

        Log.e(LOG_TAG, "INSERT: " + sUriMatcher.match(uri));

        switch (sUriMatcher.match(uri)) {

            case CODE_ADD_MOVIE: {

                    id = mOpenHelper.getWritableDatabase().insert(
                    MovieContract.movieEntry.TABLE_NAME,
                    null,
                    values
                    );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        switch (sUriMatcher.match(uri)) {

            case CODE_DELETE_MOVIE: {

                int deleted = mOpenHelper.getWritableDatabase().delete(

                        MovieContract.movieEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                return deleted;
            }

            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
