package com.example.tsuscheduler;

import androidx.recyclerview.widget.RecyclerView;

import javax.xml.parsers.FactoryConfigurationError;

public class SubjectRowType implements Schedule.ScheduleRowType {
    private String time;
    private String description;

    public SubjectRowType(String time, String description){
        this.time = time;
        this.description = description;
    }


    @Override
    public int getItemViewType() {
        return Schedule.ScheduleRowType.SUBJECT_ROW_TYPE;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder) {
        ((ScheduleViewHolderFactory.SubjectViewHolder)holder).description.setText(description);
        ((ScheduleViewHolderFactory.SubjectViewHolder)holder).time.setText(time);
    }
}
