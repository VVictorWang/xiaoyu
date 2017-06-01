package com.ainemo.pad;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.log.L;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import com.ainemo.sdk.otf.NemoSDK;
import com.ainemo.sdk.otf.NemoSDKErrorCode;
import com.ainemo.sdk.otf.NemoSDKListener;
import com.ainemo.sdk.otf.VideoInfo;
import java.util.List;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class BusinessActivity extends Activity {
    private static final String TAG = "BusinessActivity";
    private DialFragment mDialFragment;
    private VideoFragment mVideoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mDialFragment = new DialFragment();
        mVideoFragment = new VideoFragment();
        final FragmentManager manager = getFragmentManager();
        manager.beginTransaction().add(R.id.content_frame, mDialFragment).commitAllowingStateLoss();

        Intent intent = getIntent();
        boolean isIncomingCall = intent.getBooleanExtra("isIncomingCall", false);
        if (isIncomingCall) {
            final int callIndex = intent.getIntExtra("callIndex", -1);
            String callerName = intent.getStringExtra("callerName");
            String callerNumber = intent.getStringExtra("callerNumber");
            showIncomingCallDialog(callIndex, callerName, callerNumber);
        }
        NemoSDK.getInstance().setNemoSDKListener(new NemoSDKListener() {
            @Override
            public void onContentStateChanged(NemoSDKListener.ContentState contentState) {
                mVideoFragment.onContentStateChanged(contentState);
            }

            @Override
            public void onNewContentReceive(Bitmap bitmap) {
                mVideoFragment.onNewContentReceive(bitmap);
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
                                    Toast.makeText(BusinessActivity.this, "wrong password", Toast.LENGTH_SHORT).show();
                                } else if (NemoSDKErrorCode.INVALID_PARAM == integer) {
                                    Toast.makeText(BusinessActivity.this, "wrong param", Toast.LENGTH_SHORT).show();
                                } else if (NemoSDKErrorCode.NETWORK_UNAVAILABLE == integer) {
                                    Toast.makeText(BusinessActivity.this, "net work unavailable", Toast.LENGTH_SHORT).show();
                                } else if (NemoSDKErrorCode.HOST_ERROR == integer) {
                                    Toast.makeText(BusinessActivity.this, "host error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

            @Override
            public void onCallStateChange(final NemoSDKListener.CallState state, final NemoSDKListener.Reason reason) {
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
                                        if (mVideoFragment.isAdded()) {
                                            manager.beginTransaction().hide(mDialFragment).show(mVideoFragment).commitAllowingStateLoss();
                                        } else {
                                            manager.beginTransaction().add(R.id.content_frame,
                                                    mVideoFragment).hide(mDialFragment).commitAllowingStateLoss();
                                        }
                                        break;
                                    case CONNECTED:
                                        break;
                                    case DISCONNECTED:
                                        if (reason == Reason.CANCEL) {
                                            Toast.makeText(BusinessActivity.this, "call canceled", Toast.LENGTH_SHORT).show();
                                        }

                                        if (reason == Reason.BUSY) {
                                            Toast.makeText(BusinessActivity.this, "the side is busy, please call later", Toast.LENGTH_SHORT).show();
                                        }

                                        if (mVideoFragment.isAdded()) {
                                            Log.i(TAG, "reason is " + reason);
                                            mVideoFragment.releaseResource();
                                            manager.beginTransaction().hide(mVideoFragment).show(mDialFragment).commitAllowingStateLoss();
                                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                                        }

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
                                mVideoFragment.onVideoDataSourceChange(videoInfos);
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
        if (isIncomingCall) {
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
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void showIncomingCallDialog(final int callIndex, String callerNumber, String callerName) {
        new AlertDialog.Builder(BusinessActivity.this)
                .setTitle("RECEIVE CALL")
                .setMessage("caller name : " + callerName + "\n" + "caller number " + callerNumber)
                .setNegativeButton("accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NemoSDK.getInstance().answerCall(callIndex, true);
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("refuse", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NemoSDK.getInstance().answerCall(callIndex, false);
                        dialog.dismiss();
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
