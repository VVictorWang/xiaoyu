package com.victor.myclient.xiaoyu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import com.ainemo.sdk.otf.ConnectNemoCallback;
import com.ainemo.sdk.otf.NemoSDK;
import com.ainemo.shared.Event.Business;
import demo.animen.com.xiaoyutask.R;

/**
 * Created by 小武哥 on 2017/4/29.
 */

public class ConnectNemoActivity extends AppCompatActivity implements OnClickListener{

  private EditText displayName;
  private EditText externalId;
  private NemoSDK nemoSDK=NemoSDK.getInstance();
  private static final String TAG = "ConnectNemoActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_connect_nemo);

    displayName=(EditText)findViewById(R.id.display_name);
    externalId=(EditText)findViewById(R.id.external_id);

  }
  @Override
  public void onClick(View view){
    switch(view.getId()){
      case R.id.connect_button:
        nemoSDK.connectNemo(displayName.getText().toString(), externalId.getText().toString(),
            new ConnectNemoCallback() {
              @Override
              public void onFailed(int i) {
                Log.e(TAG, "onFailed: ,errorCode is "+i );
              }

              @Override
              public void onSuccess(String s) {
                Log.i(TAG, "onSuccess: number is "+s);
                startActivity(new Intent(ConnectNemoActivity.this, Business.class));
              }
            });

      break;
    }
  }

}
