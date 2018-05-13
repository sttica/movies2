package com.example.android.movies.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import static java.lang.String.valueOf;

/**
 * Created by Timo on 01.03.2018.
 */

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    public static final String BASE_IMG_URL = "https://image.tmdb.org/t/p/";

    public static final String YOUTUBE_URL = "https://www.youtube.com";


    private static final String BASE_URL =
            "https://api.themoviedb.org/3/movie/";

    public static URL buildMainUrl(String type,String API_KEY) {

        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(type)
                .appendQueryParameter("api_key", valueOf(API_KEY))
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildDetailUrl(String movieId,String API_KEY) {

        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(movieId)
                .appendQueryParameter("api_key", valueOf(API_KEY))
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildReviewUrl(String movieId,String API_KEY) {

        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(movieId)
                .appendPath("reviews")
                .appendQueryParameter("api_key", valueOf(API_KEY))
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildTrailerUrl(String movieId,String API_KEY) {

        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(movieId)
                .appendPath("videos")
                .appendQueryParameter("api_key", valueOf(API_KEY))
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static Uri buildTrailerVideoUrl(String key) {

        Uri uri = Uri.parse(YOUTUBE_URL).buildUpon()
                .appendPath("watch")
                .appendQueryParameter("v", key)
                .build();

        return uri;
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
