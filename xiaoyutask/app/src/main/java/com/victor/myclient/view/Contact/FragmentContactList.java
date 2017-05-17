package com.victor.myclient.view.Contact;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.victor.myclient.Datas.ContactListData;
import com.victor.myclient.SomeUtils.Utils;
import com.victor.myclient.view.Contact.sortlist.CharacterParser;
import com.victor.myclient.view.Contact.sortlist.SideBar;
import com.victor.myclient.view.Contact.sortlist.SortModel;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import demo.animen.com.xiaoyutask.R;

/**
 * Created by victor on 2017/4/24.
 */

public class FragmentContactList extends Fragment {
    private Activity activity;
    private View view;
    private RecyclerView sortView;
    private SideBar sideBar;
    private TextView dialog;
    private Contact_Adapter adapter;
    private RelativeLayout back;
    private ClearEditText mClearEditText;

    private CharacterParser characterParser;
    private List<SortModel> SourceDateList;

    private PinyinComparator pinyinComparator;
    private RecyclerView.LayoutManager layoutManager;
    private TextView add_new;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x123) {
               initData();
            } else if (msg.what == 0x124) {
                Utils.showShortToast(activity, "没有数据");
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = activity.getLayoutInflater().inflate(R.layout.activity_main_contact, null);
        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        initView();
        initEvent();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        new ConstactAsyncTask().execute(0);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        new ConstactAsyncTask().execute(0);

    }

    private void initView() {
        sideBar = (SideBar)view.findViewById(R.id.sidrbar);
        dialog = (TextView) view.findViewById(R.id.dialog);

        sideBar.setTextView(dialog);
        add_new = (TextView) view.findViewById(R.id.add_new_contact);
        back = (RelativeLayout) view.findViewById(R.id.back_to_main_contact_list);
        sortView = (RecyclerView) view.findViewById(R.id.sortlist);
        layoutManager = new LinearLayoutManager(activity);
        sortView.setLayoutManager(layoutManager);

        mClearEditText = (ClearEditText)view
                .findViewById(R.id.filter_edit);
        // 实例化汉字转拼音类
        sideBar.setTextView(dialog);
        // 设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @SuppressLint("NewApi")
            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置

                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortView.scrollToPosition(position);
                }
            }
        });

        mClearEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                mClearEditText.setGravity(Gravity.LEFT| Gravity.CENTER_VERTICAL);

            }
        });
    }
    private void initEvent(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.startActivity(activity, New_Contactor.class);
            }
        });
    }
    private void initData() {
        adapter = new Contact_Adapter(activity, SourceDateList);
        sortView.setAdapter(adapter);
        // 根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


    }


    private class ConstactAsyncTask extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected Integer doInBackground(Integer... arg0) {
            if (DataSupport.isExist(ContactListData.class)) {
                List<ContactListData> contactListDatas = DataSupport.findAll(ContactListData.class);
                List<String> names = new ArrayList<>();
                List<String> numbers = new ArrayList<>();
                for (ContactListData contactListData : contactListDatas) {
                    names.add(contactListData.getName());
                    numbers.add(contactListData.getNumber());
                }
                String[] names_list = new String[] {};
                String[] numbers_list = new String[]{};
                numbers_list = numbers.toArray(numbers_list);
                names_list = names.toArray(names_list);
                SourceDateList = filledData(names_list,numbers_list);

                // 根据a-z进行排序源数据
                Collections.sort(SourceDateList, pinyinComparator);
                handler.sendEmptyMessage(0x123);
            }else
                handler.sendEmptyMessage(0x124);
            return 1;
        }
        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);



        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

    }

    /**
     * 为ListView填充数据
     *
     * @param date
     * @return
     */
    private List<SortModel> filledData(String[] date,String[] numbers) {
        List<SortModel> mSortList = new ArrayList<SortModel>();

        for (int i = 0; i < date.length; i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(date[i]);
            sortModel.setNumber(numbers[i]);
            // 汉字转换成拼音
            String pinyin = characterParser.getSelling(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }

    /**
     * 根据输入框中的值来过滤数据并更新recyclerview
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<SortModel> filterDateList = new ArrayList<SortModel>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList;
        } else {
            filterDateList.clear();
            for (SortModel sortModel : SourceDateList) {
                String name = sortModel.getName();
                if (name.indexOf(filterStr.toString()) != -1
                        || characterParser.getSelling(name).startsWith(
                        filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }
}
