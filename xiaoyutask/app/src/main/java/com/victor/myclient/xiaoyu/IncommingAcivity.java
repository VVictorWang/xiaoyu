package com.victor.myclient.xiaoyu;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
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
import com.ainemo.sdk.otf.NemoSDKListener;
import com.ainemo.sdk.otf.VideoInfo;
import com.victor.myclient.SomeUtils.GlobalData;
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
    private ImageView usercapture;
    private SimpleVideoView videoView;
    private boolean isIncoming;
    private ImageButton audioonlybtn;
    private ImageButton mutebtn;
    private ImageButton switchcamera;
    private ImageView bgturn;
    private android.widget.RelativeLayout profilepic;
    private ImageView nemoicon;
    private android.widget.RelativeLayout nemopic;
    private TextView time_call;
    private String name,number;

    private boolean foregroundCamera = true;
    private boolean micMute = false;
    private boolean audioMode = false;

    private int time = 0, minute = 0, hour = 0;

    private int start_time;
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
                } else if ( time>=60) {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.callincoming_fragment);

        initView();
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
        this.usercapture = (ImageView) findViewById(R.id.user_capture);
        this.connmtanswerbtn = (ImageButton) findViewById(R.id.conn_mt_answer_btn);
        this.connmtendcallbtn = (ImageButton) findViewById(R.id.conn_mt_endcall_btn);
        this.connmtdialfromtext = (TextView) findViewById(R.id.conn_mt_dial_from_text);
        this.nemopic = (RelativeLayout) findViewById(R.id.nemo_pic);
        this.nemoicon = (ImageView) findViewById(R.id.nemo_icon);
        this.profilepic = (RelativeLayout) findViewById(R.id.profile_pic);
        this.bgturn = (ImageView) findViewById(R.id.bg_turn);
        this.switchcamera = (ImageButton) findViewById(R.id.switch_camera);
        this.mutebtn = (ImageButton) findViewById(R.id.mute_btn);
        this.audioonlybtn = (ImageButton) findViewById(R.id.audio_only_btn);
        this.time_call = (TextView) findViewById(R.id.time_call_text);
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

                finish();
            }
        });
        connmtdialfromtext.setText(callerName + " (" + callerNumber + ")");

    }

    private void InitEvent() {
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (visible) {
                    connmtdialfromtext.setVisibility(View.GONE);
//                    connmtendcallbtn.setVisibility(View.GONE);
                    time_call.setVisibility(View.GONE);
                    profilepic.setVisibility(View.GONE);
                    audioonlybtn.setVisibility(View.GONE);
                    mutebtn.setVisibility(View.GONE);
                    switchcamera.setVisibility(View.GONE);
                    finishcall.setVisibility(View.GONE);
                } else {
                    connmtdialfromtext.setVisibility(View.VISIBLE);
//                    connmtendcallbtn.setVisibility(View.VISIBLE);
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
            }
        });
        audioonlybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioMode = !audioMode;
                NemoSDK.getInstance().switchCallMode(audioMode);
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
                int during_hour  = hour +Utils.getIntValue(IncommingAcivity.this, GlobalData.DRURATION_HOUR);;
                int during_minute = minute + Utils.getIntValue(IncommingAcivity.this, GlobalData.DRURATION_MINITE);;
                int during_second = time + Utils.getIntValue(IncommingAcivity.this, GlobalData.DRURATION_SECOND);
                Utils.putIntValue(IncommingAcivity.this, GlobalData.DRURATION_HOUR, during_hour);
                Utils.putIntValue(IncommingAcivity.this, GlobalData.DRURATION_MINITE, during_minute);
                Utils.putIntValue(IncommingAcivity.this, GlobalData.DRURATION_SECOND, during_second);
                Log.e(TAG, NemoSDK.getInstance().getStatisticsInfo());
                finish();
            }
        });
    }

    private void InitData() {
        NemoSDK.getInstance().setNemoSDKListener(new NemoSDKListener() {
            @Override
            public void onContentStateChanged(ContentState contentState) {

            }

            @Override
            public void onCallFailed(int i) {

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
//                                        myThread.start();
                                        break;
                                    case CONNECTED:
//                                        myThread.stop();
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
                                            Toast.makeText(IncommingAcivity.this, "call canceled", Toast.LENGTH_SHORT).show();
                                        }

                                        if (s.equals("BUSY")) {
                                            Toast.makeText(IncommingAcivity.this, "对方忙", Toast.LENGTH_SHORT).show();
                                            finish();
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
                            }
                        });
            }
        });
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

    class Counter implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                start_time++;
                if (start_time > 10) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.showShortToast(IncommingAcivity.this, "未接听");
                        }
                    });
                    finish();
                    CallRecord callRecord = new CallRecord();
                    callRecord.setName(name);
                    callRecord.setDate(new Date(System.currentTimeMillis()));
                    callRecord.setState(CallRecord.CALL_REJECT);
                    callRecord.save();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
