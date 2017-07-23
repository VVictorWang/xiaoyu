package com.victor.myclient.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.victor.myclient.utils.Utils;
import com.victor.myclient.activity.VideoActivity;
import com.victor.myclient.view.CircleTextImageView;

import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import demo.animen.com.xiaoyutask.R;

/**
 * Created by victor on 2017/4/24.
 */

public class CallFragment extends Fragment {
    private Activity activity;
    private View layout;
    private CircleTextImageView[] circleTextImageViews = new CircleTextImageView[13];
    private TextView textView;
    private CircleImageView backspace_number;
    private RelativeLayout back;
    Handler hander = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what >= 0 && msg.what <= 9) {
                textView.append("" + msg.what);
                circleTextImageViews[msg.what].setImageDrawable(getResources().getDrawable(R.drawable.call_number_image_selecte));
                backspace_number.setVisibility(View.VISIBLE);
            } else if (msg.what == 10) {
                textView.append("*");
                circleTextImageViews[msg.what].setImageDrawable(getResources().getDrawable(R.drawable.call_number_image_selecte));
                backspace_number.setVisibility(View.VISIBLE);
            } else if (msg.what == 11) {
                textView.append("#");
                circleTextImageViews[msg.what].setImageDrawable(getResources().getDrawable(R.drawable.call_number_image_selecte));
                backspace_number.setVisibility(View.VISIBLE);
            } else if (msg.what > 11) {
                circleTextImageViews[msg.what-12].setImageDrawable(getResources().getDrawable(R.drawable.call_number_image));
                //权限检查


            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (layout == null) {
            activity = this.getActivity();
            layout = activity.getLayoutInflater().inflate(R.layout.contact_call, null);
        } else {
            ViewGroup parent = (ViewGroup) layout.getParent();
            if (parent != null) {
                parent.removeView(layout);
            }
        }
        init();
        return layout;
    }
    private void init() {
        circleTextImageViews[0] = (CircleTextImageView) layout.findViewById(R.id.number_call_0);
        circleTextImageViews[1] = (CircleTextImageView) layout.findViewById(R.id.number_call_1);
        circleTextImageViews[2] = (CircleTextImageView) layout.findViewById(R.id.number_call_2);
        circleTextImageViews[3] = (CircleTextImageView) layout.findViewById(R.id.number_call_3);
        circleTextImageViews[4] = (CircleTextImageView) layout.findViewById(R.id.number_call_4);
        circleTextImageViews[5] = (CircleTextImageView) layout.findViewById(R.id.number_call_5);
        circleTextImageViews[6] = (CircleTextImageView) layout.findViewById(R.id.number_call_6);
        circleTextImageViews[7] = (CircleTextImageView) layout.findViewById(R.id.number_call_7);
        circleTextImageViews[8] = (CircleTextImageView) layout.findViewById(R.id.number_call_8);
        circleTextImageViews[9] = (CircleTextImageView) layout.findViewById(R.id.number_call_9);
        circleTextImageViews[10] = (CircleTextImageView) layout.findViewById(R.id.number_call_10);
        circleTextImageViews[11] = (CircleTextImageView) layout.findViewById(R.id.number_call_11);
        circleTextImageViews[12] = (CircleTextImageView) layout.findViewById(R.id.number_call);
        back = (RelativeLayout) layout.findViewById(R.id.back_to_main_contact_call);
        textView = (TextView) layout.findViewById(R.id.call_number);
        backspace_number = (CircleImageView) layout.findViewById(R.id.backspace_number_image);
        initEvent();
    }
    private void initEvent(){
        backspace_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = textView.getText().toString();
                if (!text.equals("")) {
                    text = text.substring(0, text.length() - 1);
                } else {
                    backspace_number.setVisibility(View.GONE);
                }
                textView.setText(text);
                if (text.equals("")) {
                    backspace_number.setVisibility(View.GONE);
                }
            }
        });
        for (int i = 0; i < 12; i++) {
            final int j = i;

            circleTextImageViews[i].setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (j>=0 && j<=11) {

                        hander.sendEmptyMessage(j);
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                hander.sendEmptyMessage(j +12);
                            }
                        },150);
                    }
                }
            });

        }
        circleTextImageViews[12].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
                //呼叫用户，以及没有密码的会议
                String number = textView.getText().toString();
                if (number.isEmpty() || number == null) {
                    Utils.showShortToast(activity, "请输入号码！");
                } else {
                    Intent intent = new Intent(activity, VideoActivity.class);
                    intent.putExtra("number", number);
                    intent.putExtra("type", "patient");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }


    private void checkPermission() {
        if (!(ContextCompat
            .checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED) &&
            !(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat
                .requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, 0);
        } else if (!(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO)
            == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat
                .requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, 0);
        } else if (!(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 0);
        }
    }
}
