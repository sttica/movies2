package com.example.android.movies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.movies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;


public class GridAdapter extends RecyclerView.Adapter<GridAdapter.GridAdapterViewHolder>{

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private String[] mMovieData;

    private final Context mContext;
    final private GridAdapterOnClickHandler mClickHandler;

    public interface GridAdapterOnClickHandler {
        void onClick();
    }

    GridAdapter(@NonNull Context context, GridAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @Override
    public GridAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_item, viewGroup, false);
        return new GridAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GridAdapterViewHolder gridAdapterViewHolder, final int position) {

        Context context = gridAdapterViewHolder.posterView.getContext();

        String pathToPoster = NetworkUtils.BASE_IMG_URL + "/" + context.getString(R.string.posterLoadSize) + "/" + mMovieData[position];

        //Log.v(TAG, "Poster path " + String.valueOf(position) + ": " + pathToPoster);

        Picasso.with(context).load(pathToPoster).into(gridAdapterViewHolder.posterView);

    }

    @Override
    public int getItemCount() {
        if (null == mMovieData) return 0;
        return mMovieData.length;
    }

    public class GridAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView posterView;
        //final ImageView posterView;

        GridAdapterViewHolder(View view) {
            super(view);

            posterView = view.findViewById(R.id.poster);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            //mClickHandler.onClick();
        }
    }

    void setMovieData(String[] movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }
}