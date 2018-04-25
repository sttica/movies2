package com.example.android.movies;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.android.movies.data.Movie;
import com.example.android.movies.databinding.ActivityDetailBinding;
import com.example.android.movies.utilities.DatabaseUtils;
import com.example.android.movies.utilities.NetworkUtils;
import com.example.android.movies.utilities.themovieDbJsonUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Movie>> {

    private static final String TAG = themovieDbJsonUtils.class.getSimpleName();

    public static SQLiteDatabase mDatabase;

    ActivityDetailBinding mBinding;

    private static final int MOVIE_LOADER_ID = 1;

    private String API_KEY;

    private String movieId;

    private ArrayList<Movie> mMovieData = null;

    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();

        setContentView(R.layout.activity_detail);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle!=null)
        {
            movieId =(String) bundle.get("movieId");
            API_KEY =(String) bundle.get("API_KEY");
        }

        int loaderId = MOVIE_LOADER_ID;

        if(savedInstanceState == null || !savedInstanceState.containsKey("movie")) {
            android.support.v4.app.LoaderManager.LoaderCallbacks<ArrayList<Movie>> callback = DetailActivity.this;
            Bundle bundleForLoader = null;
            getSupportLoaderManager().initLoader(loaderId, bundleForLoader, callback);
        }
        else {
            mMovieData = savedInstanceState.getParcelableArrayList("movie");
            populateUi();
        }

        mBinding.favorite.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                if(!DatabaseUtils.isFavorite(movieId,context)){
                    isFavoriteUi();
                    DatabaseUtils.addFavorite(mMovieData, movieId, context);
                }else if(DatabaseUtils.isFavorite(movieId,context)){
                    isNotFavoriteUi();
                    DatabaseUtils.removeFavorite(movieId, context);
                }
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movie", mMovieData);
        super.onSaveInstanceState(outState);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public android.support.v4.content.Loader<ArrayList<Movie>> onCreateLoader(int id, final Bundle loaderArgs) {
        return new AsyncTaskLoader<ArrayList<Movie>>(this) {

            @Override
            protected void onStartLoading() {
                if (mMovieData != null) {
                    deliverResult(mMovieData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public ArrayList<Movie> loadInBackground() {

                URL movieRequestUrl = NetworkUtils.buildDetailUrl(movieId, API_KEY);

                try {
                    String jsonMovieResponse = NetworkUtils
                            .getResponseFromHttpUrl(movieRequestUrl);

                    ArrayList<Movie> simpleJsonMovieData = themovieDbJsonUtils
                            .getMovieFromJson(DetailActivity.this, jsonMovieResponse);

                    return simpleJsonMovieData;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(ArrayList<Movie> data) {
                mMovieData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
        if (data != null) {
            populateUi();
        } else {
            Log.v(TAG, "No movie data available");
        }
    }

    private void populateUi() {
        mBinding.title.setText(mMovieData.get(0).title);
        mBinding.releaseDate.setText(mMovieData.get(0).release_date);
        mBinding.plot.setText(mMovieData.get(0).overview);
        mBinding.voteAverage.setText(mMovieData.get(0).vote_average.toString());

        Context context = mBinding.poster.getContext();
        String pathToPoster = NetworkUtils.BASE_IMG_URL + "/" + context.getString(R.string.posterLoadSize) + "/" + mMovieData.get(0).poster_path;
        Picasso.with(context).load(pathToPoster).into(mBinding.poster);

        if(!DatabaseUtils.isFavorite(movieId, context)){
            isNotFavoriteUi();
        }else if(DatabaseUtils.isFavorite(movieId, context)){
            isFavoriteUi();
        }

    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<ArrayList<Movie>> loader) {

    }

    private void isFavoriteUi(){
        mBinding.favorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_black_24dp));
    };

    private void isNotFavoriteUi(){
        mBinding.favorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_border_black_24dp));
    };

}
