package com.example.tsuscheduler;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Schedule implements Parcelable {

    private String Date;
    private ArrayList<String> time = new ArrayList<>();
    private ArrayList<String> content = new ArrayList<>();

    public interface ScheduleRowType{
        int DATE_ROW_TYPE =0;
        int SUBJECT_ROW_TYPE = 1;

        int getItemViewType();
        void onBindViewHolder(RecyclerView.ViewHolder holder);
    }

    public Schedule(String Date, ArrayList<String> time, ArrayList<String> content){
        this.time = time;
        this.content = content;
        this.Date = Date;
    }

    public Schedule(){};
    public Schedule(String notification1, String notification2, String notification3){
        this.Date = notification1;
        addTime(notification2);
        addContent(notification3);
    }

    public void setDate(String date) {
        Date = date;
    }

    public void addTime(String time){
        this.time.add(time);
    }

    public void addContent(String content){
        this.content.add(content);
    }

    public ArrayList<String> getContent() {
        return content;
    }

    public ArrayList<String> getTime() {
        return time;
    }

    public String getDate() {
        return Date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Date);
        dest.writeList(this.time);
        dest.writeList(this.content);

    }
}

