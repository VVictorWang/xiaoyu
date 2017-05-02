package com.example.franklin.myclient.view.Case.CaseLayout;

/**
 * Created by 小武哥 on 2017/4/23.
 */

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class DisplayUtils {

  private DisplayUtils() {

  }

  private static float scale = 0.0f;
  private static float screenWidth=0.0f;
  private static float screenHeight=0.0f;
  public static void init(Context context) {
    scale = context.getResources().getDisplayMetrics().density;

    DisplayMetrics dm = new DisplayMetrics();
    ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
    screenWidth=dm.widthPixels ;
    screenHeight=dm.heightPixels;
  }

  public static int dpToPx(float dipValue) {
    return (int) (dipValue * scale + 0.5);
  }

  public static float pxToDp(float pxValue) {
    return pxValue / scale;
  }
  public static int getScreenWidth(){
    return (int) screenWidth;
  }

  public static int getScreenHeight(){
    return (int) screenHeight;
  }

}