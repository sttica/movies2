package com.example.android.movies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.example.android.movies.data.Review;
import com.example.android.movies.databinding.ActivityReviewBinding;
import com.example.android.movies.utilities.NetworkUtils;
import com.example.android.movies.utilities.themovieDbJsonUtils;

import java.net.URL;
import java.util.ArrayList;

public class ReviewActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Review>> {

    private static final String TAG = themovieDbJsonUtils.class.getSimpleName();

    ActivityReviewBinding mBinding;
    private String API_KEY;
    private String movieId;

    private ReviewListAdapter mReviewListAdapter;

    private static final int REVIEWS_LOADER_ID = 2;

    private ArrayList<Review> mReviewData = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_review);

        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mReviewListAdapter = new ReviewListAdapter(this);
        mBinding.recyclerView.setAdapter(mReviewListAdapter);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        int loaderId = REVIEWS_LOADER_ID;

        if(bundle!=null)
        {
            movieId =(String) bundle.get("movieId");
            API_KEY =(String) bundle.get("API_KEY");
        }

        if(savedInstanceState == null || !savedInstanceState.containsKey("movie")) {
            android.support.v4.app.LoaderManager.LoaderCallbacks<ArrayList<Review>> callback = ReviewActivity.this;
            Bundle bundleForLoader = null;
            getSupportLoaderManager().initLoader(loaderId, bundleForLoader, callback);
        }
        else {
            mReviewData = savedInstanceState.getParcelableArrayList("review");
        }
    }


    @SuppressLint("StaticFieldLeak")
    @Override
    public android.support.v4.content.Loader<ArrayList<Review>> onCreateLoader(int id, final Bundle loaderArgs) {
        return new AsyncTaskLoader<ArrayList<Review>>(this) {

            @Override
            protected void onStartLoading() {

                if (mReviewData != null) {
                    deliverResult(mReviewData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public ArrayList<Review> loadInBackground() {
                URL reviewRequestUrl = NetworkUtils.buildReviewUrl(movieId, API_KEY);

                try {
                    String jsonReviewResponse = NetworkUtils
                            .getResponseFromHttpUrl(reviewRequestUrl);

                    Log.d(TAG, "Review - jsonReviewResponse: " + jsonReviewResponse);

                    ArrayList<Review> simpleJsonReviewData = themovieDbJsonUtils
                            .getReviewFromJson(ReviewActivity.this, jsonReviewResponse);

                    Log.d(TAG, "Review - simpleJsonReviewData: " + simpleJsonReviewData.get(0).author);

                    return simpleJsonReviewData;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(ArrayList<Review> data) {
                mReviewData = data;

                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Review>> loader, ArrayList<Review> data) {
        if (data != null) {
            mReviewListAdapter.setReviewData(data);
        } else {
            Log.v(TAG, "No movie data available");
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Review>> loader) {

    }


}
