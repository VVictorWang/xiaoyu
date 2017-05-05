package com.example.franklin.myclient.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by victor on 17-5-2.
 */

public class CaseListDataBase extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "case_list.db";
    private static final int DATABASE_VERSION = 1;
    public static final String DB_TABLE_NAME = "cases";
    public static final String DB_COLUMN_ID = "_id";    //标识名
    public static final String DB_COLUNM_PATIETENT_ID = "patient_id";

    public static final String DB_COLUMN_PATIENTEN_NAME = "name";   //患者姓名
    public static final String DB_COLUMN_DOCTOR_NAME = "doctor_name";//医生姓名
    public static final String DB_COLUMN_CREATE_TIME = "time"; //病例创建时间
    public static final String DB_COLUMN_DOCTOR_ID = "doctor_id";   //医生id
    public static final String DB_COLUMN_TEMPERATURE = "temperature";    //头像地址
    public static final String DB_COLUMN_BLODD_PRESSURE = "blood_pressure";    //最近通话状况
    public static final String DB_COLUMN_ILL_PROBLEM = "ill_problem"; //病情描述
    public static final String DB_COLUMN_ILL_RESULT = "ill_result";
    private String CREATE = "CREATE TABLE " + DB_TABLE_NAME + "(" +
            DB_COLUMN_ID + " INTEGER PRIMARY KEY, " + DB_COLUNM_PATIETENT_ID
            + " text, " +
            DB_COLUMN_PATIENTEN_NAME + " text, " +
            DB_COLUMN_DOCTOR_NAME + " TEXT, " +
            DB_COLUMN_CREATE_TIME + " text, " + DB_COLUMN_DOCTOR_ID + " text, " + DB_COLUMN_TEMPERATURE + " text, "
            + DB_COLUMN_BLODD_PRESSURE + " text, " + DB_COLUMN_ILL_PROBLEM + " text, "+DB_COLUMN_ILL_RESULT+" text)";

    public CaseListDataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_NAME);
        onCreate(db);
    }

    public void update(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_NAME);
        onCreate(db);
    }
    public void insert(int id, String name, String createData, String patient_id, String doctor_id, String ill_problem, String ill_result, String temperature, String blood_pressure, String doctor_name) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DB_COLUMN_ID,id);
        contentValues.put(DB_COLUMN_PATIENTEN_NAME, name);
        contentValues.put(DB_COLUMN_CREATE_TIME, createData);
        contentValues.put(DB_COLUNM_PATIETENT_ID, patient_id);
        contentValues.put(DB_COLUMN_DOCTOR_ID, doctor_id);
        contentValues.put(DB_COLUMN_ILL_PROBLEM, ill_problem);
        contentValues.put(DB_COLUMN_TEMPERATURE, temperature);
        contentValues.put(DB_COLUMN_BLODD_PRESSURE, blood_pressure);
        contentValues.put(DB_COLUMN_ILL_RESULT, ill_result);
        contentValues.put(DB_COLUMN_DOCTOR_NAME, doctor_name);
        db.insert(DB_TABLE_NAME,null, contentValues);
    }
    public Cursor getAllItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + DB_TABLE_NAME, null);
    }

    public Cursor getItemByid(int id) {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery("select * from " + DB_TABLE_NAME + " where " + DB_COLUMN_ID + " = ? ", new String[]{Integer.toString(id)});
    }
    public boolean isEmpty(){
        return getAllItems().getCount() == 0;
    }
}
