package com.example.franklin.myclient.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by victor on 2017/4/26.
 */

public class ContactDBhelper extends SQLiteOpenHelper {

  public static final String DATABASE_NAME = "contact_list.db";
  private static final int DATABASE_VERSION = 5;
  public static final String DB_TABLE_NAME = "contacts";
  public static final String DB_TABLE_RECORD_NAME = "recordlist";
  public static final String DB_COLUMN_ID = "_id";    //标识名
  public static final String DB_COLUMN_NAME = "name";   //代办事项的标题
  public static final String DB_COLUMN_NUMBER = "number";   //内容？ 手机号？
  public static final String DB_COLUMN_TIME = "time"; //最后一次通话时间
  public static final String DB_COLUMN_XIAOYU = "xiaoyu";   //小鱼号
  public static final String DB_COLUMN_IMAGE_URL = "image_url";    //头像地址
  public static final String DB_COLUMN_STATUS = "status";    //最近通话状况
  private String CREATE = "CREATE TABLE " + DB_TABLE_NAME + "(" +
      DB_COLUMN_ID + " INTEGER PRIMARY KEY, " +
      DB_COLUMN_NAME + " text, " +
      DB_COLUMN_NUMBER + " TEXT, " +
      DB_COLUMN_TIME + " LONG, " + DB_COLUMN_XIAOYU + " text, " + DB_COLUMN_IMAGE_URL + " text, "
      + DB_COLUMN_STATUS + " integer)";


  private String CREATE_RECORD = "CREATE TABLE " + DB_TABLE_RECORD_NAME + "(" +
      DB_COLUMN_ID + " INTEGER PRIMARY KEY, " +
      DB_COLUMN_NUMBER + " TEXT, " +
      DB_COLUMN_XIAOYU +" text, "+
      DB_COLUMN_TIME + " LONG, "
      + DB_COLUMN_IMAGE_URL+" text, "+
      DB_COLUMN_NAME+" text, "
      + DB_COLUMN_STATUS + " integer)";

  public ContactDBhelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(CREATE);
    db.execSQL(CREATE_RECORD);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_NAME);
    db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_RECORD_NAME);
    onCreate(db);
  }

  public void updata(SQLiteDatabase db) {
    db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_RECORD_NAME);
    db.execSQL(CREATE_RECORD);
  }

  public long insertRecordList(String number,String xiaoyu, String name, long time, String imageUrl,int status) {
    SQLiteDatabase db = getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(DB_COLUMN_NUMBER, number);
    values.put(DB_COLUMN_TIME, time);
    values.put(DB_COLUMN_XIAOYU,xiaoyu);
    values.put(DB_COLUMN_STATUS, status);
    values.put(DB_COLUMN_IMAGE_URL,imageUrl);
    values.put(DB_COLUMN_NAME,name);
    return db.insert(DB_TABLE_RECORD_NAME, null, values);
  }

  public long insert(String name, String xiaoyu_number
     ) {
    SQLiteDatabase db = getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(DB_COLUMN_NAME, name);
    values.put(DB_COLUMN_XIAOYU, xiaoyu_number);
    return db.insert(DB_TABLE_NAME, null, values);
  }

  //更新通话时间
  public boolean updateTime(Integer id, long time, int status) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(DB_COLUMN_TIME, time);
    values.put(DB_COLUMN_STATUS, status);
    db.update(DB_TABLE_NAME, values, DB_COLUMN_ID + " = ? ",
        new String[]{Integer.toString(id)});
    return true;
  }


  //根据id来得到cursor
  public Cursor getItem(int id) {
    SQLiteDatabase db = this.getReadableDatabase();
    return db.rawQuery("SELECT * FROM " + DB_TABLE_NAME + " WHERE " +
        DB_COLUMN_ID + " = ? ", new String[]{Integer.toString(id)});
  }

  public Cursor getRecordListItem(int id) {
    SQLiteDatabase db = this.getReadableDatabase();
    return db.rawQuery("SELECT * FROM " + DB_TABLE_RECORD_NAME + " WHERE " + DB_COLUMN_ID + " = ? ",
        new String[]{Integer.toBinaryString(id)});
  }

  //根据创建时间的倒序得到cursor
  public Cursor getAllItems() {
    SQLiteDatabase db = this.getReadableDatabase();
    return db.rawQuery("SELECT * FROM " + DB_TABLE_NAME, null);
  }

  //获取所有的通话记录
  public Cursor getAllRecordItems(){
    SQLiteDatabase db=this.getReadableDatabase();
    return db.rawQuery("SELECT * FROM "+DB_TABLE_RECORD_NAME,null);
  }
  public Integer deleteItem(Integer id) {
    SQLiteDatabase db = this.getWritableDatabase();
    return db.delete(DB_TABLE_NAME, DB_COLUMN_ID + " = ? ",
        new String[]{Integer.toString(id)});
  }

  public Integer deleteRecordItem(Integer id){
    SQLiteDatabase db = this.getWritableDatabase();
    return db.delete(DB_TABLE_RECORD_NAME, DB_COLUMN_ID + " = ? ",
        new String[]{Integer.toString(id)});
  }
  public boolean isEmpty() {
    return getAllItems().getCount() == 0;
  }
  public boolean isRecordEmpty(){
    return 0==getAllRecordItems().getCount();
  }
}
