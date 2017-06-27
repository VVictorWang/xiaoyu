package com.victor.myclient.view;
import android.content.Context;

import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.ainemo.sdk.otf.NemoSDK;
import com.ainemo.sdk.otf.OpenGLTextureView;
import com.ainemo.sdk.otf.VideoInfo;

import java.util.ArrayList;
import java.util.List;


public class SimpleVideoView extends ViewGroup {


    private static final String TAG = "SimpleVideoView";


    public int indexTag = 0;
    public static final int LOCAL_VIEW_ID = 99;
    private OpenGLTextureView localVideoView;

    public List<VideoCellView> getmVideoViews() {
        return mVideoViews;
    }

    public void setmVideoViews(List<VideoCellView> mVideoViews) {
        this.mVideoViews = mVideoViews;
    }

    private List<VideoCellView> mVideoViews = new ArrayList<>();
    private Handler handler = new Handler();
    private Runnable drawVideoFrameRunnable = new Runnable() {
        @Override
        public void run() {
            for (VideoCellView videoCellView : mVideoViews) {
                videoCellView.requestRender();
            }
            requestRender();
        }
    };

    private Runnable drawLocalVideoFrameRunnable = new Runnable() {
        @Override
        public void run() {
            localVideoView.requestRender();
            requestLocalVideoRender();
        }
    };

    public SimpleVideoView(Context context) {
        super(context);
        init();
    }

    public SimpleVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OpenGLTextureView getLocalVideoView() {
        return localVideoView;
    }

    private void init() {
        localVideoView = new OpenGLTextureView(getContext());
        localVideoView.setSourceID(NemoSDK.getInstance().getLocalVideoStreamID());
        localVideoView.setContent(false);
        addView(localVideoView);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//      mVideoViews.add((VideoCellView) localVideoView);
        int size = Math.min(mVideoViews.size(), 5); //最多取5个view显示
        Log.i(TAG, "layout cell count " + size);
        Log.d(TAG, "onLayout: mVideoViews " + mVideoViews);
        if (indexTag == 0) {
            localVideoView.layout(l, t, r, b);
            localVideoView.bringToFront();
        } else {
            mVideoViews.get(indexTag-1).layout(l, t, r, b);
            mVideoViews.get(indexTag-1).bringToFront();
            localVideoView.layout(l, (b * 3 / 4 - t / 4), (r + l) / 5, b);
            localVideoView.bringToFront();
        }

        for (int i = 0; i < size; i++) {
            if (i > indexTag - 1||indexTag==0) {
                Log.i(TAG, "layout item at " + i);
                mVideoViews.get(i).layout(l + 20 + (r - l) / 5 * (i), (b * 3 / 4 - t / 4),
                        l + 20 + (r - l) / 5 * (i + 1), b)
                ;
                mVideoViews.get(i).bringToFront();
            }else if(i!=indexTag-1){
                mVideoViews.get(i).layout(l + 20 + (r - l) / 5 * (i+1), (b * 3 / 4 - t / 4),
                        l + 20 + (r - l) / 5 * (i + 2), b)
                ;
                mVideoViews.get(i).bringToFront();
            }
            Log.i(TAG, "layout local item");
//        localVideoView.layout(l, (b * 3 / 4 - t / 4), (r + l) / 5, b);

        }
    }

    public void setLayoutInfos (List < VideoInfo > videoInfos) {

        Log.i(TAG, "video info size is " + videoInfos.size());

        List<VideoCellView> toDel = new ArrayList<>();

        Log.d(TAG, "setLayoutInfos: true");

        l:
        for (VideoCellView videoCellView : mVideoViews) {
            for (VideoInfo mVideoInfo : videoInfos) {
                if (mVideoInfo.getParticipantId() == videoCellView.getParticipantId()) {
                    videoCellView.setSourceID(mVideoInfo.getDataSourceID());
                    videoCellView.setContent(mVideoInfo.isContent());
                    continue l;
                }
            }
            toDel.add(videoCellView);
            removeView(videoCellView);
        }

        mVideoViews.removeAll(toDel);

        k:
        for (VideoInfo videoInfo : videoInfos) {
            for (VideoCellView videoCellView : mVideoViews) {
                if (videoCellView.getParticipantId() == videoInfo.getParticipantId()) {
                    continue k;
                }
            }
            VideoCellView toAdd = new VideoCellView(getContext());
            toAdd.setParticipantId(videoInfo.getParticipantId());
            toAdd.setSourceID(videoInfo.getDataSourceID());
            Log.i(TAG, "add view");
            mVideoViews.add(toAdd);
            addView(toAdd);
        }
        requestLayout();
        requestRender();
    }

    public void destroy () {
        destroyDrawingCache();
        mVideoViews.clear();
        handler.removeCallbacksAndMessages(null);
    }

    public void stopRender () {
        handler.removeCallbacks(drawVideoFrameRunnable);
    }

    private void requestRender () {
        handler.postDelayed(drawVideoFrameRunnable, 1000 / 15);
    }

    public void requestLocalFrame () {
        handler.removeCallbacks(drawLocalVideoFrameRunnable);
        requestLocalVideoRender();
    }

    public void stopLocalFrameRender () {
        handler.removeCallbacks(drawLocalVideoFrameRunnable);
    }

    private void requestLocalVideoRender () {
        handler.postDelayed(drawLocalVideoFrameRunnable, 1000 / 15);
    }

    @Override
    protected void onMeasure ( int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        for (int childIndex = 0; childIndex < getChildCount(); childIndex++) {
            View child = getChildAt(childIndex);
            child.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.UNSPECIFIED),
                    MeasureSpec.makeMeasureSpec(height, MeasureSpec.UNSPECIFIED));
        }
    }
}