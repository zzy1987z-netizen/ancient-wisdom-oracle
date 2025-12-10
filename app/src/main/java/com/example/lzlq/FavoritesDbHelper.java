package com.example.lzlq;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoritesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "lzlq_favorites.db";
    // 数据库升级：版本从 1 -> 2
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_FAVORITES = "favorites";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_SIGN_ID = "sign_id";
    // 新增列：签的属性 (上签, 下签等)
    public static final String COLUMN_SIGN_ATTRIBUTE = "sign_attribute";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_FAVORITES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_SIGN_ID + " INTEGER UNIQUE NOT NULL, " +
                    COLUMN_SIGN_ATTRIBUTE + " TEXT NOT NULL" +
                    ");";

    public FavoritesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // 从版本1升级到版本2，为旧表添加新列，而不是删除重建
            db.execSQL("ALTER TABLE " + TABLE_FAVORITES + " ADD COLUMN " + COLUMN_SIGN_ATTRIBUTE + " TEXT DEFAULT '中平签' NOT NULL");
        }
    }

    // 新的 addFavorite 方法，包含签的属性
    public void addFavorite(int signId, String attribute) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SIGN_ID, signId);
        values.put(COLUMN_SIGN_ATTRIBUTE, attribute);
        db.insert(TABLE_FAVORITES, null, values);
    }

    public void removeFavorite(int signId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITES, COLUMN_SIGN_ID + " = ?", new String[]{String.valueOf(signId)});
    }

    public boolean isFavorite(int signId) {
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.query(TABLE_FAVORITES, new String[]{COLUMN_SIGN_ID},
                COLUMN_SIGN_ID + " = ?", new String[]{String.valueOf(signId)}, null, null, null)) {
            return cursor.getCount() > 0;
        }
    }

    // 获取所有收藏，返回一个Cursor，这是最高效的方式
    public Cursor getAllFavorites() {
        SQLiteDatabase db = this.getReadableDatabase();
        // 查询所有列，并按ID降序排列 (最新的在最前)
        return db.query(TABLE_FAVORITES, 
                new String[]{COLUMN_ID, COLUMN_SIGN_ID, COLUMN_SIGN_ATTRIBUTE},
                null, null, null, null, COLUMN_ID + " DESC");
    }
}
