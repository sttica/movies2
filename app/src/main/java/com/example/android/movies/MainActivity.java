package com.example.android.movies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.android.movies.data.Movies;
import com.example.android.movies.databinding.ActivityMainBinding;
import com.example.android.movies.utilities.NetworkUtils;
import com.example.android.movies.utilities.themovieDbJsonUtils;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        GridAdapter.GridAdapterOnClickHandler, LoaderCallbacks<ArrayList<Movies>>,SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = themovieDbJsonUtils.class.getSimpleName();

    private String mRequestType;

    private GridAdapter mGridAdapter;

    private ArrayList<Movies> mMovieData = null;

    // load keys library
    static {
        System.loadLibrary("keys");
    }

    // declare native function
    public native String getNativeKey1();

    // get API_KEY string
    String API_KEY = new String(Base64.decode(getNativeKey1(),Base64.DEFAULT));

    ActivityMainBinding mBinding;

    private static final int MOVIES_LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mGridAdapter = new GridAdapter(this,this);
        mBinding.recyclerView.setAdapter(mGridAdapter);

        setupSharedPreferences();

        int loaderId = MOVIES_LOADER_ID;

        if(savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
            android.support.v4.app.LoaderManager.LoaderCallbacks<ArrayList<Movies>> callback = MainActivity.this;
            Bundle bundleForLoader = null;
            getSupportLoaderManager().initLoader(loaderId, bundleForLoader, callback);
        }
        else {
            mMovieData = savedInstanceState.getParcelableArrayList("movies");
            mGridAdapter.setMovieData(mMovieData);
        }

    }

    private void setupSharedPreferences() {
        // Get all of the values from shared preferences to set it up
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mRequestType = sharedPreferences.getString(getString(R.string.pref_sort_order_key),getString(R.string.pref_sort_order_default_value));
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", mMovieData);
        super.onSaveInstanceState(outState);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public android.support.v4.content.Loader<ArrayList<Movies>> onCreateLoader(int id, final Bundle loaderArgs) {
        return new AsyncTaskLoader<ArrayList<Movies>>(this) {

            @Override
            protected void onStartLoading() {
                if (mMovieData != null) {
                    deliverResult(mMovieData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public ArrayList<Movies> loadInBackground() {

                URL movieRequestUrl = NetworkUtils.buildMainUrl(mRequestType, API_KEY);

                try {
                    String jsonMovieResponse = NetworkUtils
                            .getResponseFromHttpUrl(movieRequestUrl);

                    ArrayList<Movies> simpleJsonMoviesData = themovieDbJsonUtils
                            .getMoviesFromJson(MainActivity.this, jsonMovieResponse);

                    return simpleJsonMoviesData;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(ArrayList<Movies> data) {
                mMovieData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<ArrayList<Movies>> loader, ArrayList<Movies> data) {
        if (data != null) {
            mGridAdapter.setMovieData(data);
        } else {
            Log.v(TAG, "No movie data available");
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<ArrayList<Movies>> loader) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(int position) {
        Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);
        detailIntent.putExtra("movieId",mMovieData.get(position).movieId);
        detailIntent.putExtra("API_KEY",API_KEY);
        Log.v(TAG, "movieId" + mMovieData.get(position).movieId);
        startActivity(detailIntent);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

}
