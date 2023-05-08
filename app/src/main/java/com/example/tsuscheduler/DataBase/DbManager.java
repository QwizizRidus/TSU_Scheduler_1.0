package com.example.tsuscheduler.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.tsuscheduler.Schedule;

import java.util.ArrayList;
import java.util.Currency;

public class DbManager {
    private DbHelper dbHelper;
    private SQLiteDatabase db;
    private String tableName;

    public DbManager(Context context, String groupID) {
        tableName = "group_"+groupID;
        dbHelper = new DbHelper(context, tableName);
    }


    public void openDB() {
        db = dbHelper.getWritableDatabase();
        dbHelper.onCreate(db);
    }

    public void deleteTable(){
        dbHelper.onDeleteTable(db);
    }

    public void upgradeTable(){
        dbHelper.onUpgrade(db,0,0);
    }

    public void insertToDb(String date, String time, String subject) {
        ContentValues cv = new ContentValues();
        cv.put(DbConstants.DATE, date);
        cv.put(DbConstants.TIME, time);
        cv.put(DbConstants.SUBJECT, subject);
        db.insert(tableName, null, cv);
    }

    public ArrayList<Schedule> getTimeTable() {
        Schedule tempSchedule = new Schedule();
        ArrayList<Schedule> timeTable = new ArrayList<>();
        Cursor cursor = db.query(tableName, null, null, null, null,
                null, null);
        if(cursor.getCount()==0){
            tempSchedule.setDate("404");
            tempSchedule.addTime("Can't load schedule\n");
            tempSchedule.addContent("Server is not available or wrong group ID entered\n");
            timeTable.add(tempSchedule);
            return timeTable;
        }
        String buff;
        cursor.moveToNext();
        String curDate = cursor.getString(cursor.getColumnIndexOrThrow(DbConstants.DATE));
        tempSchedule.setDate(curDate);
        tempSchedule.addTime(cursor.getString(cursor.getColumnIndexOrThrow(DbConstants.TIME)));
        tempSchedule.addContent(cursor.getString(cursor.getColumnIndexOrThrow(DbConstants.SUBJECT)));

        while(cursor.moveToNext()){
            buff = cursor.getString(cursor.getColumnIndexOrThrow(DbConstants.DATE));
            if(!curDate.equals(buff)){
                timeTable.add(tempSchedule);
                tempSchedule = new Schedule();
                tempSchedule.setDate(buff);
                curDate = buff;
            }
            tempSchedule.addTime(cursor.getString(cursor.getColumnIndexOrThrow(DbConstants.TIME)));
            tempSchedule.addContent(cursor.getString(cursor.getColumnIndexOrThrow(DbConstants.SUBJECT)));
        }
        timeTable.add(tempSchedule);
        cursor.close();
        return timeTable;
    }

    public void closeDb (){
        dbHelper.close();
    }

}