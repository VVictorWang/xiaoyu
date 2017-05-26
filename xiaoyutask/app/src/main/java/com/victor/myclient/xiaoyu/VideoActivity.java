package com.victor.myclient.xiaoyu;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.log.L;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import com.victor.myclient.SomeUtils.Utils;
import com.victor.myclient.view.Contact.ContactActivity;
import com.victor.myclient.view.Contact.Record.CallRecord;

import java.util.Date;
import java.util.List;

import demo.animen.com.xiaoyutask.R;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class VideoActivity extends AppCompatActivity {
    private static final String TAG = VideoFragment.class.getSimpleName();
    private SimpleVideoView mVideoView;
    private ImageView mContent;
    private boolean foregroundCamera = true;
    private boolean micMute = false;
    private boolean audioMode = false;
    private ImageView bgturn;
    private android.widget.TextView connmtdialtotext;
    private android.widget.ImageButton connmtcancelcallbtn;
    private android.widget.ImageButton switchcamera;
    private android.widget.ImageButton mutebtn;
    private android.widget.ImageButton audioonlybtn;
    private RelativeLayout user_pic_layout;
    private ImageView user_pic;
    private TextView time_call;
    private NemoSDK nemoSDK;
    private String number,name;
    private boolean visible=true;
    private int start_time = 0;


    private int time=0,minute = 0,hour=0;
    Handler handler = new Handler(){
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calloutgoing_fragment);
        ActivityManage activityManage = ActivityManage.getInstance();
        activityManage.pushActivity(VideoActivity.this);
        nemoSDK = NemoSDK.getInstance();
         number = getIntent().getStringExtra("number");
        name = "victor";
        InitView();
        InitEvent();
        InitData();
    }

    @Override
    protected void onStart() {
        mVideoView.requestLocalFrame();
        super.onStart();
    }


    @Override
    protected void onDestroy() {
       nemoSDK.setNemoSDKListener(null);
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        ActivityManage.getInstance().popActivity(VideoActivity.this);
    }

    private void InitView() {
        mVideoView = (SimpleVideoView) findViewById(R.id.remote_video_view);
        mContent = (ImageView) findViewById(R.id.shared_content);
        this.audioonlybtn = (ImageButton) findViewById(R.id.audio_only_btn);
        this.mutebtn = (ImageButton) findViewById(R.id.mute_btn);
        this.switchcamera = (ImageButton) findViewById(R.id.switch_camera);
        this.connmtcancelcallbtn = (ImageButton) findViewById(R.id.conn_mt_cancelcall_btn);
        this.connmtdialtotext = (TextView) findViewById(R.id.conn_mt_dial_to_text);
        this.bgturn = (ImageView) findViewById(R.id.bg_turn);
        user_pic = (ImageView) findViewById(R.id.user_capture);
        user_pic_layout = (RelativeLayout) findViewById(R.id.profile_pic);
        time_call = (TextView) findViewById(R.id.call_time_text);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏
    }

    private void InitEvent() {
        switchcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foregroundCamera = !foregroundCamera;
                nemoSDK.switchCamera(foregroundCamera);
            }
        });
        mutebtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                micMute = !micMute;
                nemoSDK.enableMic(micMute);
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
                nemoSDK.switchCallMode(audioMode);
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
        connmtcancelcallbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nemoSDK.hangup();

                CallRecord callRecord = new CallRecord();
                Date date = new Date(System.currentTimeMillis());
                callRecord.setDate(date);
                callRecord.setName(name);
                callRecord.setXiaoyuId(number);
                callRecord.setState(CallRecord.CALL_OUT);
                callRecord.save();
                int during_hour  = hour +Utils.getIntValue(VideoActivity.this, GlobalData.DRURATION_HOUR);;
                int during_minute = minute + Utils.getIntValue(VideoActivity.this, GlobalData.DRURATION_MINITE);;
                int during_second = time + Utils.getIntValue(VideoActivity.this, GlobalData.DRURATION_SECOND);
                Utils.putIntValue(VideoActivity.this, GlobalData.DRURATION_HOUR, during_hour);
                Utils.putIntValue(VideoActivity.this, GlobalData.DRURATION_MINITE, during_minute);
                Utils.putIntValue(VideoActivity.this, GlobalData.DRURATION_SECOND, during_second);
                Utils.finishActivity(VideoActivity.this);
            }
        });
        mVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (visible) {
                    connmtcancelcallbtn.setVisibility(View.GONE);
                    time_call.setVisibility(View.GONE);
                    connmtdialtotext.setVisibility(View.GONE);
                    user_pic_layout.setVisibility(View.GONE);
                    visible = !visible;
                } else {
                    connmtdialtotext.setVisibility(View.VISIBLE);
                    connmtcancelcallbtn.setVisibility(View.VISIBLE);
                    time_call.setVisibility(View.VISIBLE);
                    user_pic_layout.setVisibility(View.VISIBLE);
                    visible = !visible;
                }

            }
        });
    }


    private void InitData() {
        time = 0;
        minute = 0;
        hour = 0;
        checkPermission();
        nemoSDK.makeCall(number);
       nemoSDK.setNemoSDKListener(new NemoSDKListener() {
            @Override
            public void onContentStateChanged(ContentState contentState) {
                if (NemoSDKListener.ContentState.ON_START == contentState) {
                    mContent.setVisibility(View.VISIBLE);
                } else {
                    mContent.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCallFailed(int i) {
                Observable.just(i)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Integer>() {
                            @Override
                            public void call(Integer integer) {
                                L.e("ContactActivity", "error code is " + integer);
                                if (NemoSDKErrorCode.WRONG_PASSWORD == integer) {
                                    final EditText editText  =new EditText(VideoActivity.this);
                                    new AlertDialog.Builder(VideoActivity.this).setTitle("请输入密码").setView(editText).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String password = editText.getText().toString();
                                            nemoSDK.makeCall(number, password);
                                            dialog.dismiss();
                                        }
                                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            Utils.finishActivity(VideoActivity.this);
                                        }
                                    }).show();
                                    Toast.makeText(VideoActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                                } else if (NemoSDKErrorCode.INVALID_PARAM == integer) {
                                    Toast.makeText(VideoActivity.this, "错误的号码", Toast.LENGTH_SHORT).show();
                                } else if (NemoSDKErrorCode.NETWORK_UNAVAILABLE == integer) {
                                    Toast.makeText(VideoActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
                                } else if (NemoSDKErrorCode.HOST_ERROR == integer) {
                                    Toast.makeText(VideoActivity.this, "主机错误", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

            @Override
            public void onNewContentReceive(Bitmap bitmap) {
                mContent.setImageBitmap(bitmap);
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
                                        hideSoftKeyboard();
//                                        new Thread(new CountTime()).start();
                                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏
                                        break;
                                    case CONNECTED:
                                        new Thread(new TimeThread()).start();
                                        connmtdialtotext.setText("通话进行中...");
                                        connmtcancelcallbtn.setVisibility(View.GONE);
                                        time_call.setVisibility(View.GONE);
                                        connmtdialtotext.setVisibility(View.GONE);
                                        user_pic_layout.setVisibility(View.GONE);
                                        visible = !visible;
                                        break;
                                    case DISCONNECTED:
                                        if (s.equals("CANCEL")) {
                                            Toast.makeText(VideoActivity.this, "通话取消", Toast.LENGTH_SHORT).show();
                                        }
                                        if (s.equals("BUSY")) {
                                            Toast.makeText(VideoActivity.this, "对方忙", Toast.LENGTH_SHORT).show();
                                            releaseResource();
                                           Utils.finishActivity(VideoActivity.this);
                                        }
//                                        releaseResource();
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
                                    mVideoView.setLayoutInfos(list);
                                } else {
                                    mVideoView.stopRender();
                                }
                            }
                        });

            }
        });
    }

    private void releaseResource() {
        mVideoView.destroy();
    }

    private void checkPermission() {
        if (!(ContextCompat
                .checkSelfPermission(VideoActivity.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) &&
                !(ContextCompat.checkSelfPermission(VideoActivity.this, Manifest.permission.RECORD_AUDIO)
                        == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat
                    .requestPermissions(VideoActivity.this,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, 0);
        } else if (!(ContextCompat.checkSelfPermission(VideoActivity.this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat
                    .requestPermissions(VideoActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, 0);
        } else if (!(ContextCompat.checkSelfPermission(VideoActivity.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(VideoActivity.this, new String[]{Manifest.permission.CAMERA}, 0);
        }
    }
    private void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
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
