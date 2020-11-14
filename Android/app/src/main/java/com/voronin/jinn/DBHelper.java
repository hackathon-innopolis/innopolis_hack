package com.voronin.jinn;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "wishDB";
    public static final String TABLE_WISHES = "wishes";
    public static final String TABLE_USERS = "users";
    public static final String KEY_PASS = "password";
    public static final String KEY_WISH_COUNT = "wishes";
    public static final String KEY_ID = "_id";
    public static final  String KEY_USER = "user";
    public static final String KEY_WISH = "wish";

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_WISHES + "(" + KEY_ID + " integer primary key," + KEY_USER + " text," + KEY_WISH + " text" + ")");
        db.execSQL("create table users(_id integer primary key,user text,password text,wishes integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop  table if exists " + TABLE_WISHES);
        db.execSQL("drop table if exists " + TABLE_USERS);
        onCreate(db);
    }
}
