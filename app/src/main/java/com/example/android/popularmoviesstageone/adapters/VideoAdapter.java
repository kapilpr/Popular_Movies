package com.example.android.popularmoviesstageone.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesstageone.R;
import com.example.android.popularmoviesstageone.models.VideoParcel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoAdapterViewHolder> {

    public interface VideoAdapterOnClickHandler {
        void onClick(VideoParcel video);
    }

    private static final String LOG_TAG = VideoAdapter.class.getSimpleName();
    private ArrayList<VideoParcel> mVideoParcelArrayList;
    private Context mContext;
    private final VideoAdapterOnClickHandler mClickHandler;

    // VideoAdapter Constructor
    public VideoAdapter(Context context, VideoAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @Override
    public VideoAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the view and pass it to the ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_list_item, parent, false);
        return new VideoAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoAdapterViewHolder holder, int position) {

        VideoParcel videoParcel = mVideoParcelArrayList.get(position);
        // Using Picasso library to download and view the video thumbnail
            // YouTube Thumbnail Url
            String youtubeImageUrl = "http://img.youtube.com/vi/" + videoParcel.getKey() + "/0.jpg";
            Picasso.with(mContext)
                    .load(youtubeImageUrl)
                    .error(R.drawable.tmdb)
                    .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        if (null == mVideoParcelArrayList) return 0;
        return mVideoParcelArrayList.size();
    }

    public class VideoAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected ImageView mImageView;
        protected TextView mTextView;

        public VideoAdapterViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.iv_video_thumbnail);
            //mTextView = (TextView) itemView.findViewById(R.id.tv_video_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //Get the movie object from the clicked position
            int adapterPosition = getAdapterPosition();
            VideoParcel videoParcel = mVideoParcelArrayList.get(adapterPosition);
            mClickHandler.onClick(videoParcel);
        }
    }

    // This method is used to set the Video Data on the Video Adapter if we have already created one
    // and need not create a new instance of Video Adapter to display it.
    public void setVideoData(ArrayList<VideoParcel> videoData) {
        mVideoParcelArrayList = videoData;
        notifyDataSetChanged();
    }
}
