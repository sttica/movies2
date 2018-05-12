package com.example.android.movies.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Timo on 24.03.2018.
 */

public class Movie implements Parcelable{
    public String title;
    public String release_date;
    public String overview;
    public Double vote_average;
    public int vote_count;
    public String poster_path;

    public Movie(String title, String release_date, String overview, Double vote_average, int vote_count,String poster_path){
        this.title = title;
        this.release_date = release_date;
        this.overview = overview;
        this.vote_average = vote_average;
        this.vote_count = vote_count;
        this.poster_path = poster_path;
    }

    private Movie(Parcel in){
        title = in.readString();
        release_date = in.readString();
        overview = in.readString();
        vote_average = in.readDouble();
        poster_path = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }



    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(release_date);
        parcel.writeString(overview);
        parcel.writeDouble(vote_average);
        parcel.writeString(poster_path);
    }

    public final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }

    };

}
