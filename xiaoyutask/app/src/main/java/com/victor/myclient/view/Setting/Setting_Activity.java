package com.victor.myclient.view.Setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.victor.myclient.Datas.UserInfor;
import com.victor.myclient.SomeUtils.GlobalData;
import com.victor.myclient.SomeUtils.MyBitmapUtils;
import com.victor.myclient.SomeUtils.Utils;
import com.victor.myclient.view.Setting.Change.ChangeEmailActivity;
import com.victor.myclient.view.Setting.Change.ChangeXiaoYuActivity;
import com.victor.myclient.view.Login.LoginActivity;
import com.victor.myclient.view.Setting.choosePhoto.GlideImageLoader;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView.Style;
import com.thinkcool.circletextimageview.CircleTextImageView;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import demo.animen.com.xiaoyutask.R;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by victor on 2017/4/24.
 */

public class Setting_Activity extends AppCompatActivity {

    private de.hdodenhof.circleimageview.CircleImageView loginhead;


    final static int IMAGE_PICKER = 1;
    private RelativeLayout settingback;
    private Button settingchangeimage;
    private EditText changemail;
    private CircleTextImageView changemailbutton;
    private CircleTextImageView changepassword;
    private TextView logoutoutbutton;
    private String email;

    private boolean networkavailable;

    private UserInfor userInfor;
    private MyBitmapUtils bitmapUtils = new MyBitmapUtils();
    @Override
    protected void onResume() {
        super.onResume();
        resumeView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        email = Utils.getValue(Setting_Activity.this, GlobalData.USer_email);
        initView();
        initEvent();
    }

    private void resumeView() {

//        bitmapUtils.disPlay(loginhead, GlobalData.GET_PATIENT_FAMILY_IMAGE + Utils.getValue(Setting_Activity.this, GlobalData.FAMILY_IMage));
        changemail.setText("绑定邮箱: " + Utils.getValue(Setting_Activity.this, GlobalData.USer_email));
    }

    private void initView() {
        this.logoutoutbutton = (TextView) findViewById(R.id.logout_out_button);
        this.changepassword = (CircleTextImageView) findViewById(R.id.change_password);
        this.changemailbutton = (CircleTextImageView) findViewById(R.id.change_mail_button);
        this.changemail = (EditText) findViewById(R.id.change_mail);
        this.settingchangeimage = (Button) findViewById(R.id.setting_change_image);
        this.loginhead = (CircleImageView) findViewById(R.id.login_head_setting);
        this.settingback = (RelativeLayout) findViewById(R.id.setting_back);
        changemail.setText("绑定邮箱: " + email);
        bitmapUtils.disPlay(loginhead, GlobalData.GET_PATIENT_FAMILY_IMAGE + Utils.getValue(Setting_Activity.this, GlobalData.FAMILY_IMage));
        networkavailable = Utils.isNetWorkAvailabe(Setting_Activity.this);
//        loginhead.setImageBitmap(BitmapUtil.getBitmap(GlobalData.DoctorIMage + Utils.getValue(Setting_Activity.this, GlobalData.PATIENTFAMILY_ID)));
//        imagePath = Utils.getValue(Setting_Activity.this, GlobalData.Img_URl);
//        if (imagePath != null) {
//            loginhead.setImageURI(Uri.fromFile(new File(imagePath)));
//        }
    }

    private void initEvent() {
        changemailbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting_Activity.this, ChangeEmailActivity.class);
                startActivity(intent);
            }
        });
        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting_Activity.this, ChangeXiaoYuActivity.class);
                startActivity(intent);
            }
        });

        logoutoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.putBooleanValue(Setting_Activity.this, GlobalData.Login_status, false);
                Intent intent = new Intent(Setting_Activity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        settingback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                Intent intent = new Intent(Setting_Activity.this, ImageGridActivity.class);
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
                        Log.i(TAG, "onActivityResult: 有数据");
                        // Toast.makeText(this, "有数据", Toast.LENGTH_SHORT).show();
                        if (images.get(0).path != null) {
                            Utils.putValue(Setting_Activity.this, GlobalData.Img_URl, images.get(0).path);
                            loginhead.setImageURI(Uri.fromFile(new File(images.get(0).path)));
                            uploadImage(images.get(0).path);
                        }
                    } else {
                        Log.i(TAG, "onActivityResult: 没有数据");
                    }
                }
                break;
        }
    }

    private void uploadImage(final String imageurl) {
        new Thread(new Runnable() {
            OkHttpClient client = new OkHttpClient();
            @Override
            public void run() {
                MediaType MEDIA_TYPE_JPG = MediaType.parse("image/png");
                client = new OkHttpClient();
                MultipartBody.Builder builder = new MultipartBody.Builder();
                File f = new File(imageurl);
                String id = Utils.getValue(Setting_Activity.this, GlobalData.PATIENTFAMILY_ID);
                builder.addFormDataPart("patientFamilyImage", f.toString(), RequestBody.create(MEDIA_TYPE_JPG, f));
                builder.addFormDataPart("id", id);
                builder.setType(MultipartBody.FORM);
                Log.e("id: ", id);
                RequestBody requestBody = builder.build();
                final Request request = new Request.Builder().url(GlobalData.POST_IMAGE).post(requestBody).build();
                try {
                    Response response = client.newCall(request).execute();
                    String op = response.body().string();
                    if (op.contains("success") && networkavailable) {
                        Gson gson = new Gson();
                        userInfor = gson.fromJson(Utils.sendRequest(GlobalData.GET_USR_INFOR + "FamilyName=" + Utils.getValue(Setting_Activity.this, GlobalData.NAME)), UserInfor.class);
                        Utils.putValue(Setting_Activity.this, GlobalData.FAMILY_IMage, userInfor.getImage());
                    }
                    Log.e("responce: ", op);
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }).start();
    }
}
