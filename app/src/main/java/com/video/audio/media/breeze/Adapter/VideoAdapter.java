package com.video.audio.media.breeze.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.video.audio.media.breeze.Model.Video;
import com.video.audio.media.breeze.R;
import com.video.audio.media.breeze.Util.UriToFilePathConverter;
import com.video.audio.media.breeze.ViewHolder.VideoViewHolder;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoViewHolder> {

    Context context;
    List<Video> videoList;
    RequestOptions requestOptions;

    public VideoAdapter(Context context, List<Video> videoList) {
        this.context = context;
        this.videoList = videoList;
        requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VideoViewHolder(LayoutInflater.from(context).inflate(R.layout.video_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {

        Log.i("imageUri", "onBindViewHolder: "+new UriToFilePathConverter(context).getFilePath(videoList.get(position).getUri()));
        Glide.with(context).load(new UriToFilePathConverter(context).getFilePath(videoList.get(position).getUri())).apply(requestOptions).into(holder.videoImageView);
        holder.videoLengthTextView.setText(getDuration(videoList.get(position).getDuration()));
        holder.videoSizeTextView.setText(getSizeInMB(videoList.get(position).getSize()));
        holder.videoNameTextView.setText(videoList.get(position).getName());

        
    }


    @Override
    public int getItemCount() {
        return videoList.size();
    }

    private String getSizeInMB(int size)
    {
        float sizeInMB =  (float)(size)/(1024*1024);
        return String.format("%.2f",sizeInMB)+"MB";
    }
    private String getDuration(int duration) {

        int seconds = duration/1000;
        if(seconds>(60*60))
        {
            int hr=seconds/(60*60);
            int min = seconds%(60*60);
            return hr+"h "+min+"m";
        }
        else if(seconds>60)
        {
            int min=seconds/60;
            int sec=seconds%60;
            return min+"m "+sec+"s";
        }
        else
        {
            return seconds+"s";
        }
    }
}
