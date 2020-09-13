package com.video.audio.media.breeze.Util;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ScrollListener extends RecyclerView.OnScrollListener {

    private static final float MIN_SCROLLED_DISTANCE=25;
    int scrollDistanceDy=0;
    boolean isVisible = true;

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if(isVisible && scrollDistanceDy>MIN_SCROLLED_DISTANCE)
        {
            onHide();
            scrollDistanceDy=0;
            isVisible=false;
        }
        else if(!isVisible && scrollDistanceDy<(-MIN_SCROLLED_DISTANCE))
        {
            onShow();
            scrollDistanceDy=0;
            isVisible=true;
        }
        else if(dy>MIN_SCROLLED_DISTANCE)
        {
            onLoadMore();
        }

        if((isVisible && dy>0) || (!isVisible &&  dy<0))
        {
            scrollDistanceDy+=dy;
        }
    }

    public void onLoadMore()
    {

    }

    public void onHide()
    {

    }

    public void onShow()
    {

    }
}
