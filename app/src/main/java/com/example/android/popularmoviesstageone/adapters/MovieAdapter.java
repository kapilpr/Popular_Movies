package com.example.android.popularmoviesstageone.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmoviesstageone.R;
import com.example.android.popularmoviesstageone.models.MovieParcel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    public interface MovieAdapterOnClickHandler {
        void onClick(MovieParcel movie);
    }

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();
    private ArrayList<MovieParcel> mMovieParcelArrayList;
    private Context mContext;
    private final MovieAdapterOnClickHandler mClickHandler;

    // MovieAdapter Constructor
    public MovieAdapter(Context context, MovieAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the view and pass it to the ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {

        // MoviesDB Url
        String moviesDbBaseUrl = "http://image.tmdb.org/t/p/w185/";
        MovieParcel movieParcel = mMovieParcelArrayList.get(position);

        // Using Picasso library to download and view the movie poster
        if (movieParcel.getPoster() != null) {
            Picasso.with(mContext)
                    .load(moviesDbBaseUrl + movieParcel.getPoster())
                    .error(R.drawable.tmdb)
                    .into(holder.mImageView);
        }
    }

    @Override
    public int getItemCount() {
        if (null == mMovieParcelArrayList) return 0;
        return mMovieParcelArrayList.size();
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected ImageView mImageView;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.iv_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //Get the movie object from the clicked position
            int adapterPosition = getAdapterPosition();
            MovieParcel movieParcel = mMovieParcelArrayList.get(adapterPosition);
            mClickHandler.onClick(movieParcel);
        }
    }

    // This method is used to set the Movie Data on the Movie Adapter if we have already created one
    // and need not create a new instance of Movie Adapter to display it.
    public void setMovieData(ArrayList<MovieParcel> movieData) {
        mMovieParcelArrayList = movieData;
        notifyDataSetChanged();
    }
}
