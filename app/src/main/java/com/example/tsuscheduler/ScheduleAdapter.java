package com.example.tsuscheduler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ScheduleAdapter extends RecyclerView.Adapter {

    private ArrayList<Schedule.ScheduleRowType> data;

    public ScheduleAdapter(ArrayList<Schedule.ScheduleRowType> data){
        this.data = data;
    }

    @Override
    public int getItemViewType(int position) {
       return data.get(position).getItemViewType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ScheduleViewHolderFactory.create(parent,viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        data.get(position).onBindViewHolder(holder);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

}
