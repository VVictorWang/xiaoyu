package com.example.franklin.myclient.xiaoyu;

import android.Manifest;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import com.ainemo.sdk.otf.NemoSDK;
import demo.animen.com.xiaoyutask.R;

/**
 * Created by 小武哥 on 2017/4/29.
 */


public class DialFragment extends Fragment {

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.activity_connect_nemo, container, false);
  }
  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    final EditText number = (EditText) view.findViewById(R.id.display_name);
    final EditText password = (EditText) view.findViewById(R.id.external_id);
    view.findViewById(R.id.connect_button).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        checkPermission();
        NemoSDK.getInstance().makeCall(number.getText().toString(), password.getText().toString());
      }
    });
    super.onViewCreated(view, savedInstanceState);
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
