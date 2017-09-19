package com.victor.myclient;

import android.app.Activity;
import android.content.Intent;

import java.util.Stack;

import demo.animen.com.xiaoyutask.R;

/**
 * Created by victor on 2017/5/25.
 */

public class ActivityManage {
    private static Stack<Activity> activityStack;
    private static ActivityManage instance;

    private ActivityManage() {

    }

    public static ActivityManage getInstance() {
        if (instance == null) {
            instance = new ActivityManage();
        }
        return instance;
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

    public static void startActivity(Activity activity, Intent intent) {
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public void popActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
            activityStack.remove(activity);
            activity = null;
        }
    }

    public void pushActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    public Activity getCurrentActivity() {
        Activity activity = null;
        if (!activityStack.isEmpty()) {
            activity = activityStack.lastElement();
        }
        return activity;
    }

    public void popAllActivity() {
        while (true) {
            Activity activity = getCurrentActivity();
            if (activity == null) {
                break;
            }
            popActivity(activity);
        }
    }
}
