package com.example.android.movies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.movies.data.Review;
import com.example.android.movies.utilities.NetworkUtils;

import java.util.ArrayList;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ReviewListAdapterViewHolder>{

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private ArrayList<Review> mReviewData;

    private final Context mContext;

    ReviewListAdapter(@NonNull Context context) {
        mContext = context;
    }

    @Override
    public ReviewListAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.review_item, viewGroup, false);
        return new ReviewListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewListAdapterViewHolder reviewListAdapterViewHolder, int position) {
        Review review = mReviewData.get(position);

        TextView authorView = reviewListAdapterViewHolder.authorView;
        authorView.setText(review.author);

        TextView contentView = reviewListAdapterViewHolder.contentView;
        contentView.setText(review.content);
    }

    @Override
    public int getItemCount() {
        if (null == mReviewData) return 0;
        return mReviewData.size();
    }

    public class ReviewListAdapterViewHolder extends RecyclerView.ViewHolder {
        final TextView authorView;
        final TextView contentView;

        ReviewListAdapterViewHolder(View view) {
            super(view);

            authorView = view.findViewById(R.id.author);
            contentView = view.findViewById(R.id.content);
        }

    }

    void setReviewData(ArrayList<Review> reviewData) {
        mReviewData = reviewData;
        notifyDataSetChanged();
    }
}