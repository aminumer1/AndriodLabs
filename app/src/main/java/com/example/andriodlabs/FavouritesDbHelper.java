package com.example.andriodlabs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavouritesDbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "bbc_favourites.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE = "favourites";
    public static final String COL_ID = "_id";
    public static final String COL_TITLE = "title";
    public static final String COL_LINK = "link";
    public static final String COL_DESC = "description";
    public static final String COL_DATE = "pubDate";

    public FavouritesDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITLE + " TEXT NOT NULL, " +
                COL_LINK + " TEXT NOT NULL UNIQUE, " +
                COL_DESC + " TEXT, " +
                COL_DATE + " TEXT" +
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }
}
