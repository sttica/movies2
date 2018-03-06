package com.example.android.movies.utilities;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Timo on 01.03.2018.
 */

public class themovieDbJsonUtils {

    private static final String TAG = themovieDbJsonUtils.class.getSimpleName();

    private static final String MOVIES_LIST = "results";

    public static String[] getMoviesFromJson(Context context, String moviesJsonStr)
            throws JSONException {

        JSONObject moviesJson = new JSONObject(moviesJsonStr);

        JSONArray resultsJson = moviesJson.getJSONArray("results");

        int resultsJsonLength = resultsJson.length();
        List<String> results = new ArrayList<>();

        if (resultsJsonLength > 0) {
            for (int i = 0; i < resultsJsonLength; i++) {
                results.add(resultsJson.getJSONObject(i).getString("poster_path"));
            }
        }

        String[] moviePosters = new String[results.size()];
        moviePosters = results.toArray(moviePosters);

        return moviePosters;
    }

}
