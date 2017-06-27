package com.victor.myclient.Utils;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.victor.myclient.ActivityManage;

import demo.animen.com.xiaoyutask.R;

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

/**
 * Created by victor on 17-4-29.
 */

public class Utils {
    private static Toast mToast = null;

    public static void showLongToast(Context context, String msg) {
        if (mToast != null) {
            mToast.makeText(context, msg, Toast.LENGTH_LONG);
        } else {
            mToast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        }
        mToast.show();
    }

    public static void showShortToast(Context context, String msg) {

        mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        mToast.show();
    }

    public static void finishActivity(Activity activity) {
        ActivityManage activityManage = ActivityManage.getInstance();
        activityManage.popActivity(activity);
        activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    public static void startActivity(Activity activity, Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(activity, cls);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public static boolean isNetWorkAvailabe(Context context) {
        if (context.checkCallingOrSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager == null) {
                showShortToast(context, "不能得到系统网络服务");
            } else {
                NetworkInfo[] infos = connectivityManager.getAllNetworkInfo();
                if (infos != null) {
                    for (int i = 0; i < infos.length; i++) {
                        if (infos[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
        }
        showShortToast(context, "网络不可用");
        return false;
    }



    public static class Counter implements Runnable {
        private int time = 0;
        private Activity activity;
        private String message;

        public Counter(Activity activity, String message) {
            this.activity = activity;
            this.message = message;
        }

        public Counter(int time, Activity activity, String message) {
            this.time = time;
            this.activity = activity;
            this.message = message;
        }

        public Counter(int start) {
            this.time = start;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                time++;
                if (time > 10) {
                    showShortToast(activity, message);
                    activity.finish();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 移除SharedPreference
     *
     * @param context
     * @param key
     */
    public static final void RemoveValue(Context context, String key) {
        Editor editor = getSharedPreference(context).edit();
        editor.remove(key);
        boolean result = editor.commit();
        if (!result) {
            Log.e("移除Shared", "save " + key + " failed");
        }
    }

    private static final SharedPreferences getSharedPreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * 获取SharedPreference 值
     *
     * @param context
     * @param key
     * @return
     */
    public static final String getValue(Context context, String key) {
        return getSharedPreference(context).getString(key, "");
    }

    public static final Boolean getBooleanValue(Context context, String key) {
        return getSharedPreference(context).getBoolean(key, false);
    }

    public static final void putBooleanValue(Context context, String key,
                                             boolean bl) {
        Editor edit = getSharedPreference(context).edit();
        edit.putBoolean(key, bl);
        edit.commit();
    }

    public static final int getIntValue(Context context, String key) {
        return getSharedPreference(context).getInt(key, 0);
    }

    public static final long getLongValue(Context context, String key,
                                          long default_data) {
        return getSharedPreference(context).getLong(key, default_data);
    }

    public static final boolean putLongValue(Context context, String key,
                                             Long value) {
        Editor editor = getSharedPreference(context).edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    public static final Boolean hasValue(Context context, String key) {
        return getSharedPreference(context).contains(key);
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

    /**
     * 设置SharedPreference 值
     *
     * @param context
     * @param key
     * @param value
     */
    public static final boolean putValue(Context context, String key,
                                         String value) {
        value = value == null ? "" : value;
        Editor editor = getSharedPreference(context).edit();
        editor.putString(key, value);
        boolean result = editor.commit();
        if (!result) {
            return false;
        }
        return true;
    }

    /**
     * 设置SharedPreference 值
     *
     * @param context
     * @param key
     * @param value
     */
    public static final boolean putIntValue(Context context, String key,
                                            int value) {
        Editor editor = getSharedPreference(context).edit();
        editor.putInt(key, value);
        boolean result = editor.commit();
        if (!result) {
            return false;
        }
        return true;
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

    public static long parsedataNumber(long data_number, boolean isadd) {
        int year, month, day;
        day = (int) data_number % 100;
        month = (int) (data_number / 100) % 100;
        year = (int) (data_number / 10000);
        if (isadd) {
            if (isRun(year) && month == 2 && day >= 29) {
                day = 1;
                month++;
            } else if (month >= 1 && month <= 7 && month % 2 != 0 && day >= 32) {
                day = 1;
                month++;
                if (month >= 13) {
                    month = 1;
                    year++;
                }
            } else if (month >= 8 && month <= 12 && month % 2 == 0 && day >= 32) {
                day = 1;
                month++;
                if (month >= 13) {
                    month = 1;
                    year++;
                }
            } else if (month >= 1 && month <= 7 && month % 2 == 0 && day >= 31) {
                day = 1;
                month++;
                if (month >= 13) {
                    month = 1;
                    year++;
                }
            } else if (month >= 8 && month <= 12 && month % 2 != 0 && day >= 31) {
                day = 1;
                month++;
                if (month >= 13) {
                    month = 1;
                    year++;
                }
            }
        } else {
            if (isRun(year) && month == 3 && day <= 0) {
                day = 29;
                month--;
            } else if (month >= 1 && month <= 7 && month % 2 == 0 && day <= 0) {
                day = 31;
                month--;
            } else if (month >= 8 && month <= 12 && month % 2 != 0 && day <= 0) {
                day = 31;
                month--;
            } else if (month == 1 && day <= 0) {
                day = 31;
                month = 12;
                year--;
            } else if (month >= 1 && month <= 7 && month % 2 != 0 && day <= 0) {
                day = 30;
                month--;
                if (month <= 0) {
                    month = 12;
                    year--;
                }
            } else if (month >= 8 && month <= 12 && month % 2 == 0 && day <= 0) {
                day = 30;
                month--;
                if (month <= 0) {
                    month = 12;
                    year--;
                }
            }
        }

        return (long) (year * 10000 + month * 100 + day);
    }


    private static boolean isRun(int year) {
        return ((year % 400 == 0) || ((year % 4 == 0) && (year % 100 != 0)));
    }

    public static String dataTostringtem(Date date) {
        DateFormat format = new SimpleDateFormat("yyyyMMdd");
        String string = null;
        try {
            string = format.format(date);
        } catch (Exception e) {
            e.printStackTrace();
//            Log.d(TAG, "dateToString: dateToString failed");
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
//            Log.d(TAG, "dateToString: dateToString failed");
        }
        return string;
    }

    public static String dataToStringWithChinese(Date date) {
        DateFormat format = new SimpleDateFormat("yyyy年MM月dd");
        String string = null;
        try {
            string = format.format(date);
        } catch (Exception e) {
            e.printStackTrace();
//            Log.d(TAG, "dateToString: dateToString failed");
        }
        return string;
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
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }

    /**
     * 验证手机号
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(17[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }


    /**
     * 验证是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        java.util.regex.Matcher match = pattern.matcher(str);
        if (match.matches() == false) {
            return false;
        } else {
            return true;
        }
    }


}