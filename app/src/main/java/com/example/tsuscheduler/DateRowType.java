package com.example.tsuscheduler;

import androidx.recyclerview.widget.RecyclerView;

public class DateRowType implements Schedule.ScheduleRowType {
    private String date;

    public DateRowType(String date){
        this.date = date;
    }

    @Override
    public int getItemViewType() {
        return Schedule.ScheduleRowType.DATE_ROW_TYPE;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder) {
        ((ScheduleViewHolderFactory.DateViewHolder)holder).date.setText(date);
    }
}
