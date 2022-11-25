package com.example.androidanime;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class DBAccess {
    public static final String ANIME_TABLE = "ANIME_TABLE";
    private static final String queryString = "SELECT * FROM " + ANIME_TABLE;

    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_IMG = "IMAGE_ID";
    public static final String COLUMN_TITLE = "ANIME_TITLE";
    public static final String COLUMN_EPOCH = "RELEASE_EPOCH";
    public static final String COLUMN_MAXEP = "MAX_EPISODES";
    public static final String COLUMN_FAV = "FAVOURITE";

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DBAccess instance;
    Cursor c = null;

    private static int getResID(String resName)
    {
        try{
            Field idField = R.drawable.class.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch(Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    private DBAccess(Context context)
    {
        this.openHelper = new DBHelper(context);
    }

    public static DBAccess getInstance(Context context)
    {
        if(instance == null)
            instance = new DBAccess(context);

        return instance;
    }

    public void open()
    {
        this.db = openHelper.getWritableDatabase();
    }

    public void close()
    {
        if(db!=null)
        {
            this.db.close();
        }
    }

    public ArrayList<AnimeInfo> GetAllAnime()
    {
        ArrayList<AnimeInfo> list = new ArrayList<AnimeInfo>();

        c = db.rawQuery(queryString, null);

        if(c.moveToFirst())
        {
            //loop through the cursor
            do{
                long epoch = c.getLong(3);
                boolean bFav = c.getInt(5) == 1 ? true : false;
                list.add(new AnimeInfo(c.getInt(0),getResID(c.getString(1)),c.getString(2), epoch, c.getInt(4), bFav));
            }while(c.moveToNext());
        }

        c.close();
        return list;
    }

    public ArrayList<AnimeInfo> GetFavAnime()
    {
        ArrayList<AnimeInfo> list = new ArrayList<>();

        c= db.rawQuery(queryString, null);
        if(c.moveToFirst())
        {
            //loop through the cursor
            do{
                long epoch = c.getLong(3);
                boolean bFav = c.getInt(5) == 1 ? true : false;

                if(bFav)
                    list.add(new AnimeInfo(c.getInt(0),getResID(c.getString(1)),c.getString(2), epoch, c.getInt(4), bFav));

            }while(c.moveToNext());
        }

        c.close();
        return list;
    }

    public void UpdateAnimeFavourites(int rowID, boolean inFav)
    {
        ContentValues cValue = new ContentValues();
        if(inFav)
            cValue.put(COLUMN_FAV, 1);
        else
            cValue.put(COLUMN_FAV,0);

        db.update(ANIME_TABLE, cValue, COLUMN_ID + " = " + String.valueOf(rowID),null);
    }

    public void RemoveAnimeFromFavourites(int rowID, boolean inFav)
    {
        ContentValues cValue = new ContentValues();
        cValue.put(COLUMN_FAV, 0);

        db.update(ANIME_TABLE, cValue, COLUMN_ID + " = " + String.valueOf(rowID),null);
    }
}
