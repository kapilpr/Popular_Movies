package com.example.android.popularmoviesstageone.task;

import android.content.Context;
import android.os.AsyncTask;

import com.example.android.popularmoviesstageone.models.VideoParcel;
import com.example.android.popularmoviesstageone.utilities.NetworkUtils;

import java.util.ArrayList;

/**
 * AsyncTask for fetching Video Data
 */

public class FetchVideosAsyncTask extends AsyncTask<String, Void, ArrayList<VideoParcel>> {

    private FetchVideosListener mFetchVideosListener;
    private Context mContext;

    public FetchVideosAsyncTask(Context context, FetchVideosListener fetchVideosListener) {
        mContext = context;
        mFetchVideosListener = fetchVideosListener;
    }

    @Override
    protected void onPreExecute() {
        mFetchVideosListener.onTaskPreExecute();
        super.onPreExecute();
    }

    @Override
    protected ArrayList<VideoParcel> doInBackground(String... movieIds) {

        ArrayList<VideoParcel> videoList = null;
        // Don't perform the request if there are no sort types.
        if (movieIds.length < 1 || movieIds[0] == null) {
            return null;
        }

        // Fetch the Video Details from the Network
        videoList = NetworkUtils.fetchVideoData(movieIds[0]);
        return videoList;
    }

    @Override
    protected void onPostExecute(ArrayList<VideoParcel> videoParcelArrayList) {
        mFetchVideosListener.onTaskPostExecute(videoParcelArrayList);
    }
}
