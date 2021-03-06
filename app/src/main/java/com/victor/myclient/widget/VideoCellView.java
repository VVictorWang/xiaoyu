package com.victor.myclient.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.ainemo.sdk.otf.OpenGLTextureView;


public class VideoCellView extends OpenGLTextureView {
    private int participantId;

    public VideoCellView(Context context) {
        super(context);
    }

    public VideoCellView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public int getParticipantId() {
        return participantId;
    }

    public void setParticipantId(int participantId) {
        this.participantId = participantId;
    }
}
