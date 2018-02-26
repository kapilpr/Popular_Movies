package com.example.android.popularmoviesstageone.models;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieParcel implements Parcelable {

    private String mTitle;
    private String mOverview;
    private String mReleaseDate;
    private String mVotes;
    private String mPoster;
    private String mBackdrop;
    private String mMovieId;

    public MovieParcel(String title, String overview, String releaseDate, String votes, String poster, String backdrop, String movieId) {
        mTitle = title;
        mOverview = overview;
        mReleaseDate = releaseDate;
        mVotes = votes;
        mPoster = poster;
        mBackdrop = backdrop;
        mMovieId = movieId;
    }

    protected MovieParcel(Parcel in) {
        mTitle = in.readString();
        mOverview = in.readString();
        mReleaseDate = in.readString();
        mVotes = in.readString();
        mPoster = in.readString();
        mBackdrop = in.readString();
        mMovieId = in.readString();
    }

    public String getTitle() {
        return mTitle;
    }

    public String getOverview() {
        return mOverview;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public String getVotes() {
        return mVotes;
    }

    public String getPoster() {
        return mPoster;
    }

    public String getBackdrop() {
        return mBackdrop;
    }

    public String getMovieId() {
        return mMovieId;
    }

    public static final Creator<MovieParcel> CREATOR = new Creator<MovieParcel>() {
        @Override
        public MovieParcel createFromParcel(Parcel in) {
            return new MovieParcel(in);
        }

        @Override
        public MovieParcel[] newArray(int size) {
            return new MovieParcel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mOverview);
        dest.writeString(mReleaseDate);
        dest.writeString(mVotes);
        dest.writeString(mPoster);
        dest.writeString(mBackdrop);
        dest.writeString(mMovieId);
    }
}
