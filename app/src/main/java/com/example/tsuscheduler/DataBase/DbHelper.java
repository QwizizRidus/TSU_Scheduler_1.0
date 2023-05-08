package com.example.tsuscheduler.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    private String tableName;
    private String SCHEDULE_TABLE_STRUCTURE;
    private String DROP_TABLE = "drop table if exists ";

    public DbHelper(@Nullable Context context, String tableName) {
        super(context, DbConstants.DB_NAME, null, DbConstants.DB_VERSION);
        this.tableName = tableName;
        SCHEDULE_TABLE_STRUCTURE = "CREATE TABLE IF NOT EXISTS " +
                this.tableName + " (" + DbConstants._ID + " INTEGER PRIMARY KEY," + DbConstants.DATE +
                " TEXT," + DbConstants.TIME + " TEXT," + DbConstants.SUBJECT + " TEXT)";
        DROP_TABLE += tableName;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SCHEDULE_TABLE_STRUCTURE);
    }

    public void onDeleteTable(SQLiteDatabase db){
        db.execSQL(DROP_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

}
