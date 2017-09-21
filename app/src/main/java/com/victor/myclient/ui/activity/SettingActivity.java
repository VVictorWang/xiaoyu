package com.victor.myclient.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView.Style;
import com.victor.myclient.ActivityManage;
import com.victor.myclient.api.UserApi;
import com.victor.myclient.data.MessageResponse;
import com.victor.myclient.data.UserInfor;
import com.victor.myclient.ui.base.BaseActivity;
import com.victor.myclient.utils.GlideImageLoader;
import com.victor.myclient.utils.GlobalData;
import com.victor.myclient.utils.PrefUtils;
import com.victor.myclient.utils.Utils;
import com.victor.myclient.view.CircleImageView;
import com.victor.myclient.view.CircleTextImageView;

import java.io.File;
import java.util.ArrayList;

import demo.animen.com.xiaoyutask.R;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by victor on 2017/4/24.
 */

public class SettingActivity extends BaseActivity {

    public static final String TAG = "@victor SettingActivity";
    final static int IMAGE_PICKER = 1;
    private CircleImageView loginhead;
    private RelativeLayout settingback;
    private Button settingchangeimage;
    private EditText changemail;
    private CircleTextImageView changemailbutton;
    private CircleTextImageView changepassword;
    private TextView logoutoutbutton;
    private String email;

    private static OnAvatarChanged sOnAvatarChanged;

    @Override
    protected void onResume() {
        resumeView();
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        email = PrefUtils.getValue(getActivity(), GlobalData.USer_email);
        super.onCreate(savedInstanceState);
        initEvent();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected Activity getActivity() {
        return SettingActivity.this;
    }

    private void resumeView() {
        changemail.setText("绑定邮箱: " + PrefUtils.getValue(SettingActivity.this, GlobalData
                .USer_email));
    }

    @Override
    protected void initView() {
        this.logoutoutbutton = (TextView) findViewById(R.id.logout_out_button);
        this.changepassword = (CircleTextImageView) findViewById(R.id.change_password);
        this.changemailbutton = (CircleTextImageView) findViewById(R.id.change_mail_button);
        this.changemail = (EditText) findViewById(R.id.change_mail);
        this.settingchangeimage = (Button) findViewById(R.id.setting_change_image);
        this.loginhead = (CircleImageView) findViewById(R.id.login_head_setting);
        this.settingback = (RelativeLayout) findViewById(R.id.setting_back);
        changemail.setText("绑定邮箱: " + email);
        Utils.showImage(getActivity(), GlobalData.GET_PATIENT_FAMILY_IMAGE + PrefUtils.getValue
                (SettingActivity.this, GlobalData.FAMILY_IMage), loginhead);
    }

    private void initEvent() {
        changemailbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManage.startActivity(getActivity(), ChangeEmailActivity.class);
            }
        });
        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManage.startActivity(getActivity(), ChangePwdActivity.class);
            }
        });

        logoutoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefUtils.putBooleanValue(SettingActivity.this, GlobalData.Login_status, false);
                ActivityManage.startActivity(getActivity(), LoginActivity.class);

            }
        });
        settingback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManage.finishActivity(getActivity());
            }
        });

        settingchangeimage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker imagePicker = ImagePicker.getInstance();
                imagePicker.setMultiMode(false);
                imagePicker.setImageLoader(new GlideImageLoader()); //設置圖片加載器
                imagePicker.setShowCamera(true); //顯示拍照按鈕
                imagePicker.setCrop(true); //允許裁剪（單選才有效）
                imagePicker.setSaveRectangle(false); //是否按矩形區域保存
                imagePicker.setSelectLimit(1); //選中數量限制
                imagePicker.setStyle(Style.CIRCLE); //裁剪框的形狀
                imagePicker.setFocusWidth(800); //裁剪框的寬度。  單位像素（圓形自動取寬高最小值）
                imagePicker.setFocusHeight(800); //裁剪框的高度。  單位像素（圓形自動取寬高最小值）
                imagePicker.setOutPutX(1000); //保存文件的寬度。  單位像素
                imagePicker.setOutPutY(1000); //保存文件的高度。  單位像素
                Intent intent = new Intent(SettingActivity.this, ImageGridActivity.class);
                startActivityForResult(intent, IMAGE_PICKER);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IMAGE_PICKER:
                if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
                    if (data != null) {
                        ArrayList<ImageItem> images = (ArrayList<ImageItem>) data
                                .getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                        if (images.get(0).path != null) {
                            loginhead.setImageURI(Uri.fromFile(new File(images.get(0).path)));
                            uploadImage(images.get(0).path);
                        }
                    }
                }
                break;
        }
    }

    private void uploadImage(final String imageurl) {
        Observable<MessageResponse> observable = UserApi.getInstance().uploadImage(imageurl,
                PrefUtils.getValue(SettingActivity.this, GlobalData.PATIENTFAMILY_ID));
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<MessageResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "error upload");
                    }

                    @Override
                    public void onNext(MessageResponse messageResponse) {
                        if (messageResponse.getMessage().contains("success") && sOnAvatarChanged
                                != null) {
                            sOnAvatarChanged.OnImageChanged();
                        }
                    }
                });
    }

    public static void setOnAvatarChanged(OnAvatarChanged onAvatarChanged) {
        sOnAvatarChanged = onAvatarChanged;
    }

    public interface OnAvatarChanged {
        void OnImageChanged();
    }

}
