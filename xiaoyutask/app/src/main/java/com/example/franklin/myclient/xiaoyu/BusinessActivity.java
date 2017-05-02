package com.example.franklin.myclient.xiaoyu;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.log.L;
import android.os.Bundle;
import android.app.AlertDialog;
import android.util.Log;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import com.ainemo.sdk.otf.NemoSDK;
import com.ainemo.sdk.otf.NemoSDKErrorCode;
import com.ainemo.sdk.otf.NemoSDKListener;
import com.ainemo.sdk.otf.VideoInfo;
import demo.animen.com.xiaoyutask.R;
import java.util.List;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by 小武哥 on 2017/4/29.
 */

public class BusinessActivity extends Activity {

  private static final String TAG = "BusinessActivity";

  private DialFragment dialFragment;
  private VideoFragment videoFragment;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.business_activity);
    getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);
    dialFragment = new DialFragment();
    videoFragment = new VideoFragment();
    final FragmentManager manager = getFragmentManager();
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    manager.beginTransaction().add(R.id.content_frame,videoFragment).commitAllowingStateLoss();


    Intent intent = getIntent();
    boolean isIncomingCall = intent.getBooleanExtra("isIncomingCall", false);
    if (true == isIncomingCall) {
      final int callIndex = intent.getIntExtra("callIndex", -1);
      String callerName = intent.getStringExtra("callerName");
      String callerNumber = intent.getStringExtra("callerNumber");
      showIncomingCallDialog(callIndex, callerNumber, callerName);
    }
    NemoSDK.getInstance().setNemoSDKListener(new NemoSDKListener() {
      @Override
      public void onContentStateChanged(NemoSDKListener.ContentState contentState) {
        videoFragment.onContentStateChanged(contentState);
      }

      @Override
      public void onNewContentReceive(Bitmap bitmap) {
        videoFragment.onNewContentReceive(bitmap);
      }

      @Override
      public void onCallFailed(int i) {
        Observable.just(i)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<Integer>() {
              @Override
              public void call(Integer integer) {
                L.e(TAG, "error code is " + integer);
                if (NemoSDKErrorCode.WRONG_PASSWORD == integer) {
                  Toast.makeText(BusinessActivity.this, "wrong password", Toast.LENGTH_SHORT)
                      .show();
                } else if (NemoSDKErrorCode.INVALID_PARAM == integer) {
                  Toast.makeText(BusinessActivity.this, "wrong param", Toast.LENGTH_SHORT).show();
                } else if (NemoSDKErrorCode.NETWORK_UNAVAILABLE == integer) {
                  Toast.makeText(BusinessActivity.this, "net work unavailable", Toast.LENGTH_SHORT)
                      .show();
                } else if (NemoSDKErrorCode.HOST_ERROR == integer) {
                  Toast.makeText(BusinessActivity.this, "host error", Toast.LENGTH_SHORT).show();
                }
              }
            });
      }

      @Override
      public void onCallStateChange(final NemoSDKListener.CallState state,
          final NemoSDKListener.Reason reason) {
        Observable.just(state)
            .subscribeOn(Schedulers.immediate())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<CallState>() {
              @Override
              public void call(CallState callState) {
                switch (state) {
                  case CONNECTING:
                    hideSoftKeyboard();
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏
                    if (videoFragment.isAdded()) {
                      manager.beginTransaction().show(videoFragment)
                          .commitAllowingStateLoss();
                    } else {
                      manager.beginTransaction().add(R.id.content_frame,
                          videoFragment).commitAllowingStateLoss();
                    }
                    break;
                  case CONNECTED:
                    break;
                  case DISCONNECTED:
                    if (reason == Reason.CANCEL) {
                      Toast.makeText(BusinessActivity.this, "call canceled", Toast.LENGTH_SHORT)
                          .show();
                    }

                    if (reason == Reason.BUSY) {
                      Toast.makeText(BusinessActivity.this, "the side is busy, please call later",
                          Toast.LENGTH_SHORT).show();
                    }
                    if (videoFragment.isAdded()) {
                      Log.i(TAG, "reason is " + reason);
                      videoFragment.releaseResource();
                      manager.beginTransaction().hide(videoFragment)
                          .commitAllowingStateLoss();
                    }
                    onDestroy();
                    break;
                  default:
                    break;
                }
              }
            });
      }

      @Override
      public void onVideoDataSourceChange(List<VideoInfo> videoInfos) {
        Observable.just(videoInfos)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<List<VideoInfo>>() {
              @Override
              public void call(List<VideoInfo> videoInfos) {
                videoFragment.onVideoDataSourceChange(videoInfos);
              }
            });
      }
    });
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
//        final FragmentManager manager = getFragmentManager();
    boolean isIncomingCall = intent.getBooleanExtra("isIncomingCall", false);
    if (true == isIncomingCall) {
      int callIndex = intent.getIntExtra("callIndex", -1);
      String callerName = intent.getStringExtra("callerName");
      String callerNumber = intent.getStringExtra("callerNumber");
      showIncomingCallDialog(callIndex, callerNumber, callerName);
    }
  }

  @Override
  protected void onStart() {
    super.onStart();
  }

  @Override
  protected void onDestroy() {
    NemoSDK.getInstance().setNemoSDKListener(null);
    super.onDestroy();
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
  }

  private void hideSoftKeyboard() {
    if (null != getCurrentFocus()) {
      InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(
          INPUT_METHOD_SERVICE);
      inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }
  }

  private void showIncomingCallDialog(final int callIndex, String callerNumber, String callerName) {

    new AlertDialog.Builder(BusinessActivity.this)
        .setTitle("RECEIVE CALL")
        .setMessage("caller name :" + callerName + "\ncaller number" + callerNumber)
        .setNegativeButton("接听", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            NemoSDK.getInstance().answerCall(callIndex, true);
            dialog.dismiss();
          }
        })
        .setPositiveButton("拒接", new OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
            NemoSDK.getInstance().answerCall(callIndex, false);
            dialogInterface.dismiss();
          }
        })
        .show();

  }

  public interface CallListener {

    void onContentStateChanged(NemoSDKListener.ContentState contentState);

    void onNewContentReceive(Bitmap bitmap);

    void onVideoDataSourceChange(List<VideoInfo> videoInfos);
  }

}
