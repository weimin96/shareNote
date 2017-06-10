package com.aoliao.notebook.utils.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 你的奥利奥 on 2017/2/22.
 */

public class NoteDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "note.db";
    private static final int VERSION = 1;
    public static final String TABLE_NAME = "note";
    public static final String RECYCLE_TABLE = "recycle";
    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public static final String TIME = "time";

    public NoteDB(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "("
                + ID + " integer primary key autoincrement, "
                + TITLE + " text not null,"
                + CONTENT + " text not null,"
                + TIME + " text not null)");
        db.execSQL("create table " + RECYCLE_TABLE + "("
                + "newId" + " integer primary key autoincrement, "
                + ID + " integer not null,"
                + TITLE + " text not null,"
                + CONTENT + " text not null,"
                + TIME + " text not null)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
