package com.victor.myclient.SomeUtils;

/**
 * Created by victor on 17-5-16.
 */

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * 自定义的BitmapUtils,实现三级缓存
 */
public class MyBitmapUtils {
    private NetCacheUtils mNetCacheUtils;
    private LocalCacheUtils mLocalCacheUtils;
    private MemoryCacheUtils mMemoryCacheUtils;

    public MyBitmapUtils(){
        mMemoryCacheUtils=new MemoryCacheUtils();
        mLocalCacheUtils=new LocalCacheUtils();
        mNetCacheUtils=new NetCacheUtils(mLocalCacheUtils,mMemoryCacheUtils);
    }

    public void disPlay(ImageView ivPic, String url) {
//        ivPic.setImageResource(R.mipmap.pic_item_list_default);
        Bitmap bitmap;
        //内存缓存
        bitmap=mMemoryCacheUtils.getBitmapFromMemory(url);
        if (bitmap!=null){
            ivPic.setImageBitmap(bitmap);
            return;
        }

        //本地缓存
        bitmap = mLocalCacheUtils.getBitmapFromLocal(url);
        if(bitmap !=null){
            ivPic.setImageBitmap(bitmap);
            //从本地获取图片后,保存至内存中
            mMemoryCacheUtils.setBitmapToMemory(url,bitmap);
            return;
        }
        mNetCacheUtils.getBitmapFromNet(ivPic,url);
    }


}
