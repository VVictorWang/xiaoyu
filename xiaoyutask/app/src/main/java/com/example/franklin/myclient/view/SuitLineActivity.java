package com.example.franklin.myclient.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import demo.animen.com.xiaoyutask.R;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import tech.linjiang.suitlines.SuitLines;
import tech.linjiang.suitlines.Unit;

/**
 * Created by 小武哥 on 2017/4/28.
 */

public class SuitLineActivity extends AppCompatActivity {

  private SuitLines suitLines;
  private List<Unit> lines=new ArrayList<>();

  @Override
  public void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.suit_line);
    initView();

  }
  private void initView(){
    suitLines=(SuitLines)findViewById(R.id.suitlines);
    for(int i=0;i<14;i++){
      lines.add(new Unit(new SecureRandom().nextInt(48),i+""));
    }
    suitLines.feedWithAnim(lines);
  }

}
