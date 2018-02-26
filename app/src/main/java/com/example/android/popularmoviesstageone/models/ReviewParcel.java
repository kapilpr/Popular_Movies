package com.example.android.popularmoviesstageone.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ReviewParcel implements Parcelable {

    private String mId;
    private String mAuthor;
    private String mContent;
    private String mUrl;

    public ReviewParcel(String id, String author, String content, String url) {
        mId = id;
        mAuthor = author;
        mContent = content;
        mUrl = url;
    }

    protected ReviewParcel(Parcel in) {
        mId = in.readString();
        mAuthor = in.readString();
        mContent = in.readString();
        mUrl = in.readString();
    }

    public String getId() {
        return mId;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getContent() {
        return mContent;
    }

    public String getUrl() {
        return mUrl;
    }

    public static final Creator<ReviewParcel> CREATOR = new Creator<ReviewParcel>() {
        @Override
        public ReviewParcel createFromParcel(Parcel in) {
            return new ReviewParcel(in);
        }

        @Override
        public ReviewParcel[] newArray(int size) {
            return new ReviewParcel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mAuthor);
        dest.writeString(mContent);
        dest.writeString(mUrl);
    }
}

