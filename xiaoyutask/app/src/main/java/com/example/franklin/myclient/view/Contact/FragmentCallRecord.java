package com.example.franklin.myclient.view.Contact;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.franklin.myclient.view.Contact.Record.CallRecord;
import com.example.franklin.myclient.view.Contact.Record.CallRecordAdapter;
import com.example.franklin.myclient.view.Contact.sortlist.SideBar;
import com.example.franklin.myclient.DataBase.ContactDBhelper;
import com.example.franklin.myclient.SomeUtils.Utils;

import demo.animen.com.xiaoyutask.R;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by 小武哥 on 2017/4/29.
 */

public class FragmentCallRecord extends Fragment {

  private Activity activity;
  private View view;
  private RecyclerView recyclerView;
  private SideBar sideBar;
  private TextView dialog;
  private CallRecordAdapter adapter;
  private List<CallRecord> list=new ArrayList<>();
  private ContactDBhelper dBhelper;

  private RelativeLayout back;

  private RecyclerView.LayoutManager layoutManager;
  @Override
  public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,Bundle savedInstanceState){
    if(null==view){
      view=activity.getLayoutInflater().inflate(R.layout.fragment_contact_record,null);
    }else{
      ViewGroup parent=(ViewGroup)view .getParent();
      if(null!=parent){
        parent.removeView(view);
      }
    }
    initView();
    return view;
  }
  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = getActivity();
//    new ConstactAsyncTask().execute(0);

  }
  private void initView(){

    dBhelper=new ContactDBhelper(activity);
//    dBhelper.insertRecordList(null,"123456","小明",new Date(System.currentTimeMillis()).getTime(),null,2);
//
//    dBhelper.insertRecordList(null,"320483","张亮",new Date(System.currentTimeMillis()).getTime(),null,1);


    recyclerView=(RecyclerView)view.findViewById(R.id.record_list);
    layoutManager=new LinearLayoutManager(activity);
    recyclerView.setLayoutManager(layoutManager);

    back=(RelativeLayout) view .findViewById(R.id.back_to_main_contact_list);
    back.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        Utils.finishActivity(activity);
      }
    });
    new FindRecordsTask().execute(1);
  }

  public class FindRecordsTask extends AsyncTask<Integer, Integer, Integer> {
    @Override
    protected Integer doInBackground(Integer... params) {
      Cursor cursor=dBhelper.getAllRecordItems();
      cursor.moveToFirst();
      while (cursor.moveToNext())
        {
          String name=cursor.getString(cursor.getColumnIndex(ContactDBhelper.DB_COLUMN_NAME));
          String telephoneNum=cursor.getString(cursor.getColumnIndex(ContactDBhelper.DB_COLUMN_NUMBER));
          String xiaoyu=cursor.getString(cursor.getColumnIndex(ContactDBhelper.DB_COLUMN_XIAOYU));
          int state=cursor.getInt(cursor.getColumnIndex(ContactDBhelper.DB_COLUMN_STATUS));
          Date date=new Date(cursor.getLong(cursor.getColumnIndex(ContactDBhelper.DB_COLUMN_TIME)));
          String imageUrl=cursor.getString(cursor.getColumnIndex(ContactDBhelper.DB_COLUMN_IMAGE_URL));
          list.add(new CallRecord(name,telephoneNum ,xiaoyu,state,date,null,imageUrl));
        };
        cursor.close();


      Collections.sort(list, new Comparator<CallRecord>() {
        @Override
        public int compare(CallRecord callRecord, CallRecord t1) {
          Date date1=callRecord.getDate();
          Date date2=t1.getDate();
          if(date1.getTime()<date2.getTime()){
            return 1;
          }else if(date1.getTime()>date2.getTime()){
            return -1;
          }else{
            return 0;
          }
        }
      });
      return 1;
    }

    @Override
    protected void onPostExecute(Integer integer) {
      super.onPostExecute(integer);
      if (integer == 1) {
        adapter=new CallRecordAdapter(activity,list);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
      }
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
    }
  }
}
