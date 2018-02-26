package com.example.android.popularmoviesstageone.task;

import android.content.Context;
import android.os.AsyncTask;

import com.example.android.popularmoviesstageone.models.ReviewParcel;
import com.example.android.popularmoviesstageone.utilities.NetworkUtils;

import java.util.ArrayList;

/**
 * AsyncTask to fetch movie reviews
 */

public class FetchReviewsAsyncTask extends AsyncTask<String, Void, ArrayList<ReviewParcel>> {

    private FetchReviewsListener mFetchReviewsListener;
    private Context mContext;

    public FetchReviewsAsyncTask(Context context, FetchReviewsListener fetchReviewsListener) {
        mContext = context;
        mFetchReviewsListener = fetchReviewsListener;
    }

    @Override
    protected void onPreExecute() {
        mFetchReviewsListener.onTaskPreExecute();
        super.onPreExecute();
    }

    @Override
    protected ArrayList<ReviewParcel> doInBackground(String... movieIds) {

        ArrayList<ReviewParcel> reviewList = null;
        // Don't perform the request if there are no sort types.
        if (movieIds.length < 1 || movieIds[0] == null) {
            return null;
        }

        // Fetch the Review Details from the Network
        reviewList = NetworkUtils.fetchReviewData(movieIds[0]);
        return reviewList;
    }

    @Override
    protected void onPostExecute(ArrayList<ReviewParcel> reviewParcelArrayList) {
        mFetchReviewsListener.onTaskPostExecute(reviewParcelArrayList);
    }
}
