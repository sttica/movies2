package com.example.android.movies;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.movies.data.Movie;
import com.example.android.movies.data.Trailer;
import com.example.android.movies.databinding.ActivityDetailBinding;
import com.example.android.movies.utilities.DatabaseUtils;
import com.example.android.movies.utilities.NetworkUtils;
import com.example.android.movies.utilities.themovieDbJsonUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    private static final String TAG = themovieDbJsonUtils.class.getSimpleName();

    public static SQLiteDatabase mDatabase;

    ActivityDetailBinding mBinding;

    private static final int MOVIE_LOADER_ID = 1;
    private static final int TRAILER_LOADER_ID = 2;

    private String API_KEY;
    private String movieId;

    private ArrayList<Movie> mMovieData = null;
    private ArrayList<Trailer> mTrailerData = null;

    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();

        setContentView(R.layout.activity_detail);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        mBinding.voteCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reviewIntent = new Intent(DetailActivity.this, ReviewActivity.class);
                reviewIntent.putExtra("movieId",movieId);
                reviewIntent.putExtra("API_KEY",API_KEY);
                startActivity(reviewIntent);
            }
        });

        mBinding.voteAverage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reviewIntent = new Intent(DetailActivity.this, ReviewActivity.class);
                reviewIntent.putExtra("movieId",movieId);
                reviewIntent.putExtra("API_KEY",API_KEY);
                startActivity(reviewIntent);
            }
        });

        if(bundle!=null)
        {
            movieId =(String) bundle.get("movieId");
            API_KEY =(String) bundle.get("API_KEY");
        }

        if(savedInstanceState == null || !savedInstanceState.containsKey("movie")) {
            android.support.v4.app.LoaderManager.LoaderCallbacks<ArrayList<Movie>> movieCallback = new android.support.v4.app.LoaderManager.LoaderCallbacks<ArrayList<Movie>>() {

                @NonNull
                @SuppressLint("StaticFieldLeak")
                @Override
                public android.support.v4.content.Loader<ArrayList<Movie>> onCreateLoader(int id, final Bundle loaderArgs) {
                    return new AsyncTaskLoader<ArrayList<Movie>>(context) {

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
                public void onLoadFinished(@NonNull Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
                    if (data != null) {
                        populateUi();
                    } else {
                        Log.v(TAG, "No movie data available");
                    }
                }


                @Override
                public void onLoaderReset(@NonNull Loader loader) {

                }
            };


            android.support.v4.app.LoaderManager.LoaderCallbacks<ArrayList<Trailer>> trailerCallback = new LoaderManager.LoaderCallbacks<ArrayList<Trailer>>() {

                @NonNull
                @Override
                public Loader<ArrayList<Trailer>> onCreateLoader(int id, final Bundle loaderArgs) {
                    return new AsyncTaskLoader<ArrayList<Trailer>>(context) {

                        @Override
                        protected void onStartLoading() {
                            if (mTrailerData != null) {
                                deliverResult(mTrailerData);
                            } else {
                                forceLoad();
                            }
                        }

                        @Override
                        public ArrayList<Trailer> loadInBackground() {

                            URL movieRequestUrl = NetworkUtils.buildTrailerUrl(movieId, API_KEY);

                            try {
                                String jsonMovieResponse = NetworkUtils
                                        .getResponseFromHttpUrl(movieRequestUrl);

                                ArrayList<Trailer> simpleJsonMovieData = themovieDbJsonUtils
                                        .getTrailerFromJson(DetailActivity.this, jsonMovieResponse);

                                return simpleJsonMovieData;

                            } catch (Exception e) {
                                e.printStackTrace();
                                return null;
                            }
                        }

                        public void deliverResult(ArrayList<Trailer> data) {
                            mTrailerData = data;
                            super.deliverResult(data);
                        }
                    };
                }

                @Override
                public void onLoadFinished(@NonNull Loader<ArrayList<Trailer>> loader, ArrayList<Trailer> data) {
                    if (data != null) {
                        addTrailers();
                    } else {
                        Log.v(TAG, "No trailers available");
                    }
                }

                @Override
                public void onLoaderReset(@NonNull Loader<ArrayList<Trailer>> loader) {

                }
            };

            Bundle movieBundleForLoader = null;
            getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, movieBundleForLoader, movieCallback);

            Bundle trailerBundleForLoader = null;
            getSupportLoaderManager().initLoader(TRAILER_LOADER_ID, trailerBundleForLoader, trailerCallback);
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
        outState.putParcelableArrayList("trailer", mMovieData);
        super.onSaveInstanceState(outState);
    }




    private void populateUi() {
        mBinding.title.setText(mMovieData.get(0).title);
        mBinding.releaseDate.setText(mMovieData.get(0).release_date);
        mBinding.plot.setText(mMovieData.get(0).overview);
        mBinding.voteAverage.setText(String.valueOf(mMovieData.get(0).vote_average));
        mBinding.voteCount.setText(String.valueOf(mMovieData.get(0).vote_count));

        Context context = mBinding.poster.getContext();
        String pathToPoster = NetworkUtils.BASE_IMG_URL + "/" + context.getString(R.string.posterLoadSize) + "/" + mMovieData.get(0).poster_path;
        Picasso.with(context).load(pathToPoster).into(mBinding.poster);

        if(!DatabaseUtils.isFavorite(movieId, context)){
            isNotFavoriteUi();
        }else if(DatabaseUtils.isFavorite(movieId, context)){
            isFavoriteUi();
        }

    }

    private void addTrailers() {

        int trailerCount = mTrailerData.size();;

        ConstraintLayout constraintLayout = (ConstraintLayout) mBinding.voteCount.getParent();

        if (trailerCount > 0) {

            LinearLayout linearLayout= new LinearLayout(context);
            linearLayout.setId(R.id.trailerLinearLayout);
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            constraintLayout.addView(linearLayout);

            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.connect(R.id.trailerLinearLayout,ConstraintSet.TOP,R.id.plot,ConstraintSet.BOTTOM,16);
            constraintSet.applyTo(constraintLayout);

            for (int i = 0; i < trailerCount; i++) {
                final String key = mTrailerData.get(i).key;
                TextView nameView = new TextView(context);
                nameView.setText(mTrailerData.get(i).name);

                nameView.setOnClickListener(new View.OnClickListener() {

                    Uri trailerUri = NetworkUtils.buildTrailerVideoUrl(key);

                    @Override
                    public void onClick(View view) {
                        Intent video = new Intent(Intent.ACTION_VIEW,trailerUri);
                        startActivity(video);
                    }
                });

                linearLayout.addView(nameView);
            }
        }
    }


    private void isFavoriteUi(){
        mBinding.favorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_black_24dp));
    };

    private void isNotFavoriteUi(){
        mBinding.favorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_border_black_24dp));
    };

    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader loader, Object data) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {

    }

}
