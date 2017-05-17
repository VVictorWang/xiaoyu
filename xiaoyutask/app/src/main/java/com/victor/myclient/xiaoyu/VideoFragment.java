package com.victor.myclient.xiaoyu;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.ainemo.sdk.otf.NemoSDK;
import com.ainemo.sdk.otf.NemoSDKListener;
import com.ainemo.sdk.otf.VideoInfo;
import demo.animen.com.xiaoyutask.R;
import java.util.List;

/**
 * Created by 小武哥 on 2017/4/29.
 */
public class VideoFragment extends Fragment implements BusinessActivity.CallListener {

  private static final String TAG = VideoFragment.class.getSimpleName();
  private SimpleVideoView mVideoView;
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
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
  }

  public void releaseResource() {
    mVideoView.destroy();
  }

  @Override
  public void onNewContentReceive(Bitmap bitmap) {
    mContent.setImageBitmap(bitmap);
  }
}

