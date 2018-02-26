package com.example.android.popularmoviesstageone.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Contract Class for the Popular Movies App
 */

public class MovieContract {

    private MovieContract() {

    }

    public static final String CONTENT_AUTHORITY = "com.example.android.popularmoviesstageone";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "movies";

    public static final class MovieEntry implements BaseColumns {

        /**
         * The content URI to access the movie data in the provider
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MOVIES);

        public static final String TABLE_NAME = "movies";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_MOVIE_OVERVIEW = "overview";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "release_date";
        public static final String COLUMN_MOVIE_VOTES = "votes";
        public static final String COLUMN_MOVIE_POSTER = "poster";
        public static final String COLUMN_MOVIE_BACKDROP = "backdrop";
        public static final String COLUMN_MOVIE_ID = "id";
    }

}
