package com.wildanokt.searchmoviemade.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.wildanokt.searchmoviemade.entity.Favorite;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.wildanokt.searchmoviemade.db.DatabaseContract.FavoriteCollums.IMAGE_PATH;
import static com.wildanokt.searchmoviemade.db.DatabaseContract.FavoriteCollums.OVERVIEW;
import static com.wildanokt.searchmoviemade.db.DatabaseContract.FavoriteCollums.TITLE;
import static com.wildanokt.searchmoviemade.db.DatabaseContract.TABLE_NAME;

public class MovieHelper {
    private static String DATABASE_TABLE = TABLE_NAME;
    private Context context;
    private DatabaseHelper helper;

    private SQLiteDatabase database;

    public MovieHelper(Context context) {
        this.context = context;
    }

    public MovieHelper open() throws SQLException{
        helper = new DatabaseHelper(context);
        database = helper.getWritableDatabase();
        return this;
    }
    public void close(){
        helper.close();
    }
    public ArrayList<Favorite> query(){
        ArrayList<Favorite> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                _ID + " DESC",
                null
        );
        cursor.moveToFirst();

        Favorite favorite;
        if (cursor.getCount()>0){
            do {
                favorite = new Favorite();
                favorite.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                favorite.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                favorite.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));
                favorite.setImgPath(cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_PATH)));

                arrayList.add(favorite);
                cursor.moveToNext();
            }while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }
    public long insert(Favorite favorite){
        ContentValues initialValues = new ContentValues();
        initialValues.put(TITLE, favorite.getTitle());
        initialValues.put(OVERVIEW, favorite.getOverview());
        initialValues.put(IMAGE_PATH, favorite.getImgPath());

        return database.insert(DATABASE_TABLE, null, initialValues);
    }
    public int delete(int id){
        return database.delete(TABLE_NAME, _ID+" = '"+id+"'", null);
    }

    //provider
    public Cursor queryByIdProvider(String id){
        return database.query(DATABASE_TABLE,
                null,
                _ID+" = ?",
                new String[]{id},
                null,
                null,
                null,
                null
        );
    }

    public Cursor queryProvider(){
        return database.query(DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                _ID +" DESC"
        );
    }

    public long insertProvider(ContentValues values){
        return database.insert(DATABASE_TABLE, null, values);
    }
    public int updateProvider(String id,ContentValues values){
        return database.update(DATABASE_TABLE,values,_ID +" = ?",new String[]{id} );
    }
    public int deleteProvider(String id){
        return database.delete(DATABASE_TABLE, _ID+" = ?", new String[]{id});
    }
}
