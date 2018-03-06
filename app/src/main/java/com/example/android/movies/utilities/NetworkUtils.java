package com.example.android.movies.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Timo on 01.03.2018.
 */

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    public static final String BASE_URL = "https://image.tmdb.org/t/p/";

    private static final String STATIC_POPULAR_URL =
            "https://api.themoviedb.org/3/movie/popular";

    public static URL buildUrl(String popularQuery, String API_KEY) {
        Uri uri = Uri.parse(STATIC_POPULAR_URL).buildUpon()
                .appendQueryParameter("api_key", String.valueOf(API_KEY))
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();

            return response;
        } finally {
            urlConnection.disconnect();
        }
    }

}
