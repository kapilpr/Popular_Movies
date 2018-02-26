package com.example.android.popularmoviesstageone.task;

import com.example.android.popularmoviesstageone.models.MovieParcel;

import java.util.ArrayList;

// Interface callback for MainActivity's movie list fetching
public interface MovieListTaskListener {

    public void onTaskPreExecute();

    public void onTaskPostExecute(ArrayList<MovieParcel> movieParcelArrayList);
}
