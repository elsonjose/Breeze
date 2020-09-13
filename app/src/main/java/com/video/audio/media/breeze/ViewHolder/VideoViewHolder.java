package com.video.audio.media.breeze.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.video.audio.media.breeze.R;

public class VideoViewHolder extends RecyclerView.ViewHolder {

    public TextView videoNameTextView,videoSizeTextView,videoLengthTextView;
    public ImageView videoImageView;

    public VideoViewHolder(@NonNull View itemView) {
        super(itemView);

        videoImageView = itemView.findViewById(R.id.video_imageview);
        videoNameTextView = itemView.findViewById(R.id.video_name_textview);
        videoSizeTextView = itemView.findViewById(R.id.video_size_textview);
        videoLengthTextView = itemView.findViewById(R.id.video_length_textview);

    }
}
