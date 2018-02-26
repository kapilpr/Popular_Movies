package com.example.android.popularmoviesstageone.task;

import com.example.android.popularmoviesstageone.models.VideoParcel;

import java.util.ArrayList;

/**
 * Listener for Fetch Videos AsyncTask
 */
public interface FetchVideosListener {
    void onTaskPreExecute();

    void onTaskPostExecute(ArrayList<VideoParcel> videoParcelArrayList);
}
