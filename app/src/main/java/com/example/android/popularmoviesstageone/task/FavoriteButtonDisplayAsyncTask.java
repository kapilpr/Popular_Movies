package com.example.android.popularmoviesstageone.task;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.example.android.popularmoviesstageone.data.MovieContract;
import com.example.android.popularmoviesstageone.models.MovieParcel;

/**
 * AsyncTask querying favorite movies in the database
 */

public class FavoriteButtonDisplayAsyncTask extends AsyncTask<MovieParcel, Void, Boolean> {

    private static final String LOG_TAG = FavoriteButtonDisplayAsyncTask.class.getSimpleName();
    private Context mContext;
    private FavoriteButtonDisplayListener mFavoriteButtonDisplayListener;

    public FavoriteButtonDisplayAsyncTask(Context context, FavoriteButtonDisplayListener favoriteButtonDisplayListener) {
        mContext = context;
        mFavoriteButtonDisplayListener = favoriteButtonDisplayListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(MovieParcel... movieParcels) {
        if (movieParcels.length == 0) {
            return null;
        }
        MovieParcel currentMovieParcel = movieParcels[0];
        Boolean isFavorite = false;
        Cursor cursor = null;

        //Query the database for current movie.
        try {
            cursor = mContext.getContentResolver().query(
                    MovieContract.MovieEntry.CONTENT_URI,
                    null,
                    MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=" + currentMovieParcel.getMovieId(),
                    null,
                    null);

            if (cursor.moveToNext()) {
                isFavorite = true;
            }

        } finally {
            cursor.close();
        }
        return isFavorite;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        mFavoriteButtonDisplayListener.onTaskPostExecute(result);
    }
}
