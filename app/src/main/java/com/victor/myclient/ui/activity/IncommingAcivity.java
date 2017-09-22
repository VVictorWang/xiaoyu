package com.victor.myclient.ui.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ainemo.sdk.otf.NemoSDK;
import com.ainemo.sdk.otf.NemoSDKErrorCode;
import com.ainemo.sdk.otf.NemoSDKListener;
import com.ainemo.sdk.otf.VideoInfo;
import com.victor.myclient.utils.MyActivityManager;
import com.victor.myclient.bean.CallRecord;
import com.victor.myclient.utils.GlobalData;
import com.victor.myclient.utils.PrefUtils;
import com.victor.myclient.utils.Utils;
import com.victor.myclient.widget.CircleImageView;
import com.victor.myclient.widget.SimpleVideoView;
import com.victor.myclient.widget.VideoCellView;

import java.util.Date;
import java.util.List;

import demo.animen.com.xiaoyutask.R;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

//接电话的Activity
public class IncommingAcivity extends AppCompatActivity {

    private android.widget.TextView connmtdialfromtext;
    private android.widget.ImageButton connmtendcallbtn;
    private android.widget.ImageButton connmtanswerbtn;
    private ImageButton finishcall;
    private SimpleVideoView videoView;
    private ImageButton audioonlybtn;
    private ImageButton mutebtn;
    private ImageButton switchcamera;
    private CircleImageView user_image;
    private android.widget.RelativeLayout profilepic, videoLayout;
    private TextView time_call;
    private Chronometer mChronometer;

    private String name, number;
    private boolean foregroundCamera = true;
    private boolean micMute = false;
    private boolean audioMode = false;
    private boolean visible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.callincoming_fragment);
        MyActivityManager.getInstance().pushActivity(IncommingAcivity.this);
        initView();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏
        Intent intent = getIntent();
        boolean isIncoming = intent.getBooleanExtra("isIncomingCall", false);
        if (isIncoming) {
            final int callIndex = intent.getIntExtra("callIndex", -1);
            String callerName = intent.getStringExtra("callerName");
            String callerNumber = intent.getStringExtra("callerNumber");
            name = callerName;
            number = callerNumber;
            showIncomingCall(callIndex, callerName, callerNumber);
        }
        initData();
        initEvent();
    }

    private void initView() {
        this.connmtanswerbtn = (ImageButton) findViewById(R.id.conn_mt_answer_btn);
        this.connmtendcallbtn = (ImageButton) findViewById(R.id.conn_mt_endcall_btn);
        this.connmtdialfromtext = (TextView) findViewById(R.id.conn_mt_dial_from_text);
        this.profilepic = (RelativeLayout) findViewById(R.id.profile_pic);
        this.switchcamera = (ImageButton) findViewById(R.id.switch_camera);
        this.mutebtn = (ImageButton) findViewById(R.id.mute_btn);
        this.audioonlybtn = (ImageButton) findViewById(R.id.audio_only_btn);
        this.time_call = (TextView) findViewById(R.id.time_call_text);
        user_image = (CircleImageView) findViewById(R.id.bg_turn);
        finishcall = (ImageButton) findViewById(R.id.conn_mt_endcall_btn_calling);
        finishcall.setVisibility(View.GONE);
        videoView = (SimpleVideoView) findViewById(R.id.incoming_view);
        mChronometer = (Chronometer) findViewById(R.id.time_eclipse);
        videoLayout = (RelativeLayout) findViewById(R.id.video_layout);
    }

    @Override
    protected void onStart() {
        videoView.requestLocalFrame();
        super.onStart();
    }

    private void showIncomingCall(final int callIndex, final String callerNumber, final String
            callerName) {
        connmtanswerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏
                NemoSDK.getInstance().answerCall(callIndex, true);
            }
        });
        connmtendcallbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NemoSDK.getInstance().hangup();
                CallRecord callRecord = new CallRecord();
                callRecord.setState(CallRecord.CALL_REJECT);
                callRecord.setXiaoyuId(callerName);
                callRecord.setName(callerNumber);
                callRecord.setDate(new Date(System.currentTimeMillis()));
                callRecord.save();
                MyActivityManager.finishActivity(IncommingAcivity.this);
            }
        });
        connmtdialfromtext.setText(callerName + " (" + callerNumber + ")");
        Utils.showImage(IncommingAcivity.this, GlobalData.GET_CALLING_IMAGE, user_image);
    }

    private void initEvent() {
        switchcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foregroundCamera = !foregroundCamera;
                NemoSDK.getInstance().switchCamera(foregroundCamera);
            }
        });
        mutebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                micMute = !micMute;
                NemoSDK.getInstance().enableMic(micMute);
                if (micMute) {
                    BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R
                            .drawable.no_voice_bitmap);
                    Bitmap bitmap = drawable.getBitmap();
                    mutebtn.setImageBitmap(bitmap);
                } else {
                    BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R
                            .drawable.ic_toolbar_mic);
                    Bitmap bitmap = drawable.getBitmap();
                    mutebtn.setImageBitmap(bitmap);
                }
            }
        });
        audioonlybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioMode = !audioMode;
                NemoSDK.getInstance().switchCallMode(audioMode);
                if (audioMode) {
                    BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R
                            .drawable.audio_multi);
                    Bitmap bitmap = drawable.getBitmap();
                    audioonlybtn.setImageBitmap(bitmap);
                } else {
                    BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R
                            .drawable.ic_toolbar_audio_only);
                    Bitmap bitmap = drawable.getBitmap();
                    audioonlybtn.setImageBitmap(bitmap);
                }
            }
        });
        finishcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NemoSDK.getInstance().hangup();
                CallRecord callRecord = new CallRecord();
                callRecord.setDate(new Date(System.currentTimeMillis()));
                callRecord.setName(name);
                callRecord.setState(CallRecord.CALL_IN);
                callRecord.setXiaoyuId(number);
                callRecord.save();
                int eclpise = (int) ((SystemClock.elapsedRealtime() - mChronometer.getBase()) /
                        1000) + PrefUtils.getIntValue(IncommingAcivity.this, GlobalData
                        .ECLIPSE_TIME);
                PrefUtils.putIntValue(IncommingAcivity.this, GlobalData.ECLIPSE_TIME, eclpise);
                MyActivityManager.finishActivity(IncommingAcivity.this);
            }
        });

        videoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (visible) {
                    connmtdialfromtext.setVisibility(View.INVISIBLE);
                    if (time_call.getVisibility() == View.VISIBLE) {
                        time_call.setVisibility(View.INVISIBLE);
                    } else if (mChronometer.getVisibility() == View.VISIBLE) {
                        mChronometer.setVisibility(View.INVISIBLE);
                    }
                    profilepic.setVisibility(View.INVISIBLE);
                    audioonlybtn.setVisibility(View.INVISIBLE);
                    mutebtn.setVisibility(View.INVISIBLE);
                    switchcamera.setVisibility(View.INVISIBLE);
                    finishcall.setVisibility(View.INVISIBLE);
                } else {
                    connmtdialfromtext.setVisibility(View.VISIBLE);
                    time_call.setVisibility(View.VISIBLE);
                    mChronometer.setVisibility(View.INVISIBLE);
                    profilepic.setVisibility(View.VISIBLE);
                    audioonlybtn.setVisibility(View.VISIBLE);
                    mutebtn.setVisibility(View.VISIBLE);
                    switchcamera.setVisibility(View.VISIBLE);
                    finishcall.setVisibility(View.VISIBLE);
                }
                visible = !visible;
            }
        });
    }

    @Override
    protected void onDestroy() {
        NemoSDK.getInstance().setNemoSDKListener(null);
        super.onDestroy();
    }

    private void initData() {
        NemoSDK.getInstance().setNemoSDKListener(new NemoSDKListener() {
            @Override
            public void onContentStateChanged(ContentState contentState) {

            }

            @Override
            public void onCallFailed(int i) {

                Observable.just(i)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Integer>() {
                            @Override
                            public void call(Integer integer) {
                                if (NemoSDKErrorCode.WRONG_PASSWORD == integer) {
                                    Toast.makeText(IncommingAcivity.this, "密码错误", Toast
                                            .LENGTH_SHORT).show();
                                } else if (NemoSDKErrorCode.INVALID_PARAM == integer) {
                                    Toast.makeText(IncommingAcivity.this, "错误的号码", Toast
                                            .LENGTH_SHORT).show();
                                } else if (NemoSDKErrorCode.NETWORK_UNAVAILABLE == integer) {
                                    Toast.makeText(IncommingAcivity.this, "网络不可用", Toast
                                            .LENGTH_SHORT).show();
                                } else if (NemoSDKErrorCode.HOST_ERROR == integer) {
                                    Toast.makeText(IncommingAcivity.this, "主机错误", Toast
                                            .LENGTH_SHORT).show();
                                }
                            }
                        });
            }

            @Override
            public void onNewContentReceive(Bitmap bitmap) {

            }

            @Override
            public void onCallStateChange(CallState callState, final String s) {
                Observable.just(callState)
                        .subscribeOn(Schedulers.immediate())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<CallState>() {
                            @Override
                            public void call(CallState callState) {
                                switch (callState) {
                                    case CONNECTING:
                                        break;
                                    case CONNECTED:
                                        time_call.setVisibility(View.INVISIBLE);
                                        mChronometer.setVisibility(View.VISIBLE);
                                        mChronometer.setBase(SystemClock.elapsedRealtime());
                                        mChronometer.start();
                                        profilepic.setVisibility(View.INVISIBLE);
                                        connmtdialfromtext.setText("通话中");
                                        connmtdialfromtext.setVisibility(View.INVISIBLE);
                                        connmtanswerbtn.setVisibility(View.INVISIBLE);
                                        connmtendcallbtn.setVisibility(View.INVISIBLE);
                                        visible = false;
                                        break;
                                    case DISCONNECTED:
                                        if (s.equals("CANCEL")) {
                                            Toast.makeText(IncommingAcivity.this, "通话取消", Toast
                                                    .LENGTH_SHORT).show();
                                            releaseResource();
                                            MyActivityManager.finishActivity(IncommingAcivity.this);
                                        }

                                        if (s.equals("BUSY")) {
                                            Toast.makeText(IncommingAcivity.this, "对方忙", Toast
                                                    .LENGTH_SHORT).show();
                                            releaseResource();
                                            MyActivityManager.finishActivity(IncommingAcivity.this);
                                        }
                                        if (s.equals("CONF_FULL")) {
                                            Utils.showShortToast(IncommingAcivity.this, "会议室已满");
                                            releaseResource();
                                            MyActivityManager.finishActivity(IncommingAcivity.this);
                                        }
                                        if (s.equals("NET_WORK_ERROR")) {
                                            Utils.showShortToast(IncommingAcivity.this, "网络错误");
                                            releaseResource();
                                            MyActivityManager.finishActivity(IncommingAcivity.this);
                                        }
                                        setRequestedOrientation(ActivityInfo
                                                .SCREEN_ORIENTATION_PORTRAIT);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        });
            }

            @Override
            public void onVideoDataSourceChange(final List<VideoInfo> list) {
                Observable.just(list)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<List<VideoInfo>>() {
                            @Override
                            public void call(final List<VideoInfo> videoInfos) {
                                if (list.size() > 0) {
                                    videoView.setLayoutInfos(list);
                                } else {
                                    videoView.stopRender();
                                }
                                List<VideoCellView> videoCellViews = videoView.getmVideoViews();
                                for (int i = 0; i < videoCellViews.size(); i++) {
                                    videoCellViews.get(i).setTag(i + 1);
                                    videoCellViews.get(i).setOnClickListener(new View
                                            .OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            videoView.indexTag = (Integer) v.getTag();
                                            videoView.requestLayout();
                                        }
                                    });
                                }

                            }
                        });
            }
        });
    }

    private void releaseResource() {
        videoView.destroy();
    }


}
