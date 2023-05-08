package com.example.tsuscheduler.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DrawerDbManager {
    private DrawerDbHelper dbHelper;
    private SQLiteDatabase db;
    private String tableName;

    public DrawerDbManager(Context context, String tableName) {
        this.tableName = tableName;
        dbHelper = new DrawerDbHelper(context, tableName);
    }

    public void openDB() {
        db = dbHelper.getWritableDatabase();
        dbHelper.onCreate(db);
    }

    public void insertToDb(String content) {
        ContentValues cv = new ContentValues();
        cv.put(DrawerDbConstants.CONTENT, content);
        db.insert(tableName, null, cv);
    }

    public void deleteItemFromDb(String content){
        dbHelper.onDeleteItem(db, content);
    }

    public void closeDB() {
        db.close();
    }

    public ArrayList<String> getContent() {
        ArrayList<String> buff = new ArrayList<>();
        Cursor cursor = db.query(tableName, null, null, null,
                null, null, null);
        while(cursor.moveToNext()){
            buff.add(cursor.getString(cursor.getColumnIndexOrThrow(DrawerDbConstants.CONTENT)));
        }
        cursor.close();
        return buff;
    }
}
