package com.example.android.popularmoviesstageone.task;

import android.content.Context;
import android.os.AsyncTask;

import com.example.android.popularmoviesstageone.models.MovieParcel;
import com.example.android.popularmoviesstageone.utilities.FavoriteDataUtils;
import com.example.android.popularmoviesstageone.utilities.NetworkUtils;

import java.util.ArrayList;

/**
 * AsyncTask for fetching movie list for MainActivity
 */

public class MovieListAsyncTask extends AsyncTask<String, Void, ArrayList<MovieParcel>> {

    private static final String MY_FAVORITES = "my_favorites";
    private MovieListTaskListener mMovieListTaskListener;
    private Context mContext;

    public MovieListAsyncTask(Context context, MovieListTaskListener movieListTaskListener) {
        mContext = context;
        mMovieListTaskListener = movieListTaskListener;
    }

    @Override
    protected void onPreExecute() {
        mMovieListTaskListener.onTaskPreExecute();
        super.onPreExecute();
    }

    @Override
    protected ArrayList<MovieParcel> doInBackground(String... sortStrings) {

        ArrayList<MovieParcel> movieList = null;
        // Don't perform the request if there are no sort types.
        if (sortStrings.length < 1 || sortStrings[0] == null) {
            return null;
        }
        // Do a network fetch AsyncTask if the sort type is not favorite movies
        if (!sortStrings[0].equals(MY_FAVORITES)) {
            movieList = NetworkUtils.fetchMovieData(sortStrings[0]);
        }
        // Fetch the Favorite movies from the database
        else {
            FavoriteDataUtils favoriteDataUtils = new FavoriteDataUtils(mContext);
            movieList = favoriteDataUtils.createMovieListFromCursor();
        }
        return movieList;
    }

    @Override
    protected void onPostExecute(ArrayList<MovieParcel> movieParcelArrayList) {
        mMovieListTaskListener.onTaskPostExecute(movieParcelArrayList);
    }
}
