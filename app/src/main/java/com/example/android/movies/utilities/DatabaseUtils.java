package com.example.android.movies.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.android.movies.data.MovieContract;

public class DatabaseUtils {

    private static final String TAG = themovieDbJsonUtils.class.getSimpleName();

    private static Cursor mCursor;

    public static boolean isFavorite(String id,Context context){

        Log.v(TAG, "id: " + id);

        if (id != null){
            String[] mSelectionArgs = {""};
            mSelectionArgs[0] = id;
            String[] mProjection =
                    {
                            MovieContract.movieEntry.COLUMN_ID
                    };

            mCursor = context.getContentResolver().query(
                    MovieContract.BASE_CONTENT_URI.buildUpon().appendPath(MovieContract.PATH_FAVORITES).appendPath("/#").build(),
                    mProjection,
                    null,
                    mSelectionArgs,
                    null);
            return true;
        }
        else return false;
    }

    public static void addFavorite(String id, ContentValues contentValues, Context context){

        /*
        if (newUri == null) {
            Toast.makeText(this, getString(R.string.detail_insert_item_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.detail_insert_item_successful),
                    Toast.LENGTH_SHORT).show();
        }
        */


    }

    public static void  removeFavorite(String id, Context context){

    }

}
