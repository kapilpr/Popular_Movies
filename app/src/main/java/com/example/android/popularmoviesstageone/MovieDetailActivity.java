package com.example.android.popularmoviesstageone;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmoviesstageone.adapters.ReviewAdapter;
import com.example.android.popularmoviesstageone.adapters.VideoAdapter;
import com.example.android.popularmoviesstageone.models.MovieParcel;
import com.example.android.popularmoviesstageone.models.ReviewParcel;
import com.example.android.popularmoviesstageone.models.VideoParcel;
import com.example.android.popularmoviesstageone.task.FavoriteButtonDisplayAsyncTask;
import com.example.android.popularmoviesstageone.task.FavoriteButtonDisplayListener;
import com.example.android.popularmoviesstageone.task.FavoriteButtonToggleAsyncTask;
import com.example.android.popularmoviesstageone.task.FavoriteButtonToggleListener;
import com.example.android.popularmoviesstageone.task.FetchReviewsAsyncTask;
import com.example.android.popularmoviesstageone.task.FetchReviewsListener;
import com.example.android.popularmoviesstageone.task.FetchVideosAsyncTask;
import com.example.android.popularmoviesstageone.task.FetchVideosListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieDetailActivity extends AppCompatActivity implements VideoAdapter.VideoAdapterOnClickHandler, ReviewAdapter.ReviewAdapterOnClickHandler {

    private static final String LOG_TAG = MovieDetailActivity.class.getSimpleName();

    private TextView mTitle;
    private TextView mRating;
    private TextView mReleaseDate;
    private TextView mOverview;
    private ImageView mBackDrop;
    private ImageView mPoster;
    private Button mFavoriteButton;

    private RecyclerView mVideoRecyclerView;
    private RecyclerView mReviewRecyclerView;

    private VideoAdapter mVideoAdapter;
    private ReviewAdapter mReviewAdapter;

    private ProgressBar mVideoProgressBar;
    private ProgressBar mReviewProgressBar;

    private TextView mNoVideoTextView;
    private TextView mNoReviewTextView;

    private MovieParcel mMovieParcel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mTitle = (TextView) findViewById(R.id.tv_movie_detail_title);
        mRating = (TextView) findViewById(R.id.tv_movie_detail_rating);
        mReleaseDate = (TextView) findViewById(R.id.tv_movie_detail_release_date);
        mOverview = (TextView) findViewById(R.id.tv_movie_overview);
        mBackDrop = (ImageView) findViewById(R.id.iv_movie_detail_backdrop);
        mFavoriteButton = (Button) findViewById(R.id.button_add_to_favorite);

        mVideoRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_video);
        mReviewRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_review);

        mVideoProgressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator_videos);
        mReviewProgressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator_reviews);

        mNoVideoTextView = (TextView) findViewById(R.id.tv_no_trailers_available);
        mNoReviewTextView = (TextView) findViewById(R.id.tv_no_reviews_available);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("movie")) {
                mMovieParcel = intentThatStartedThisActivity.getParcelableExtra("movie");

                final String moviesDbBaseUrl = "http://image.tmdb.org/t/p/w342/";
                // Using Picasso library to download and view the movie poster
                // on movie details activity
                if (mMovieParcel.getBackdrop() != null) {
                    Picasso.with(this)
                            .load(moviesDbBaseUrl + mMovieParcel.getBackdrop())
                            .error(R.drawable.tmdb)
                            .into(mBackDrop);
                }

                mTitle.setText(mMovieParcel.getTitle());
                mRating.setText(mMovieParcel.getVotes());
                mReleaseDate.setText(mMovieParcel.getReleaseDate());
                mOverview.setText(mMovieParcel.getOverview());

                // Set the text view of the favorite button dynamically
                displayFavoriteButton(mMovieParcel);

                // Set onClickListener on Favorite Button
                mFavoriteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            toggleFavoriteButton(mMovieParcel);
                        } catch (Exception e) {
                            Log.e(LOG_TAG, e.getMessage());
                        }
                    }
                });

                //Load the Videos RecyclerView
                LinearLayoutManager videosLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                mVideoRecyclerView.setLayoutManager(videosLayoutManager);
                mVideoRecyclerView.setHasFixedSize(true);

                mVideoAdapter = new VideoAdapter(this, this);
                mVideoRecyclerView.setAdapter(mVideoAdapter);

                //Load the Video Details for the movie
                loadVideos(mMovieParcel.getMovieId());

                //Load the Reviews RecyclerView
                LinearLayoutManager reviewsLayoutManager = new LinearLayoutManager(this);
                mReviewRecyclerView.setLayoutManager(reviewsLayoutManager);
                mReviewRecyclerView.setHasFixedSize(true);

                mReviewAdapter = new ReviewAdapter(this, this);
                mReviewRecyclerView.setAdapter(mReviewAdapter);

                //Load the Video Details for the movie
                loadReviews(mMovieParcel.getMovieId());
            }
        }
    }

    private void loadVideos(String movieId) {

        new FetchVideosAsyncTask(this, new FetchVideosListener() {
            @Override
            public void onTaskPreExecute() {
                mVideoProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTaskPostExecute(ArrayList<VideoParcel> videoParcelArrayList) {
                mVideoProgressBar.setVisibility(View.INVISIBLE);
                if (!videoParcelArrayList.isEmpty()) {
                    showVideosList();
                    mVideoAdapter.setVideoData(videoParcelArrayList);
                } else showNoVideosMessage();

            }
        }).execute(movieId);
    }

    private void showVideosList() {
        mNoVideoTextView.setVisibility(View.INVISIBLE);
        mVideoRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showNoVideosMessage() {
        mVideoRecyclerView.setVisibility(View.INVISIBLE);
        mNoVideoTextView.setVisibility(View.VISIBLE);
    }

    private void loadReviews(String movieId) {

        new FetchReviewsAsyncTask(this, new FetchReviewsListener() {
            @Override
            public void onTaskPreExecute() {
                mReviewProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTaskPostExecute(ArrayList<ReviewParcel> reviewParcelArrayList) {
                mReviewProgressBar.setVisibility(View.INVISIBLE);
                if (!reviewParcelArrayList.isEmpty()) {
                    showReviewsList();
                    mReviewAdapter.setReviewData(reviewParcelArrayList);
                } else showNoReviewsMessage();
            }
        }).execute(movieId);
    }

    private void showNoReviewsMessage() {
        mReviewRecyclerView.setVisibility(View.INVISIBLE);
        mNoReviewTextView.setVisibility(View.VISIBLE);
    }

    private void showReviewsList() {
        mNoReviewTextView.setVisibility(View.INVISIBLE);
        mReviewRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleFavoriteButton(MovieParcel movieParcel) {

        if (mFavoriteButton.getText().toString().equals(getString(R.string.action_add_favorite))) {
            mFavoriteButton.setText(getString(R.string.action_remove_favorite));
        } else {
            mFavoriteButton.setText(getString(R.string.action_add_favorite));
        }
        new FavoriteButtonToggleAsyncTask(this, new FavoriteButtonToggleListener() {
            @Override
            public void onTaskPostExecute() {
            }
        }).execute(movieParcel);
    }

    private void displayFavoriteButton(MovieParcel movieParcel) {
        new FavoriteButtonDisplayAsyncTask(this, new FavoriteButtonDisplayListener() {
            @Override
            public void onTaskPostExecute(Boolean isFavorite) {
                if (isFavorite) {
                    mFavoriteButton.setText(getApplicationContext().getString(R.string.action_remove_favorite));
                } else {
                    mFavoriteButton.setText(getApplicationContext().getString(R.string.action_add_favorite));
                }
            }
        }).execute(movieParcel);
    }

    @Override
    public void onClick(VideoParcel video) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + video.getKey()));
        startActivity(intent);

    }

    @Override
    public void onClick(ReviewParcel review) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(review.getUrl()));
        startActivity(intent);
    }
}
