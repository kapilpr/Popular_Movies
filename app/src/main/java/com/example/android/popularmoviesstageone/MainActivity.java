package com.example.android.popularmoviesstageone;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmoviesstageone.adapters.MovieAdapter;
import com.example.android.popularmoviesstageone.models.MovieParcel;
import com.example.android.popularmoviesstageone.task.MovieListAsyncTask;
import com.example.android.popularmoviesstageone.task.MovieListTaskListener;

import java.util.ArrayList;

import static com.example.android.popularmoviesstageone.R.menu.sort;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String SAVE_LAYOUT_MANAGER_KEY = "save_layout_manager";
    private static final String MOST_POPULAR = "popular";
    private static final String HIGHEST_RATED = "top_rated";
    private static final String MY_FAVORITES = "my_favorites";
    private static final String MOVIE_SORT_KEY = "sort_string";

    private String mSortString;
    private Parcelable mLayoutManagerSavedState;
    private RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private TextView mNoMovieMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private MovieAdapter mMovieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* This TextView is used to display errors and will be hidden if there are no errors */
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        /* This TextView is used to display when no movies are available to be displayed*/
        mNoMovieMessageDisplay = (TextView) findViewById(R.id.tv_no_movie_message_display);

        // Get the reference to the RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movie);

        // Set up the grid layout with 2 columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numberOfColumns());

        mRecyclerView.setLayoutManager(gridLayoutManager);

        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this, this);

        mRecyclerView.setAdapter(mMovieAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        if (savedInstanceState != null) {
            mSortString = savedInstanceState.getString(MOVIE_SORT_KEY);

        } else {
            mSortString = MOST_POPULAR;
        }

        loadMovieData(mSortString);
    }

    @Override
    protected void onStart() {
        mLayoutManagerSavedState = null;
        loadMovieData(mSortString);
        super.onStart();
    }

    // Method to calculate the number of columns dynamically
    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // Change this divider to adjust the size of the poster
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }

    private void loadMovieData(String sortType) {
        mSortString = sortType;
        // Check for network connection
        if (isNetworkAvailable(getApplicationContext())) {
            showMovieDataView();
            new MovieListAsyncTask(this, new MovieListTaskListener() {
                @Override
                public void onTaskPreExecute() {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                }

                @Override
                public void onTaskPostExecute(ArrayList<MovieParcel> movieParcelArrayList) {
                    mLoadingIndicator.setVisibility(View.INVISIBLE);
                    mNoMovieMessageDisplay.setVisibility(View.INVISIBLE);
                    if (!movieParcelArrayList.isEmpty()) {
                        showMovieDataView();
                        mMovieAdapter.setMovieData(movieParcelArrayList);

                        // Restore the Layout Manager saved state
                        if (mLayoutManagerSavedState != null) {
                            mRecyclerView.getLayoutManager().onRestoreInstanceState(mLayoutManagerSavedState);
                        }

                    } else {
                        showNoMovieMessage();
                    }
                }
            }).execute(mSortString);
        } else {
            showErrorMessage();
        }
    }

    /**
     * Returns true if network is available or about to become available
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void showMovieDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void showNoMovieMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mNoMovieMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(MOVIE_SORT_KEY, mSortString);
        outState.putParcelable(SAVE_LAYOUT_MANAGER_KEY, mRecyclerView.getLayoutManager().onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mSortString = savedInstanceState.getString(MOVIE_SORT_KEY);
        mLayoutManagerSavedState = savedInstanceState.getParcelable(SAVE_LAYOUT_MANAGER_KEY);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(sort, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mLayoutManagerSavedState = null;
        mMovieAdapter.setMovieData(null);

        int id = item.getItemId();
        switch (id) {
            case R.id.action_sort_popular:
                loadMovieData(MOST_POPULAR);
                return true;

            case R.id.action_sort_rating:
                loadMovieData(HIGHEST_RATED);
                return true;

            case R.id.action_sort_favorites:
                loadMovieData(MY_FAVORITES);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // onClick opens the MovieDetail Activity
    @Override
    public void onClick(MovieParcel movie) {
        Intent intentToStartMovieDetailActivity = new Intent(MainActivity.this, MovieDetailActivity.class);
        intentToStartMovieDetailActivity.putExtra("movie", movie);
        startActivity(intentToStartMovieDetailActivity);
    }
}
