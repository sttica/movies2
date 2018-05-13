package com.example.android.movies.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Timo on 24.03.2018.
 */

public class Trailer implements Parcelable{
    public String key;
    public String name;

    public Trailer(String author, String content){
        this.key = author;
        this.name = content;
    }

    private Trailer(Parcel in){
        key = in.readString();
        name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(key);
        parcel.writeString(name);
    }

    public final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel parcel) {
            return new Trailer(parcel);
        }

        @Override
        public Trailer[] newArray(int i) {
            return new Trailer[i];
        }

    };

}
