package com.victor.myclient.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.victor.myclient.ActivityManage;
import com.victor.myclient.data.ContactListData;
import com.victor.myclient.ui.base.BaseActivity;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import demo.animen.com.xiaoyutask.R;


//新建联系人Activity
public class NewContactorActivity extends BaseActivity {
    private RelativeLayout back;
    private Button finish;
    private TextInputEditText name;
    private TextInputEditText xiaoyu_number;
    private List<ContactListData> contactListDatas;
    private List<String> names;

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initEvent();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new__contactor;
    }

    @Override
    protected Activity getActivity() {
        return NewContactorActivity.this;
    }

    @Override
    protected void initView() {
        back = (RelativeLayout) findViewById(R.id.back_to_main_add_new_contact);
        finish = (Button) findViewById(R.id.new_contact_finish);
        name = (TextInputEditText) findViewById(R.id.new_contact_name);
        xiaoyu_number = (TextInputEditText) findViewById(R.id.new_contact_xiao_yu_number);
    }

    private void initEvent() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManage.finishActivity(NewContactorActivity.this);
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactListDatas = DataSupport.findAll(ContactListData.class);
                names = new ArrayList<String>();
                for (ContactListData contactListData : contactListDatas) {
                    names.add(contactListData.getName());
                }
                String name_infor = name.getText().toString();
                String number = xiaoyu_number.getText().toString();
                if (name_infor.equals("")) {
                    name.setError("姓名不能为空");
                } else if (isExist(name_infor)) {
                    name.setError("联系人已存在");
                } else if (number.equals("")) {
                    xiaoyu_number.setError("小鱼号不能为空");
                } else if (!isInteger(number)) {
                    xiaoyu_number.setError("请输入正确的小鱼号");
                } else {
                    ContactListData contactListData = new ContactListData();
                    contactListData.setNumber(number);
                    contactListData.setName(name_infor);
                    contactListData.save();
                    ActivityManage.finishActivity(NewContactorActivity.this);
                }
            }

        });
    }

    private boolean isExist(String name) {
        for (String old : names) {
            if (name.equals(old)) {
                return true;
            }
        }
        return false;
    }
}
