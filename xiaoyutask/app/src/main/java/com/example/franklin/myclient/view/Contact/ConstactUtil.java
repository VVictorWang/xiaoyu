package com.example.franklin.myclient.view.Contact;

import android.content.Context;
import android.database.Cursor;

import java.util.HashMap;
import java.util.Map;

import com.example.franklin.myclient.DataBase.ContactDBhelper;

/**
 * Created by victor on 2017/4/23.
 */

public class ConstactUtil {
    /**
     * 获取所有数据
     *
     * @return
     */
    public static Map<String,String> getAllCallRecords(Context context) {
        Map<String,String> temp = new HashMap<String, String>();
        ContactDBhelper contactDBhelper = new ContactDBhelper(context);
        Cursor c = contactDBhelper.getAllItems();


        if (c.moveToFirst()) {
            do {
                String name = c.getString(c.getColumnIndex(ContactDBhelper.DB_COLUMN_NAME));
                String number = c.getString(c.getColumnIndex(ContactDBhelper.DB_COLUMN_XIAOYU));
                temp.put(name, number);
            } while (c.moveToNext());
        }
        c.close();
        return temp;
    }
}

