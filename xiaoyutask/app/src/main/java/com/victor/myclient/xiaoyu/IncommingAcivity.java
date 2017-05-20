package com.victor.myclient.xiaoyu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ainemo.sdk.otf.NemoSDK;

import de.hdodenhof.circleimageview.CircleImageView;
import demo.animen.com.xiaoyutask.R;


public class IncommingAcivity extends AppCompatActivity {

    private android.widget.ImageView bgturn;
    private android.widget.RelativeLayout profilepic;
    private android.widget.ImageView nemoicon;
    private android.widget.TextView connmtdialfromtext;
    private android.widget.ImageButton connmtendcallbtn;
    private android.widget.ImageButton connmtanswerbtn;
    private de.hdodenhof.circleimageview.CircleImageView usercapture;
    private boolean isIncoming;

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
            showIncomingCall(callIndex, callerName, callerNumber);
        }
    }

    private void initView() {
        this.usercapture = (CircleImageView) findViewById(R.id.user_capture);
        this.connmtanswerbtn = (ImageButton) findViewById(R.id.conn_mt_answer_btn);
        this.connmtendcallbtn = (ImageButton) findViewById(R.id.conn_mt_endcall_btn);
        this.connmtdialfromtext = (TextView) findViewById(R.id.conn_mt_dial_from_text);
        this.nemoicon = (ImageView) findViewById(R.id.nemo_icon);
        this.profilepic = (RelativeLayout) findViewById(R.id.profile_pic);
        this.bgturn = (ImageView) findViewById(R.id.bg_turn);
    }

    private void showIncomingCall(final int callIndex, String callerNumber, String callerName) {
        connmtanswerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NemoSDK.getInstance().answerCall(callIndex, true);
            }
        });
        connmtendcallbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        connmtdialfromtext.setText(callerName + " (" + callerNumber + ")");

}}
