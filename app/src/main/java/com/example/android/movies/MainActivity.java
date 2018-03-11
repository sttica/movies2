package com.example.android.movies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.android.movies.databinding.ActivityMainBinding;
import com.example.android.movies.utilities.NetworkUtils;
import com.example.android.movies.utilities.themovieDbJsonUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements
        GridAdapter.GridAdapterOnClickHandler, LoaderCallbacks<String[]>,SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = themovieDbJsonUtils.class.getSimpleName();

    private GridAdapter mGridAdapter;

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

        int loaderId = MOVIES_LOADER_ID;

        android.support.v4.app.LoaderManager.LoaderCallbacks<String[]> callback = MainActivity.this;

        Bundle bundleForLoader = null;

        getSupportLoaderManager().initLoader(loaderId, bundleForLoader, callback);

    }


    @Override
    public android.support.v4.content.Loader<String[]> onCreateLoader(int id, final Bundle loaderArgs) {
        return new AsyncTaskLoader<String[]>(this) {

            String[] mMovieData = null;

            @Override
            protected void onStartLoading() {
                if (mMovieData != null) {
                    deliverResult(mMovieData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public String[] loadInBackground() {

                //TODO set type via settings
                String type = "popular";
                URL movieRequestUrl = NetworkUtils.buildUrl(type,API_KEY);

                try {
                    String jsonMovieResponse = NetworkUtils
                            .getResponseFromHttpUrl(movieRequestUrl);

                    String[] simpleJsonMovieData = themovieDbJsonUtils
                            .getMoviesFromJson(MainActivity.this, jsonMovieResponse);

                    return simpleJsonMovieData;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(String[] data) {
                mMovieData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<String[]> loader, String[] data) {
        if (data != null) {
            mGridAdapter.setMovieData(data);
        } else {
            Log.v(TAG, "No movie data available");
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<String[]> loader) {

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
    public void onClick() {
        Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);
        startActivity(detailIntent);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

    }

}
