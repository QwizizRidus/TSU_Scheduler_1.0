package com.example.tsuscheduler;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tsuscheduler.DataBase.DbManager;
import com.example.tsuscheduler.DataBase.DrawerDbConstants;
import com.example.tsuscheduler.DataBase.DrawerDbManager;

public class IntroductionActivity extends AppCompatActivity {

    private EditText editText;
    private Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        Toolbar toolbar = findViewById(R.id.intro_toolbar);
        setSupportActionBar(toolbar);

        button=findViewById(R.id.find_group_btn);
        editText = findViewById(R.id.group_id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),MainActivity.class);
                intent.putExtra("group",editText.getText().toString());
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        addContentToBD();
                    }
                });
                thread.start();
                startActivity(intent);
            }
        });
    }

    private void addContentToBD(){
        DrawerDbManager dbManager = new DrawerDbManager(this, DrawerDbConstants.GROUP_TABLE);
        dbManager.openDB();
        dbManager.insertToDb(editText.getText().toString());
        dbManager.closeDB();
    }
}
