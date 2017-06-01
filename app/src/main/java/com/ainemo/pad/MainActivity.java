package com.ainemo.pad;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import com.ainemo.sdk.otf.ConnectNemoCallback;
import com.ainemo.sdk.otf.NemoSDK;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "MainActivity";
    private NemoSDK nemoSDK = NemoSDK.getInstance();
    private EditText displayName;
    private EditText externalId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.sign).setOnClickListener(this);
        findViewById(R.id.connect_nemo).setOnClickListener(this);
        displayName = (EditText) findViewById(R.id.display_name);
        externalId = (EditText) findViewById(R.id.register_external_id);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.connect_nemo:
                nemoSDK.connectNemo(displayName.getText().toString(), externalId.getText().toString(),
                        new ConnectNemoCallback() {
                            @Override
                            public void onFailed(int errorCode) {
                                Log.e(TAG, "on connect failed, errorCode is " + errorCode);
                            }

                            @Override
                            public void onSuccess(String callNumber) {
                                Log.i(TAG, "number is " + callNumber);
                                Intent intent = new Intent(MainActivity.this, BusinessActivity.class);
                                startActivity(intent);
                            }
                        });
                break;
            case R.id.sign:
                break;
            default:
                break;
        }
    }
}
