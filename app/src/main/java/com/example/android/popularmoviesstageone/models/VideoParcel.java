package com.example.android.popularmoviesstageone.models;

import android.os.Parcel;
import android.os.Parcelable;

public class VideoParcel implements Parcelable {

    private String mId;
    private String mName;
    private String mKey;
    private String mSite;

    public VideoParcel(String id, String name, String key, String site) {
        mId = id;
        mName = name;
        mKey = key;
        mSite = site;
    }

    protected VideoParcel(Parcel in) {
        mId = in.readString();
        mName = in.readString();
        mKey = in.readString();
        mSite = in.readString();
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getKey() {
        return mKey;
    }

    public String getSite() {
        return mSite;
    }

    public static final Creator<VideoParcel> CREATOR = new Creator<VideoParcel>() {
        @Override
        public VideoParcel createFromParcel(Parcel in) {
            return new VideoParcel(in);
        }

        @Override
        public VideoParcel[] newArray(int size) {
            return new VideoParcel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mName);
        dest.writeString(mKey);
        dest.writeString(mSite);
    }
}
