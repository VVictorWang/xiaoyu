package com.ainemo.pad;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.ainemo.sdk.otf.NemoSDK;
import com.ainemo.sdk.otf.NemoSDKListener;
import com.ainemo.sdk.otf.VideoInfo;
import java.util.List;

public class VideoFragment extends Fragment implements BusinessActivity.CallListener {

  private static final String TAG = VideoFragment.class.getSimpleName();
  private SimpleVideoView mVideoView;
  private List<VideoCellView> mVideoViews;
  private ImageView mContent;
  private boolean foregroundCamera = true;
  private boolean micMute = false;
  private boolean audioMode = false;
//    private View incomingCallView;

//    public void notifyIncomingCall(final int callIndex, String callerNumber, String callerName) {
//        incomingCallView.setVisibility(View.VISIBLE);
//        ((TextView) incomingCallView.findViewById(R.id.caller_name)).setText(callerName);
//        ((TextView) incomingCallView.findViewById(R.id.caller_number)).setText(callerNumber);
//        incomingCallView.findViewById(R.id.accept).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                NemoSDK.getInstance().answerCall(callIndex, true);
//                incomingCallView.setVisibility(View.GONE);
//            }
//        });
//        incomingCallView.findViewById(R.id.refuse).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                NemoSDK.getInstance().answerCall(callIndex, false);
//                incomingCallView.setVisibility(View.GONE);
//            }
//        });
//    }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.call_fragment_layout, container, false);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
//        incomingCallView = view.findViewById(R.id.in_coming_call);
    mVideoView = (SimpleVideoView) view.findViewById(R.id.remote_video_view);

    mContent = (ImageView) view.findViewById(R.id.shared_content);
    view.findViewById(R.id.switch_camera).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        foregroundCamera = !foregroundCamera;
        NemoSDK.getInstance().switchCamera(foregroundCamera);
      }
    });
    view.findViewById(R.id.switch_mic_mute).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        micMute = !micMute;
        NemoSDK.getInstance().enableMic(micMute);
      }
    });

    view.findViewById(R.id.switch_call_module).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        audioMode = !audioMode;
        NemoSDK.getInstance().switchCallMode(audioMode);
      }
    });

    view.findViewById(R.id.drop_call).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        NemoSDK.getInstance().hangup();
      }
    });
    super.onViewCreated(view, savedInstanceState);
  }

  @Override
  public void onResume() {
    super.onResume();
  }

  @Override
  public void onContentStateChanged(NemoSDKListener.ContentState contentState) {
    if (NemoSDKListener.ContentState.ON_START == contentState) {
      mContent.setVisibility(View.VISIBLE);
    } else {
      mContent.setVisibility(View.GONE);
    }
  }

  @Override
  public void onStart() {
    mVideoView.requestLocalFrame();

    super.onStart();
  }

  @Override
  public void onHiddenChanged(boolean hidden) {
    super.onHiddenChanged(hidden);
    if (null == mVideoView) {
      return;
    }

    if (!hidden) {
      mVideoView.requestLocalFrame();
    } else {
      mVideoView.stopLocalFrameRender();
    }
  }


  @Override
  public void onVideoDataSourceChange(List<VideoInfo> videoInfos) {
    if (videoInfos.size() > 0) {
      mVideoView.setLayoutInfos(videoInfos);
    } else {
      mVideoView.stopRender();
    }
    mVideoViews = mVideoView.getmVideoViews();
    Log.d(TAG, "onVideoDataSourceChange: mVideoView "+mVideoView);
    Log.d(TAG, "onVideoDataSourceChange: mVideoViews "+mVideoViews);
    Log.d(TAG, "onViewCreated ONCOntent: size " + mVideoViews.size());
    mVideoView.getLocalVideoView().setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        mVideoView.indexTag=0;
        mVideoView.requestLayout();
        Log.d(TAG, "onView click localView");
      }
    });
    for (int i = 0; i < mVideoViews.size(); i++) {
      mVideoViews.get(i).setTag(i+1);
      mVideoViews.get(i).setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
//          VideoCellView videoCellView = (VideoCellView) v;
//          Log.d(TAG, "onView onClick: v "+v);
//          VideoCellView temp;
//          temp = mVideoViews.get(0);
//          Log.d(TAG, "onView onClick: temp"+temp);
//          mVideoViews.remove(videoCellView);
//          mVideoViews.set(0, videoCellView);
//          mVideoViews.add(temp);
////          mVideoView.requestLayout();
//          Log.d(TAG, "onClick: " + videoCellView);
          mVideoView.indexTag=(Integer) v.getTag();
          Log.d(TAG, "onView tag"+v.getTag());
          mVideoView.requestLayout();
        }
      });
      Log.d(TAG, "onViewCreated onCOntent: clickListener add");
    }
  }

  public void releaseResource() {
    mVideoView.destroy();
  }

  @Override
  public void onNewContentReceive(Bitmap bitmap) {
    mContent.setImageBitmap(bitmap);
  }
}
