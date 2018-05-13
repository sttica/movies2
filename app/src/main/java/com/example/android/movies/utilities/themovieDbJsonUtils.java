package com.example.android.movies.utilities;

import android.content.Context;

import com.example.android.movies.data.Movie;
import com.example.android.movies.data.Movies;
import com.example.android.movies.data.Review;
import com.example.android.movies.data.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Timo on 01.03.2018.
 */

public class themovieDbJsonUtils {

    private static final String TAG = themovieDbJsonUtils.class.getSimpleName();

    private static final String MOVIES_LIST = "results";

    public static ArrayList<Movies> getMoviesFromJson(Context context, String moviesJsonStr)
            throws JSONException {

        JSONObject moviesJson = new JSONObject(moviesJsonStr);

        JSONArray resultsJson = moviesJson.getJSONArray("results");

        int resultsJsonLength = resultsJson.length();
        ArrayList<Movies> results = new ArrayList<>();

        if (resultsJsonLength > 0) {
            for (int i = 0; i < resultsJsonLength; i++) {
                results.add(new Movies(resultsJson.getJSONObject(i).getString("poster_path"),resultsJson.getJSONObject(i).getString("id")));
            }
        }

        return results;
    }

    public static ArrayList<Movie> getMovieFromJson(Context context, String movieJsonStr)
        throws JSONException {

        JSONObject movieJson = new JSONObject(movieJsonStr);

        ArrayList<Movie> result = new ArrayList<>();

        result.add(new Movie(
                movieJson.getString("title"),
                movieJson.getString("release_date"),
                movieJson.getString("overview"),
                movieJson.getDouble("vote_average"),
                movieJson.getInt("vote_count"),
                movieJson.getString("poster_path")
        ));


        return result;
    }

    public static ArrayList<Trailer> getTrailerFromJson(Context context, String trailerJsonStr)
            throws JSONException {

        JSONObject trailerJson = new JSONObject(trailerJsonStr);

        JSONArray resultsJson = trailerJson.getJSONArray("results");

        int resultsJsonLength = resultsJson.length();
        ArrayList<Trailer> result = new ArrayList<>();

        if (resultsJsonLength > 0) {
            for (int i = 0; i < resultsJsonLength; i++) {
                result.add(new Trailer(
                        resultsJson.getJSONObject(i).getString("key"),
                        resultsJson.getJSONObject(i).getString("name")
                ));
            }
        }



        return result;
    }

    public static ArrayList<Review> getReviewFromJson(Context context, String reviewJsonStr)
            throws JSONException {

        JSONObject reviewJson = new JSONObject(reviewJsonStr);

        JSONArray resultsJson = reviewJson.getJSONArray("results");

        int resultsJsonLength = resultsJson.length();
        ArrayList<Review> result = new ArrayList<>();

        if (resultsJsonLength > 0) {
            for (int i = 0; i < resultsJsonLength; i++) {
                result.add(new Review(
                        resultsJson.getJSONObject(i).getString("author"),
                        resultsJson.getJSONObject(i).getString("content")
                ));
            }
        }



        return result;
    }

}
