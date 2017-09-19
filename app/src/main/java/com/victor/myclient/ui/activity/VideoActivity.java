package com.victor.myclient.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import com.google.gson.Gson;
import com.victor.myclient.ActivityManage;
import com.victor.myclient.data.CallRecord;
import com.victor.myclient.data.DoctorImage;
import com.victor.myclient.data.PatientImageInfor;
import com.victor.myclient.ui.base.BaseActivity;
import com.victor.myclient.utils.GlobalData;
import com.victor.myclient.utils.MyBitmapUtils;
import com.victor.myclient.utils.PrefUtils;
import com.victor.myclient.utils.Utils;
import com.victor.myclient.view.CircleImageView;
import com.victor.myclient.view.SimpleVideoView;
import com.victor.myclient.view.VideoCellView;

import java.util.Date;
import java.util.List;

import demo.animen.com.xiaoyutask.R;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class VideoActivity extends BaseActivity {
    private SimpleVideoView mVideoView;
    private CircleImageView mContent;
    private boolean foregroundCamera = true;
    private boolean micMute = false;
    private boolean audioMode = false;
    private android.widget.TextView connmtdialtotext;
    private android.widget.ImageButton connmtcancelcallbtn;
    private android.widget.ImageButton switchcamera;
    private android.widget.ImageButton mutebtn;
    private android.widget.ImageButton audioonlybtn;
    private RelativeLayout user_pic_layout;
    private TextView time_call;
    private String number, name, type;
    private boolean visible = true;

    private List<VideoCellView> videoCellViews;
    private MyBitmapUtils bitmapUtils = new MyBitmapUtils();
    private ImageView user_image;
    private int time = 0, minute = 0, hour = 0;
    private PatientImageInfor patientImageInfor = new PatientImageInfor();
    private DoctorImage doctorImage = new DoctorImage();
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
            } else if (msg.what == 2) {
                bitmapUtils.disPlay(user_image, GlobalData.GET_PATIENT_IMAGE + patientImageInfor
                        .getImage());
            } else if (msg.what == 3) {
                bitmapUtils.disPlay(user_image, GlobalData.GET_DOCTOR_IMAGE + doctorImage
                        .getDoctorimage());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        number = getIntent().getStringExtra("number");
        type = getIntent().getStringExtra("type");
        name = "无用户信息";
        InitView();
        InitEvent();
        InitData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.calloutgoing_fragment;
    }

    @Override
    protected Activity getActivity() {
        return VideoActivity.this;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void onStart() {
        mVideoView.requestLocalFrame();
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
        finish();
        ActivityManage.getInstance().popActivity(VideoActivity.this);
    }

    private void getImageUrl() {
        if (type.equals("patient")) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Gson gson = new Gson();
                    patientImageInfor = gson.fromJson(Utils.sendRequest(GlobalData
                            .GET_CALLING_IMAGE + "&identity=patient"), PatientImageInfor.class);
                    handler.sendEmptyMessage(2);
                }
            }).start();
        } else if (type.equals("doctor")) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Gson gson = new Gson();
                    doctorImage = gson.fromJson(Utils.sendRequest(GlobalData.GET_CALLING_IMAGE +
                            "&identity=doctor"), DoctorImage.class);
                    handler.sendEmptyMessage(3);

                }
            }).start();
        }


    }

    private void InitView() {
        mVideoView = (SimpleVideoView) findViewById(R.id.remote_video_view);
        mContent = (CircleImageView) findViewById(R.id.shared_content);
        this.audioonlybtn = (ImageButton) findViewById(R.id.audio_only_btn);
        this.mutebtn = (ImageButton) findViewById(R.id.mute_btn);
        this.switchcamera = (ImageButton) findViewById(R.id.switch_camera);
        this.connmtcancelcallbtn = (ImageButton) findViewById(R.id.conn_mt_cancelcall_btn);
        this.connmtdialtotext = (TextView) findViewById(R.id.conn_mt_dial_to_text);
        user_pic_layout = (RelativeLayout) findViewById(R.id.profile_pic);
        time_call = (TextView) findViewById(R.id.call_time_text);
        user_image = (com.victor.myclient.view.CircleImageView) findViewById(R.id.bg_turn);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏
        NemoSDK.getInstance().setNemoSDKListener(new NemoSDKListener() {
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
                                if (NemoSDKErrorCode.WRONG_PASSWORD == integer) {
                                    final EditText editText = new EditText(VideoActivity.this);
                                    new AlertDialog.Builder(VideoActivity.this).setTitle("请输入密码")
                                            .setView(editText).setPositiveButton("确定", new
                                            DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int
                                                        which) {
                                                    String password = editText.getText().toString();
                                                    dialog.dismiss();
                                                    NemoSDK.getInstance().makeCall(number,
                                                            password);
                                                }
                                            }).setNegativeButton("取消", new DialogInterface
                                            .OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            ActivityManage.finishActivity(VideoActivity.this);
                                        }
                                    }).show();
                                    Toast.makeText(VideoActivity.this, "密码错误", Toast
                                            .LENGTH_SHORT).show();
                                } else if (NemoSDKErrorCode.INVALID_PARAM == integer) {
                                    Toast.makeText(VideoActivity.this, "错误的号码", Toast
                                            .LENGTH_SHORT).show();
                                } else if (NemoSDKErrorCode.NETWORK_UNAVAILABLE == integer) {
                                    Toast.makeText(VideoActivity.this, "网络不可用", Toast
                                            .LENGTH_SHORT).show();
                                } else if (NemoSDKErrorCode.HOST_ERROR == integer) {
                                    Toast.makeText(VideoActivity.this, "主机错误", Toast
                                            .LENGTH_SHORT).show();
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
                                        getImageUrl();
                                        setRequestedOrientation(ActivityInfo
                                                .SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏
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
//                                            Toast.makeText(VideoActivity.this, "通话取消", Toast
// .LENGTH_SHORT).show();
//                                            releaseResource();
//                                            Utils.finishActivity(VideoActivity.this);
                                        }
                                        if (s.equals("BUSY")) {
                                            Toast.makeText(VideoActivity.this, "对方忙", Toast
                                                    .LENGTH_SHORT).show();
                                            releaseResource();
                                            ActivityManage.finishActivity(VideoActivity.this);
                                        }
                                        if (s.equals("CONF_FULL")) {
                                            Utils.showShortToast(VideoActivity.this, "会议室已满");
                                            releaseResource();
                                            ActivityManage.finishActivity(VideoActivity.this);
                                        }
                                        if (s.equals("NET_WORK_ERROR")) {
                                            Utils.showShortToast(VideoActivity.this, "网络错误");
                                            releaseResource();
                                            ActivityManage.finishActivity(VideoActivity.this);
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
                            public void call(List<VideoInfo> videoInfos) {
                                if (list.size() > 0) {
                                    mVideoView.setLayoutInfos(list);
                                } else {
                                    mVideoView.stopRender();
                                }
                                videoCellViews = mVideoView.getmVideoViews();
                                mVideoView.getLocalVideoView().setOnClickListener(new View
                                        .OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mVideoView.indexTag = 0;
                                        mVideoView.requestLayout();
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
                                for (int i = 0; i < videoCellViews.size(); i++) {
                                    videoCellViews.get(i).setTag(i + 1);
                                    videoCellViews.get(i).setOnClickListener(new View
                                            .OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mVideoView.indexTag = (Integer) v.getTag();
                                            mVideoView.requestLayout();
                                        }
                                    });
                                }
                            }
                        });

            }
        });
    }

    private void InitEvent() {
        switchcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foregroundCamera = !foregroundCamera;
                NemoSDK.getInstance().switchCamera(foregroundCamera);
            }
        });
        mutebtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
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
        connmtcancelcallbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NemoSDK.getInstance().hangup();
                CallRecord callRecord = new CallRecord();
                Date date = new Date(System.currentTimeMillis());
                callRecord.setDate(date);
                callRecord.setName(name);
                callRecord.setXiaoyuId(number);
                callRecord.setState(CallRecord.CALL_OUT);
                callRecord.save();
                int during_hour = hour + PrefUtils.getIntValue(VideoActivity.this, GlobalData
                        .DRURATION_HOUR);
                int during_minute = minute + PrefUtils.getIntValue(VideoActivity.this, GlobalData
                        .DRURATION_MINITE);
                int during_second = time + PrefUtils.getIntValue(VideoActivity.this, GlobalData
                        .DRURATION_SECOND);
                PrefUtils.putIntValue(VideoActivity.this, GlobalData.DRURATION_HOUR, during_hour);
                PrefUtils.putIntValue(VideoActivity.this, GlobalData.DRURATION_MINITE,
                        during_minute);
                PrefUtils.putIntValue(VideoActivity.this, GlobalData.DRURATION_SECOND,
                        during_second);
                ActivityManage.finishActivity(VideoActivity.this);
                NemoSDK.getInstance().setNemoSDKListener(null);
            }
        });
        mVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }


    private void InitData() {
        time = 0;
        minute = 0;
        hour = 0;
        checkPermission();
        NemoSDK.getInstance().makeCall(number);

    }

    private void releaseResource() {
        mVideoView.destroy();
    }

    private void checkPermission() {
        if (!(ContextCompat
                .checkSelfPermission(VideoActivity.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) &&
                !(ContextCompat.checkSelfPermission(VideoActivity.this, Manifest.permission
                        .RECORD_AUDIO)
                        == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat
                    .requestPermissions(VideoActivity.this,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission
                                    .RECORD_AUDIO}, 0);
        } else if (!(ContextCompat.checkSelfPermission(VideoActivity.this, Manifest.permission
                .RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat
                    .requestPermissions(VideoActivity.this, new String[]{Manifest.permission
                            .RECORD_AUDIO}, 0);
        } else if (!(ContextCompat.checkSelfPermission(VideoActivity.this, Manifest.permission
                .CAMERA)
                == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(VideoActivity.this, new String[]{Manifest
                    .permission.CAMERA}, 0);
        }
    }

    private void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService
                    (INPUT_METHOD_SERVICE);
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
