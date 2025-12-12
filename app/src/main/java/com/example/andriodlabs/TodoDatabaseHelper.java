package com.example.andriodlabs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TodoDatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "TodoDB";
    public static final int DB_VERSION = 1;

    public static final String TABLE_TODOS = "TODOS";
    public static final String COL_ID = "_id";
    public static final String COL_TEXT = "TEXT";
    public static final String COL_URGENT = "URGENT";

    public TodoDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_TODOS + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_TEXT + " TEXT, "
                + COL_URGENT + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODOS);
        onCreate(db);
    }
}
