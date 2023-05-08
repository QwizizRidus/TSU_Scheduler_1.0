package com.example.tsuscheduler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ScheduleViewHolderFactory {

    public static class DateViewHolder extends RecyclerView.ViewHolder {
        public TextView date;

        public DateViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
        }
    }

    public static class SubjectViewHolder extends RecyclerView.ViewHolder {
        public TextView time;
        public TextView description;

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            description = itemView.findViewById(R.id.description);
        }
    }

    public static RecyclerView.ViewHolder create(ViewGroup parent, int viewType) {
        switch (viewType) {
            case Schedule.ScheduleRowType.DATE_ROW_TYPE: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_row_type, parent, false);
                return new DateViewHolder(view);
            }
            case Schedule.ScheduleRowType.SUBJECT_ROW_TYPE: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_row_type, parent, false);
                return new SubjectViewHolder(view);
            }
            default: {
                return null;
            }
        }

    }


}