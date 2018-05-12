package com.example.android.movies.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Timo on 24.03.2018.
 */

public class Review implements Parcelable{
    public String author;
    public String content;

    public Review(String author, String content){
        this.author = author;
        this.content = content;
    }

    private Review(Parcel in){
        author = in.readString();
        content = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(author);
        parcel.writeString(content);
    }

    public final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel parcel) {
            return new Review(parcel);
        }

        @Override
        public Review[] newArray(int i) {
            return new Review[i];
        }

    };

}
