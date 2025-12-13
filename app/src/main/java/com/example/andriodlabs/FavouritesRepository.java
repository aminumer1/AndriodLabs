package com.example.andriodlabs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class FavouritesRepository {
    private final FavouritesDbHelper helper;

    public FavouritesRepository(Context ctx) {
        helper = new FavouritesDbHelper(ctx);
    }

    public boolean isFavourite(String link) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(FavouritesDbHelper.TABLE,
                new String[]{FavouritesDbHelper.COL_LINK},
                FavouritesDbHelper.COL_LINK + "=?",
                new String[]{link},
                null, null, null);
        boolean exists = c.moveToFirst();
        c.close();
        return exists;
    }

    public boolean add(Article a) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FavouritesDbHelper.COL_TITLE, a.title);
        cv.put(FavouritesDbHelper.COL_LINK, a.link);
        cv.put(FavouritesDbHelper.COL_DESC, a.description);
        cv.put(FavouritesDbHelper.COL_DATE, a.pubDate);

        long r = db.insertWithOnConflict(
                FavouritesDbHelper.TABLE,
                null,
                cv,
                SQLiteDatabase.CONFLICT_IGNORE
        );
        return r != -1;
    }

    public boolean deleteByLink(String link) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int rows = db.delete(FavouritesDbHelper.TABLE,
                FavouritesDbHelper.COL_LINK + "=?",
                new String[]{link});
        return rows > 0;
    }

    public List<Article> getAll() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(FavouritesDbHelper.TABLE,
                new String[]{
                        FavouritesDbHelper.COL_TITLE,
                        FavouritesDbHelper.COL_LINK,
                        FavouritesDbHelper.COL_DESC,
                        FavouritesDbHelper.COL_DATE
                },
                null, null, null, null,
                FavouritesDbHelper.COL_ID + " DESC");

        List<Article> out = new ArrayList<>();
        while (c.moveToNext()) {
            out.add(new Article(
                    c.getString(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3)
            ));
        }
        c.close();
        return out;
    }
}
