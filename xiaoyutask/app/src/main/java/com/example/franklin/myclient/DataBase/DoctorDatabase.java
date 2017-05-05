//package com.example.franklin.myclient.DataBase;
//
//import android.content.ContentValues;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
///**
// * Created by victor on 17-5-4.
// */
//
//public class DoctorDatabase extends SQLiteOpenHelper {
//    /**
//     * id:医生id
//     * phone:医生电话
//     * name:医生姓名
//     * mail:医生邮箱
//     * sex: 医生性别
//     * age: 医生年龄
//     * image:医生头像  前应加http://ip/upload/doctorimage/
//     * 例如http://139.196.40.97/upload/doctorimage/8525f03f5354d4386440947594872b1a0eb059de.jpg
//     * hospital:医生所在医院
//     * job_title:医生职称 （主任医生、主治医生....）
//     * good_at : 擅长领域
//     * cases：医生介绍
//     * creationDate:添加时间
//     */
//    public static final String DATABASE_NAME = "doctor_infor.db";
//    private static final int DATABASE_VERSION = 1;
//    public static final String DB_TABLE_NAME = "doctor";
//    public static final String DB_COLUMN_DOCTOR_ID = "id";    //标识名
//    public static final String DB_COLUMN_DOCTOR_NAME = "name";   //代办事项的标题
//    public static final String DB_COLUMN_DOCTOR_PHONE = "number";   //内容？ 手机号？
//    public static final String DB_COLUMN_DOCTOR_MAIL = "mail"; //最后一次通话时间
//    public static final String DB_COLUMN_DOCTOR_SEX = "sex";   //小鱼号
//    public static final String DB_COLUMN_IMAGE_URL = "image_url";    //头像地址
//    public static final String DB_COLUMN_DOCTOR_AGE = "age";    //最近通话状况
//    public static final String DB_COLUMN_DOCTOR_HOSTIPTAL = "hospital";
//    public static final String DB_COLUMN_DOCTOR_JOB = "job";
//    public static final String DB_COLUMN_DOCTOR_CASES = "cases";
//    public static final String DB_COLUMN_DOCTOR_CRETIONDATA = "data";
//    private static final String CREAT = "create table " + DB_TABLE_NAME + " ( " + DB_COLUMN_DOCTOR_ID + "varchar primary key, " + DB_COLUMN_DOCTOR_NAME + " text,"
//            + DB_COLUMN_DOCTOR_PHONE + " text, " + DB_COLUMN_DOCTOR_MAIL + " text, " + DB_COLUMN_DOCTOR_SEX + " varchar, " + DB_COLUMN_IMAGE_URL + " text, "
//            + DB_COLUMN_DOCTOR_AGE + " varchar, " + DB_COLUMN_DOCTOR_HOSTIPTAL + " text, " + DB_COLUMN_DOCTOR_JOB + " text, " + DB_COLUMN_DOCTOR_CASES + " text, " + DB_COLUMN_DOCTOR_CRETIONDATA + " text)";
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(CREAT);
//
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("drop table if it exists " + DB_TABLE_NAME);
//        onCreate(db);
//    }
//
//    public void insert(String id, String name, String number, String mail, String sex, String age, String hospital, String job, String cases, String data) {
//        SQLiteDatabase db = getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(DB_COLUMN_DOCTOR_ID, id);
//        values.put(DB_COLUMN_DOCTOR_NAME, name);
//        values.put(DB_COLUMN_DOCTOR_PHONE, number);
//        values.put(DB_COLUMN_DOCTOR_MAIL, mail);
//        values.put(DB_COLUMN_DOCTOR_SEX, sex);
//        values.put(DB_COLUMN_DOCTOR_AGE, age);
//        values.put(DB_COLUMN_DOCTOR_HOSTIPTAL, hospital);
//
//    }
//
//}
