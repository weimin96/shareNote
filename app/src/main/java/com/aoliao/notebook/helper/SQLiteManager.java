package com.aoliao.notebook.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

/**
 * 应用所需的数据库操作
 */
public class SQLiteManager extends SQLiteOpenHelper {


    // 所有的静态变量
    // 数据库版本
    private static final int DATABASE_VERSION = 1;

    // 数据库名称

    // 表名
    private static final String TABLE_LOGIN = "login";

    // 列名
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_UID = "uid";
    private static final String KEY_CREATED_AT = "created_at";

    public SQLiteManager(Context context) {
        super(context, Config.DATABASE_NAME, null, DATABASE_VERSION);
    }

    // 创建表
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + Config.TABLE_NAME_LOGIN + "("
                + Config.USERS_UID + " INTEGER PRIMARY KEY,"
//                + Config.USERS_USERNAME + " TEXT,"
//                + Config.USERS_PHONE + " TEXT UNIQUE,"
//                + Config.USERS_UUID + " TEXT,"
//                + Config.USERS_CREATE_AT + " TEXT" + ")";
                + Config.USERS_USERNAME + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);
    }

    // 更新表
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 如果表存在则删除表
        db.execSQL("DROP TABLE IF EXISTS " + Config.TABLE_NAME_LOGIN);

        // 重新创建表
        onCreate(db);
    }

    /**
     * Storing user details in database
     */
    public void addUser(String username) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Config.USERS_USERNAME, username); // 名字
//        values.put(Config.USERS_PHONE, phone); // 手机号
//        values.put(Config.USERS_UUID, uuid); // 唯一id,用于标示用户
//        values.put(Config.USERS_CREATE_AT, created_at); // 创建时间

        db.close();
    }

    /**
     * 从数据库获取用户信息
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        String selectQuery = "SELECT  * FROM " + Config.TABLE_NAME_LOGIN;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put(Config.USERS_USERNAME, cursor.getString(1));
//            user.put(Config.USERS_PHONE, cursor.getString(2));
//            user.put(Config.USERS_UUID, cursor.getString(3));
//            user.put(Config.USERS_CREATE_AT, cursor.getString(4));
        }
        cursor.close();
        db.close();
        return user;
    }

    /**
     * 获取用户的登陆状态,如果表中没有数据说明用户没有登陆
     */
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + Config.TABLE_NAME_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        // return row count
        return rowCount;
    }

    /**
     * 从数据库中删除所有信息
     */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // 删除所有列
        db.delete(Config.TABLE_NAME_LOGIN, null, null);
        db.close();
    }

}
