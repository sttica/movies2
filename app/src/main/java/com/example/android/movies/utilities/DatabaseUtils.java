package com.example.android.movies.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.android.movies.data.Movie;
import com.example.android.movies.data.MovieContract;
import com.example.android.movies.data.Movies;

import java.util.ArrayList;

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
                    MovieContract.movieEntry.CONTENT_URI.buildUpon().appendPath("s").build(),
                    mProjection,
                    null,
                    mSelectionArgs,
                    null);

            if(mCursor.moveToFirst()){
                return true;
            }
            else return false;
        }
        else return false;
    }

    public static void addFavorite(ArrayList<Movie> movie, String movieId, Context context){

        ContentValues contentValues = new ContentValues();

        contentValues.put(MovieContract.movieEntry.COLUMN_POSTER_PATH, movie.get(0).poster_path);
        contentValues.put(MovieContract.movieEntry.COLUMN_ID, movieId);
        contentValues.put(MovieContract.movieEntry.COLUMN_TITLE, movie.get(0).title);
        contentValues.put(MovieContract.movieEntry.COLUMN_RELEASE_DATE, movie.get(0).release_date);
        contentValues.put(MovieContract.movieEntry.COLUMN_SYNOPSIS, movie.get(0).overview);
        contentValues.put(MovieContract.movieEntry.COLUMN_VOTE_AVERAGE, movie.get(0).vote_average.toString());

        context.getContentResolver().insert(MovieContract.movieEntry.CONTENT_URI.buildUpon().appendPath("a").build(),contentValues);
    }

    public static void  removeFavorite(String id, Context context){

        if(id != null) {
            String selection = MovieContract.movieEntry.COLUMN_ID + "=?";
            String[] selectionArgs = {
                    id
            };
            int deleted = context.getContentResolver().delete(MovieContract.movieEntry.CONTENT_URI.buildUpon().appendPath("d").build(),selection,selectionArgs);
            Log.v(TAG, "DELETED: " + deleted);
        }

    }

    public static ArrayList<Movies> getFavorites(Context context){

        ArrayList<Movies> favorites = new ArrayList<>();

        mCursor = context.getContentResolver().query(
                MovieContract.movieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

        if(mCursor.moveToFirst()){
            do{
                String id = mCursor.getString(mCursor.getColumnIndexOrThrow(MovieContract.movieEntry.COLUMN_ID));
                String posterPath = mCursor.getString(mCursor.getColumnIndexOrThrow(MovieContract.movieEntry.COLUMN_POSTER_PATH));
                favorites.add(new Movies(posterPath,id));
            } while (mCursor.moveToNext());
        }

        return favorites;
    }


}
