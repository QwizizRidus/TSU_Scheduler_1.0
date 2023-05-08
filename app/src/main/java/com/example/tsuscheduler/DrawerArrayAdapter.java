package com.example.tsuscheduler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DrawerArrayAdapter extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<String> values;

    public DrawerArrayAdapter(@NonNull Context context, ArrayList<String> values) {
        super(context, R.layout.drawer_row_layout, values);
        this.context = context;
        this.values = values;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.drawer_row_layout,parent,false);
        TextView textView = rowView.findViewById(R.id.drawer_list_text);
        ImageView imageView = rowView.findViewById(R.id.drawer_list_icon);
        textView.setText(values.get(position));
        String str = values.get(position);
        if(str.equals("Add a group")){
            imageView.setImageResource(R.drawable.ic_baseline_add_24);
        }
        else{
           imageView.setImageResource(R.drawable.ic_baseline_schedule_24);
        }

        return rowView;
    }
}
