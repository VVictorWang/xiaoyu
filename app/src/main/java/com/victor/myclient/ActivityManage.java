package com.victor.myclient;

import android.app.Activity;

import java.util.Stack;

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
