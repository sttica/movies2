package com.example.android.movies.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.android.movies.R;

/**
 * Created by Timo on 12.03.2018.
 */

public class MoviePreferences {
    public static String getRequestType(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String keyForSort = context.getString(R.string.pref_sort_order_key);
        String defaultSort = context.getString(R.string.pref_sort_order_default_value);

        return sp.getString(keyForSort, defaultSort);
    }
}
