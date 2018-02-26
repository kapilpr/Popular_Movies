package com.example.android.popularmoviesstageone.task;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.popularmoviesstageone.data.MovieContract;
import com.example.android.popularmoviesstageone.models.MovieParcel;

/**
 * AsyncTask to update the database of favorite movies when favorite button is clicked
 */

public class FavoriteButtonToggleAsyncTask extends AsyncTask<MovieParcel, Void, Void> {

    private static final String LOG_TAG = FavoriteButtonToggleAsyncTask.class.getSimpleName();
    private Context mContext;
    private FavoriteButtonToggleListener mFavoriteButtonToggleListener;

    public FavoriteButtonToggleAsyncTask(Context context, FavoriteButtonToggleListener favoriteButtonToggleListener) {
        mContext = context;
        mFavoriteButtonToggleListener = favoriteButtonToggleListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(MovieParcel... movieParcels) {
        if (movieParcels.length == 0) {
            return null;
        }
        MovieParcel currentMovieParcel = movieParcels[0];
        Cursor cursor = null;

        //Query the database for current movie.
        try {
            cursor = mContext.getContentResolver().query(
                    MovieContract.MovieEntry.CONTENT_URI,
                    null,
                    MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=" + currentMovieParcel.getMovieId(),
                    null,
                    null);

            // If the current movie is not a favorite, add it to the favorite database
            if (!cursor.moveToNext()) {
                ContentValues values = new ContentValues();
                values.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, currentMovieParcel.getTitle());
                values.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, currentMovieParcel.getOverview());
                values.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, currentMovieParcel.getReleaseDate());
                values.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTES, currentMovieParcel.getVotes());
                values.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER, currentMovieParcel.getPoster());
                values.put(MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP, currentMovieParcel.getBackdrop());
                values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, currentMovieParcel.getMovieId());

                Uri newUri = mContext.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, values);
                Log.v(LOG_TAG, "Inserted URI = " + newUri);

                // If the current movie is a favorite, remove it from the database
            } else {
                int rowsDeleted = mContext.getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=" + currentMovieParcel.getMovieId(), null);
                Log.v(LOG_TAG, "Deleted no of Rows = " + rowsDeleted);
            }

        } finally {
            cursor.close();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        mFavoriteButtonToggleListener.onTaskPostExecute();
    }
}


