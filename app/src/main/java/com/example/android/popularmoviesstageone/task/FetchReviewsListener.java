package com.example.android.popularmoviesstageone.task;

import com.example.android.popularmoviesstageone.models.ReviewParcel;

import java.util.ArrayList;

/**
 * Listener for Fetch Reviews AsyncTask
 */
public interface FetchReviewsListener {
    void onTaskPreExecute();

    void onTaskPostExecute(ArrayList<ReviewParcel> reviewParcelArrayList);
}
