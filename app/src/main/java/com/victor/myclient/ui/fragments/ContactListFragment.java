package com.victor.myclient.ui.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.victor.myclient.bean.ContactListData;
import com.victor.myclient.ui.activity.AddContactorActivity;
import com.victor.myclient.ui.adapters.ContactListAdapter;
import com.victor.myclient.ui.base.BaseFragment;
import com.victor.myclient.utils.MyActivityManager;
import com.victor.myclient.utils.Utils;
import com.victor.myclient.utils.sortlist.CharacterParser;
import com.victor.myclient.utils.sortlist.PinyinComparator;
import com.victor.myclient.utils.sortlist.SortModel;
import com.victor.myclient.widget.ClearEditText;
import com.victor.myclient.widget.PingYinSideBar;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import demo.animen.com.xiaoyutask.R;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by victor on 2017/4/24.
 */

public class ContactListFragment extends BaseFragment {
    private RecyclerView sortView;
    private PingYinSideBar sideBar;
    private TextView textDialog;
    private ContactListAdapter adapter;
    private RelativeLayout back;
    private ClearEditText mClearEditText;
    private CharacterParser characterParser;
    private List<SortModel> SourceDateList;
    private PinyinComparator pinyinComparator;
    private RecyclerView.LayoutManager layoutManager;
    private TextView add_new;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_contact;
    }

    @Override
    public void onResume() {
        super.onResume();
        getInfo();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        getInfo();
    }

    @Override
    protected void initView() {
        sideBar = (PingYinSideBar) rootView.findViewById(R.id.sidrbar);
        textDialog = (TextView) rootView.findViewById(R.id.dialog);

        sideBar.setTextView(textDialog);
        add_new = (TextView) rootView.findViewById(R.id.add_new_contact);
        back = (RelativeLayout) rootView.findViewById(R.id.back_to_main_contact_list);
        sortView = (RecyclerView) rootView.findViewById(R.id.sortlist);
        layoutManager = new LinearLayoutManager(activity);
        sortView.setLayoutManager(layoutManager);
        adapter = new ContactListAdapter(activity, sortView);
        sortView.setAdapter(adapter);

        mClearEditText = (ClearEditText) rootView
                .findViewById(R.id.filter_edit);
        // 实例化汉字转拼音类
        sideBar.setTextView(textDialog);
        // 设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new PingYinSideBar
                .OnTouchingLetterChangedListener() {

            @SuppressLint("NewApi")
            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                if (adapter != null) {
                    int position = adapter.getPositionForSection(s.charAt(0));
                    if (position != -1) {
                        sortView.scrollToPosition(position);
                    }
                }

            }
        });

        mClearEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                mClearEditText.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

            }
        });
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
        initEvent();
    }

    private void initEvent() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyActivityManager.startActivity(activity, AddContactorActivity.class);
            }
        });
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder
                    viewHolder) {
                return makeMovementFlags(0, ItemTouchHelper.RIGHT);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                List<ContactListData> contactListDatas = DataSupport.findAll(ContactListData.class);
                String name = SourceDateList.get(position).getName();
                for (ContactListData contactListData : contactListDatas) {
                    if (contactListData.getName().equals(name)) {
                        contactListData.delete();
                    }
                }
                SourceDateList.remove(position);
                adapter.updateListView(SourceDateList);
            }


        });
        helper.attachToRecyclerView(sortView);
    }

    /**
     * 为RecyclerView填充数据
     */
    private List<SortModel> filledData(String[] date, String[] numbers) {
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

    private void getInfo() {
        Observable.from(DataSupport.findAll(ContactListData.class)).observeOn(AndroidSchedulers
                .mainThread())
                .subscribeOn(Schedulers.io())
                .toList()
                .subscribe(new Observer<List<ContactListData>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Utils.showShortToast(activity, "发生错误");
                    }

                    @Override
                    public void onNext(List<ContactListData> contactListDatas) {
                        List<String> names = new ArrayList<>();
                        List<String> numbers = new ArrayList<>();
                        for (ContactListData contactListData : contactListDatas) {
                            names.add(contactListData.getName());
                            numbers.add(contactListData.getNumber());
                        }
                        String[] names_list = new String[]{};
                        String[] numbers_list = new String[]{};
                        numbers_list = numbers.toArray(numbers_list);
                        names_list = names.toArray(names_list);
                        SourceDateList = filledData(names_list, numbers_list);
                        // 根据a-z进行排序源数据
                        Collections.sort(SourceDateList, pinyinComparator);
                        adapter.addItems(SourceDateList);
                    }
                });
    }


}
