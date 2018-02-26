package com.example.android.popularmoviesstageone.utilities;

import android.content.Context;
import android.database.Cursor;

import com.example.android.popularmoviesstageone.data.MovieContract;
import com.example.android.popularmoviesstageone.models.MovieParcel;

import java.util.ArrayList;

/**
 * Utility Class for managing favorite movies database
 */

public class FavoriteDataUtils {

    private Context mContext;

    public FavoriteDataUtils(Context context) {
        mContext = context;
    }

    public ArrayList<MovieParcel> createMovieListFromCursor() {

        ArrayList<MovieParcel> movieParcelArrayList = new ArrayList<>();

        Cursor cursor = mContext.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

        if (cursor != null) {
            int titleColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE);
            int overviewColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW);
            int releaseDateColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE);
            int votesColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_VOTES);
            int posterColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER);
            int backdropColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP);
            int movieIdColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
            try {
                while (cursor.moveToNext()) {
                    MovieParcel movieParcel = new MovieParcel(
                            cursor.getString(titleColumnIndex),
                            cursor.getString(overviewColumnIndex),
                            cursor.getString(releaseDateColumnIndex),
                            cursor.getString(votesColumnIndex),
                            cursor.getString(posterColumnIndex),
                            cursor.getString(backdropColumnIndex),
                            cursor.getString(movieIdColumnIndex));

                    movieParcelArrayList.add(movieParcel);
                }
            } finally {
                cursor.close();
            }
        }
        return movieParcelArrayList;
    }
}
