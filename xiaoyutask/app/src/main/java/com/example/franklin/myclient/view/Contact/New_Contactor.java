package com.example.franklin.myclient.view.Contact;

import android.support.annotation.StyleableRes;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.franklin.myclient.DataBase.ContactDBhelper;
import com.example.franklin.myclient.Datas.ContactListData;
import com.example.franklin.myclient.SomeUtils.Utils;

import demo.animen.com.xiaoyutask.R;



public class New_Contactor extends AppCompatActivity {
    private RelativeLayout back;
    private Button finish;
    private TextInputEditText name;
    private TextInputEditText xiaoyu_number;
    private ContactDBhelper dBhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__contactor);
        InitView();
        InitEvent();
    }

    private void InitView() {
        back = (RelativeLayout) findViewById(R.id.back_to_main_add_new_contact);
        finish = (Button) findViewById(R.id.new_contact_finish);
        name = (TextInputEditText) findViewById(R.id.new_contact_name);
        xiaoyu_number = (TextInputEditText) findViewById(R.id.new_contact_xiao_yu_number);
        dBhelper = new ContactDBhelper(New_Contactor.this);
    }
    private void InitEvent(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.finishActivity(New_Contactor.this);
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name_infor = name.getText().toString();
                String number = xiaoyu_number.getText().toString();
                if (name_infor.equals("")) {
                    name.setError("姓名不能为空");
                } else if (number.equals("")) {
                    xiaoyu_number.setError("小鱼号不能为空");
                } else {
                    ContactListData contactListData = new ContactListData();
                    contactListData.setNumber(number);
                    contactListData.setName(name_infor);
                    contactListData.save();
                    Utils.finishActivity(New_Contactor.this);
                }
            }
        });
    }
}
