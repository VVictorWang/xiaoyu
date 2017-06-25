package com.victor.myclient.xiaoyu;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ainemo.sdk.otf.NemoSDK;
import com.ainemo.sdk.otf.NemoSDKErrorCode;
import com.ainemo.sdk.otf.NemoSDKListener;
import com.ainemo.sdk.otf.VideoInfo;
import com.victor.myclient.ActivityManage;
import com.victor.myclient.SomeUtils.GlobalData;
import com.victor.myclient.SomeUtils.MyBitmapUtils;
import com.victor.myclient.SomeUtils.Utils;
import com.victor.myclient.view.Contact.Record.CallRecord;

import java.util.Date;
import java.util.List;

import demo.animen.com.xiaoyutask.R;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class IncommingAcivity extends AppCompatActivity {

    private android.widget.TextView connmtdialfromtext;
    private android.widget.ImageButton connmtendcallbtn;
    private android.widget.ImageButton connmtanswerbtn;
    private ImageButton finishcall;
    private SimpleVideoView videoView;
    private boolean isIncoming;
    private ImageButton audioonlybtn;
    private ImageButton mutebtn;
    private ImageButton switchcamera;
    private ImageView user_image;
    private android.widget.RelativeLayout profilepic;
    private TextView time_call;
    private String name, number;
    private boolean foregroundCamera = true;
    private boolean micMute = false;
    private boolean audioMode = false;
    private int time = 0, minute = 0, hour = 0;
    private List<VideoCellView> videoCellViews;
    private boolean visible = true;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                time++;
                if (time < 10) {
                    if (minute >= 10) {
                        time_call.setText("通话时长: " + minute + ":0" + time);
                    } else {
                        time_call.setText("通话时长: 0" + minute + ":0" + time);
                    }
                } else if (time < 60) {
                    if (minute >= 10) {
                        time_call.setText("通话时长: " + minute + ":" + time);
                    } else {
                        time_call.setText("通话时长: 0" + minute + ":" + time);
                    }
                } else if (time >= 60) {
                    minute++;
                    time -= 60;
                    if (minute >= 10) {
                        time_call.setText("通话时长: " + minute + ":0" + time);
                    } else {
                        time_call.setText("通话时长: 0" + minute + ":0" + time);
                    }
                }
            }
        }
    };
    private static final String TAG = "IncommingAcivity";
    private MyBitmapUtils bitmapUtils = new MyBitmapUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.callincoming_fragment);
        ActivityManage.getInstance().pushActivity(IncommingAcivity.this);
        initView();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏
        Intent intent = getIntent();
        isIncoming = intent.getBooleanExtra("isIncomingCall", false);
        if (isIncoming) {
            final int callIndex = intent.getIntExtra("callIndex", -1);
            String callerName = intent.getStringExtra("callerName");
            String callerNumber = intent.getStringExtra("callerNumber");
            name = callerName;
            number = callerNumber;
            showIncomingCall(callIndex, callerName, callerNumber);
        }
        InitData();
        InitEvent();
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
        user_image = (ImageView) findViewById(R.id.user_capture);
        finishcall = (ImageButton) findViewById(R.id.conn_mt_endcall_btn_calling);
        finishcall.setVisibility(View.GONE);
        videoView = (SimpleVideoView) findViewById(R.id.incoming_view);
    }

    @Override
    protected void onStart() {
        videoView.requestLocalFrame();
        super.onStart();
    }

    private void showIncomingCall(final int callIndex, final String callerNumber, final String callerName) {
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

                Utils.finishActivity(IncommingAcivity.this);
            }
        });
        connmtdialfromtext.setText(callerName + " (" + callerNumber + ")");
        bitmapUtils.disPlay(user_image, GlobalData.GET_CALLING_IMAGE);
    }

    private void InitEvent() {
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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
                    BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.no_voice_bitmap);
                    Bitmap bitmap = drawable.getBitmap();
                    mutebtn.setImageBitmap(bitmap);
                } else {
                    BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_toolbar_mic);
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
                    BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.audio_multi);
                    Bitmap bitmap = drawable.getBitmap();
                    audioonlybtn.setImageBitmap(bitmap);
                } else {
                    BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_toolbar_audio_only);
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
                int during_hour = hour + Utils.getIntValue(IncommingAcivity.this, GlobalData.DRURATION_HOUR);

                int during_minute = minute + Utils.getIntValue(IncommingAcivity.this, GlobalData.DRURATION_MINITE);

                int during_second = time + Utils.getIntValue(IncommingAcivity.this, GlobalData.DRURATION_SECOND);
                Utils.putIntValue(IncommingAcivity.this, GlobalData.DRURATION_HOUR, during_hour);
                Utils.putIntValue(IncommingAcivity.this, GlobalData.DRURATION_MINITE, during_minute);
                Utils.putIntValue(IncommingAcivity.this, GlobalData.DRURATION_SECOND, during_second);
                Log.e(TAG, NemoSDK.getInstance().getStatisticsInfo());
                Utils.finishActivity(IncommingAcivity.this);
            }
        });
    }

    @Override
    protected void onDestroy() {
        NemoSDK.getInstance().setNemoSDKListener(null);
        super.onDestroy();
    }

    private void InitData() {
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
                                    Toast.makeText(IncommingAcivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                                } else if (NemoSDKErrorCode.INVALID_PARAM == integer) {
                                    Toast.makeText(IncommingAcivity.this, "错误的号码", Toast.LENGTH_SHORT).show();
                                } else if (NemoSDKErrorCode.NETWORK_UNAVAILABLE == integer) {
                                    Toast.makeText(IncommingAcivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
                                } else if (NemoSDKErrorCode.HOST_ERROR == integer) {
                                    Toast.makeText(IncommingAcivity.this, "主机错误", Toast.LENGTH_SHORT).show();
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
                                        new Thread(new IncommingAcivity.TimeThread()).start();
                                        profilepic.setVisibility(View.GONE);
                                        connmtdialfromtext.setText("通话中");
                                        connmtdialfromtext.setVisibility(View.GONE);
                                        time_call.setVisibility(View.GONE);
                                        connmtanswerbtn.setVisibility(View.GONE);
                                        connmtendcallbtn.setVisibility(View.GONE);

                                        break;
                                    case DISCONNECTED:
                                        if (s.equals("CANCEL")) {
                                            Toast.makeText(IncommingAcivity.this, "通话取消", Toast.LENGTH_SHORT).show();
                                            releaseResource();
                                            Utils.finishActivity(IncommingAcivity.this);
                                        }

                                        if (s.equals("BUSY")) {
                                            Toast.makeText(IncommingAcivity.this, "对方忙", Toast.LENGTH_SHORT).show();
                                            releaseResource();
                                            Utils.finishActivity(IncommingAcivity.this);
                                        }
                                        if (s.equals("CONF_FULL")) {
                                            Utils.showShortToast(IncommingAcivity.this, "会议室已满");
                                            releaseResource();
                                            Utils.finishActivity(IncommingAcivity.this);
                                        }
                                        if (s.equals("NET_WORK_ERROR")) {
                                            Utils.showShortToast(IncommingAcivity.this, "网络错误");
                                            releaseResource();
                                            Utils.finishActivity(IncommingAcivity.this);
                                        }
                                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
                            public void call(List<VideoInfo> videoInfos) {
                                if (list.size() > 0) {
                                    videoView.setLayoutInfos(list);
                                } else {
                                    videoView.stopRender();
                                }
                                videoCellViews = videoView.getmVideoViews();
                                videoView.getLocalVideoView().setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        videoView.indexTag = 0;
                                        videoView.requestLayout();
                                        if (visible) {
                                            connmtdialfromtext.setVisibility(View.GONE);
                                            time_call.setVisibility(View.GONE);
                                            profilepic.setVisibility(View.GONE);
                                            audioonlybtn.setVisibility(View.GONE);
                                            mutebtn.setVisibility(View.GONE);
                                            switchcamera.setVisibility(View.GONE);
                                            finishcall.setVisibility(View.GONE);
                                        } else {
                                            connmtdialfromtext.setVisibility(View.VISIBLE);
                                            time_call.setVisibility(View.VISIBLE);
                                            profilepic.setVisibility(View.VISIBLE);
                                            audioonlybtn.setVisibility(View.VISIBLE);
                                            mutebtn.setVisibility(View.VISIBLE);
                                            switchcamera.setVisibility(View.VISIBLE);
                                            finishcall.setVisibility(View.VISIBLE);
                                        }
                                        visible = !visible;
                                    }
                                });
                                for (int i = 0; i < videoCellViews.size(); i++) {
                                    videoCellViews.get(i).setTag(i + 1);
                                    videoCellViews.get(i).setOnClickListener(new View.OnClickListener() {
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

    class TimeThread implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                    handler.sendEmptyMessage(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
