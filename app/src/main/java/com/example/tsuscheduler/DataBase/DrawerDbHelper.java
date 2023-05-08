package com.example.tsuscheduler.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DrawerDbHelper extends SQLiteOpenHelper {
    private String DRAWER_TABLE_STRUCTURE;
    private String tableName;
    private String DROP_TABLE;
    private String DELETE_ITEM;

    public DrawerDbHelper(@Nullable Context context, String tableName) {
        super(context, DrawerDbConstants.DB_NAME, null, DrawerDbConstants.DB_VERSION);
        this.tableName = tableName;
        DRAWER_TABLE_STRUCTURE = "CREATE TABLE IF NOT EXISTS " + this.tableName +
                " (" + DrawerDbConstants._ID + " INTEGER PRIMARY KEY," +
                DrawerDbConstants.CONTENT + " TEXT, UNIQUE (" + DrawerDbConstants.CONTENT +
                ") ON CONFLICT REPLACE)";
        DROP_TABLE = "drop table if exists " + tableName;
        DELETE_ITEM = "DELETE FROM " + tableName + " WHERE " + DrawerDbConstants.CONTENT
                + " = '";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DRAWER_TABLE_STRUCTURE);
    }

    public void onDeleteItem(SQLiteDatabase db, String item){
        DELETE_ITEM += item + "'";
        db.execSQL(DELETE_ITEM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }
}
