package com.example.android.popularmoviesstageone.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.popularmoviesstageone.data.MovieContract.MovieEntry;

/**
 * Database helper for Popular Movies app. Manages database creation and version management.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movies.db";

    public static final int DATABASE_VERSION = 1;

    //Constructor is called when the database is called for the first time
    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Create a String that contains the SQL statement to create the Movies table
        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " ("
                + MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, "
                + MovieEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL, "
                + MovieEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL, "
                + MovieEntry.COLUMN_MOVIE_VOTES + " TEXT NOT NULL, "
                + MovieEntry.COLUMN_MOVIE_POSTER + " TEXT NOT NULL, "
                + MovieEntry.COLUMN_MOVIE_BACKDROP + " TEXT NOT NULL, "
                + MovieEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL);";

        //Execute the SQL statement
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
