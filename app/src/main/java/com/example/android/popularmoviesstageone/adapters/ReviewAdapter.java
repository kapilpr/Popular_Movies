package com.example.android.popularmoviesstageone.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmoviesstageone.R;
import com.example.android.popularmoviesstageone.models.ReviewParcel;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    public interface ReviewAdapterOnClickHandler {
        void onClick(ReviewParcel review);
    }

    private static final String LOG_TAG = ReviewAdapter.class.getSimpleName();
    private ArrayList<ReviewParcel> mReviewParcelArrayList;
    private Context mContext;
    private final ReviewAdapterOnClickHandler mClickHandler;

    // ReviewAdapter Constructor
    public ReviewAdapter(Context context, ReviewAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the view and pass it to the ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list_item, parent, false);
        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder holder, int position) {

        ReviewParcel reviewParcel = mReviewParcelArrayList.get(position);
        holder.bind(reviewParcel);
    }

    @Override
    public int getItemCount() {
        if (null == mReviewParcelArrayList) return 0;
        return mReviewParcelArrayList.size();
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected TextView mAuthorTextView;
        protected TextView mContentTextView;

        public ReviewAdapterViewHolder(View itemView) {
            super(itemView);
            mAuthorTextView = (TextView) itemView.findViewById(R.id.tv_review_author);
            mContentTextView = (TextView) itemView.findViewById(R.id.tv_review_content);
            itemView.setOnClickListener(this);
        }

        public void bind(ReviewParcel reviewParcel) {
            mAuthorTextView.setText(reviewParcel.getAuthor());
            mContentTextView.setText(reviewParcel.getContent());
        }

        @Override
        public void onClick(View view) {
            //Get the movie object from the clicked position
            int adapterPosition = getAdapterPosition();
            ReviewParcel reviewParcel = mReviewParcelArrayList.get(adapterPosition);
            mClickHandler.onClick(reviewParcel);
        }
    }

    // This method is used to set the Review Data on the Review Adapter if we have already created one
    // and need not create a new instance of Review Adapter to display it.
    public void setReviewData(ArrayList<ReviewParcel> reviewData) {
        mReviewParcelArrayList = reviewData;
        notifyDataSetChanged();
    }
}
