package com.victor.myclient.view.Contact;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.log.L;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.ainemo.sdk.otf.NemoSDK;
import com.ainemo.sdk.otf.NemoSDKErrorCode;
import com.ainemo.sdk.otf.NemoSDKListener;
import com.ainemo.sdk.otf.VideoInfo;
import com.victor.myclient.SomeUtils.Utils;
import com.victor.myclient.xiaoyu.BusinessActivity;
import com.victor.myclient.xiaoyu.VideoActivity;
import com.victor.myclient.xiaoyu.VideoFragment;

import java.util.List;

import demo.animen.com.xiaoyutask.R;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by victor on 2017/4/24.
 */


public class ContactActivity extends FragmentActivity {

    private ViewPager viewPager;
    private ViewPageAdapter viewPageAdapter;
    private TabLayout tabLayout;
    private TabLayout.Tab one,two, three;
    private VideoFragment videoFragment ;

    @Override
    protected void onResume() {
        super.onResume();
        viewPager.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_fragment_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.contact_toolbar_sample);
//        toolbar.inflateMenu(R.menu.contact_menu);
//        setSupportActionBar(toolbar);

//        layoutManager = new LinearLayoutManager(ContactActivity.this);

        initTab();
        initEvent();
//        videoFragment = new VideoFragment();
//        final FragmentManager manager = getSupportFragmentManager();
//        NemoSDK.getInstance().setNemoSDKListener(new NemoSDKListener() {
//            @Override
//            public void onContentStateChanged(ContentState contentState) {
//                videoFragment.onContentStateChanged(contentState);
//            }
//
//            @Override
//            public void onCallFailed(int i) {
//
//            }
//
//            @Override
//            public void onNewContentReceive(Bitmap bitmap) {
//                videoFragment.onNewContentReceive(bitmap);
//            }
//
//            @Override
//            public void onCallStateChange(CallState callState, final String s) {
//                Observable.just(callState)
//                        .subscribeOn(Schedulers.immediate())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Action1<CallState>() {
//                            @Override
//                            public void call(CallState callState) {
//                                switch (callState) {
//                                    case CONNECTING:
//                                        hideSoftKeyboard();
//                                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏
//                                        Utils.startActivity(ContactActivity.this, VideoActivity.class);
//                                        finish();
//                                        if (videoFragment.isAdded()) {
//                                            manager.beginTransaction().show(videoFragment).commitAllowingStateLoss();
//                                            viewPager.setVisibility(View.GONE);
//                                        } else {
//                                            manager.beginTransaction().add(R.id.content_frame,
//                                                    videoFragment).commitAllowingStateLoss();
//                                        }
//                                        break;
//                                    case CONNECTED:
//                                        break;
//                                    case DISCONNECTED:
//                                        if (s.equals("CANCEL")) {
//                                            Toast.makeText(ContactActivity.this, "call canceled", Toast.LENGTH_SHORT).show();
//                                        }
//
//                                        if (s.equals("BUSY")) {
//                                            Toast.makeText(ContactActivity.this, "the side is busy, please call later", Toast.LENGTH_SHORT).show();
//                                        }

//                                        if (videoFragment.isAdded()) {
//                                            Log.i("contact", "reason is " + s);
//                                           videoFragment.releaseResource();
//                                            manager.beginTransaction().hide(videoFragment).commitAllowingStateLoss();
//                                            viewPager.setVisibility(View.VISIBLE);
//                                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                                        }

//                                        break;
//                                    default:
//                                        break;
//                                }
//                            }
//                        });
//            }
//
//            @Override
//            public void onVideoDataSourceChange(List<VideoInfo> list) {
//                Observable.just(list)
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Action1<List<VideoInfo>>() {
//                            @Override
//                            public void call(List<VideoInfo> videoInfos) {
//                                videoFragment.onVideoDataSourceChange(videoInfos);
//                            }
//                        });
            }
//
//        });
//    }

    private void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void  initEvent(){
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab == tabLayout.getTabAt(0)) {
                    one.setIcon(getResources().getDrawable(R.drawable.contact_bottom_first_selected));
                    viewPager.setCurrentItem(0);
                } else if (tab == tabLayout.getTabAt(1)) {
                    two.setIcon(getResources().getDrawable(R.drawable.contact_bottom_second_selected));
                    viewPager.setCurrentItem(1);
                } else if (tab == tabLayout.getTabAt(2)) {
                    three.setIcon(getResources().getDrawable(R.drawable.contact_bottom_third_selected));
                    viewPager.setCurrentItem(2);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab == tabLayout.getTabAt(0)) {
                    one.setIcon(getResources().getDrawable(R.drawable.contact_bottom_first_nor));
                } else if (tab == tabLayout.getTabAt(1)) {
                    two.setIcon(getResources().getDrawable(R.drawable.contact_bottom_second_nor));
                } else if (tab == tabLayout.getTabAt(2)) {
                    three.setIcon(getResources().getDrawable(R.drawable.contact_bottom_third_nor));
                }

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void  initTab(){
        viewPager = (ViewPager) findViewById(R.id.contact_view_pager);
        viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPageAdapter);
        tabLayout = (TabLayout) findViewById(R.id.frag);
        tabLayout.setupWithViewPager(viewPager);
        one = tabLayout.getTabAt(0);
        two = tabLayout.getTabAt(1);
        three = tabLayout.getTabAt(2);
        one.setIcon(getResources().getDrawable(R.drawable.contact_bottom_first_selected));
        viewPager.setCurrentItem(0);
        two.setIcon(getResources().getDrawable(R.drawable.contact_bottom_second_nor));
        three.setIcon(getResources().getDrawable(R.drawable.contact_bottom_third_nor));

//        getSupportFragmentManager().beginTransaction().add(R.id.frag, fragmentContactList).show(fragmentContactList).commit();
    }



}
