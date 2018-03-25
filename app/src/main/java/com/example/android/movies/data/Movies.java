package com.example.android.movies.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Timo on 24.03.2018.
 */

public class Movies implements Parcelable{
    public String poster;
    public String movieId;

    public Movies(String poster, String movieId){
        this.poster = poster;
        this.movieId = movieId;
    }

    private Movies(Parcel in){
        poster = in.readString();
        movieId = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(poster);
        parcel.writeString(movieId);
    }

    public final Parcelable.Creator<Movies> CREATOR = new Parcelable.Creator<Movies>() {
        @Override
        public Movies createFromParcel(Parcel parcel) {
            return new Movies(parcel);
        }

        @Override
        public Movies[] newArray(int i) {
            return new Movies[i];
        }

    };

}
