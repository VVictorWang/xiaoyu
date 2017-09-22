package com.victor.myclient.utils;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import demo.animen.com.xiaoyutask.R;

/**
 * Created by victor on 17-4-29.
 */

public class Utils {

    private static Toast mToast = null;

    public static void showShortToast(Context context, String msg) {
        if (mToast != null) {
            mToast.setText(msg);
            mToast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public static String createAcacheKey(Object... param) {
        String key = "";
        for (Object o : param) {
            key += "-" + o;
        }
        return key.replaceFirst("-", "");
    }

    public static void showImage(Context context, String imageUrl, ImageView imageView) {
        Glide.with(context).
                load(imageUrl).
                fitCenter().
                error(R.drawable.image_load_error).
                into(imageView);
    }

    public static boolean isNetWorkAvailabe(Context context) {
        if (context.checkCallingOrSelfPermission(Manifest.permission.INTERNET) != PackageManager
                .PERMISSION_GRANTED) {
            return false;
        } else {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager == null) {
                showShortToast(context, "不能得到系统网络服务");
            } else {
                NetworkInfo[] infos = connectivityManager.getAllNetworkInfo();
                if (infos != null) {
                    for (int i = 0; i < infos.length; i++) {
                        if (infos[i].getState() == NetworkInfo.State.CONNECTED && infos[i]
                                .isAvailable()) {
                            return true;
                        }
                    }
                }
            }
        }
        showShortToast(context, "网络不可用");
        return false;
    }


    public static String sendRequest(String myurl) {
        HttpURLConnection connection = null;
        StringBuilder builder = new StringBuilder();
        try {
            URL url = new URL(myurl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(8000);
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (Exception p) {
            p.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }

        }
        return builder.toString();
    }


    //将字符串转成日期格式
    public static Date stringToDate(String str) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            // Fri Feb 24 00:00:00 CST 2012
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String dataTostringtem(Date date) {
        DateFormat format = new SimpleDateFormat("yyyyMMdd");
        String string = null;
        try {
            string = format.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return string;
    }

    public static String dateToString(Date date) {
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        String string = null;
        try {
            string = format.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return string;
    }


    public static String DateToStringWithChinese(Date date) {
        DateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        String result = null;
        try {
            result = format.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Date stringToDateWithChinese(String str) {
        DateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = null;
        try {
            // Fri Feb 24 00:00:00 CST 2012
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 验证邮箱
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(" +
                "([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }

    /**
     * 验证手机号
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(17[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

}