package com.example.tsuscheduler;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tsuscheduler.DataBase.DbManager;
import com.example.tsuscheduler.DataBase.DrawerDbConstants;
import com.example.tsuscheduler.DataBase.DrawerDbManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity implements ScheduleParser.ParseListener {

    private RecyclerView recyclerView;
    //private ListView listView;
    private ArrayList<Schedule> timeTable;
    private ArrayList<String> drawerItems;
    private Toolbar toolbar;
    private ListView drawerList;
    private ArrayList<Schedule.ScheduleRowType> displayList;

    private ArrayList<Schedule.ScheduleRowType> rebuildTimeTable(ArrayList<Schedule> timeTable){
        ArrayList<Schedule.ScheduleRowType> displayList =new ArrayList<>();
        Schedule curItem;
        for(int i=0;i<timeTable.size();i++) {
            curItem = timeTable.get(i);
            displayList.add(new DateRowType(curItem.getDate()));
            for(int j=0; j<curItem.getTime().size();j++){
                displayList.add(new SubjectRowType(curItem.getTime().get(j),curItem.getContent().get(j)));
            }
        }
        return displayList;
    }

    @Override
    public void onPostParse(ArrayList<Schedule> data, String groupID) {
        timeTable = data;
        displayList =rebuildTimeTable(timeTable);

        recyclerView.setAdapter(new ScheduleAdapter(displayList));
        //listView.setAdapter(new NewScheduleAdapter(timeTable,this));
        //
        putStringToSharedPref("groupID",groupID);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                saveCacheToDB(groupID);
            }
        });
        thread.start();
    }

    private void putStringToSharedPref(String key, String value) {
        SharedPreferences sharedPref = this.getPreferences(this.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPref.edit();
        prefEditor.putString(key, value);
        prefEditor.apply();
    }

    private void saveCacheToDB(String groupID) {
        DbManager dbManager = new DbManager(this , groupID);
        dbManager.openDB();
        dbManager.upgradeTable();
        Schedule curDay;
        for (int i = 0; i < timeTable.size(); i++) {
            curDay = timeTable.get(i);
            for (int j = 0; j < curDay.getTime().size(); j++) {
                dbManager.insertToDb(curDay.getDate(),curDay.getTime().get(j),
                        curDay.getContent().get(j));
            }
        }
        dbManager.closeDb();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);

        recyclerView = findViewById(R.id.schedule_container);
        //listView = findViewById(R.id.schedule_container);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //
        if(savedInstanceState==null) {
            updateDrawerItems();

            String groupId = getIntent().getStringExtra("group");
            if(groupId==null) {
                SharedPreferences sharedPref = this.getPreferences(this.MODE_PRIVATE);
                groupId = sharedPref.getString("groupID","");
                if(groupId.equals("")){
                    Intent intent = new Intent(this,IntroductionActivity.class);
                    startActivity(intent);
                }
                else{
                    parseSchedule(groupId);
                    toolbar.setTitle(groupId);
                }
            }
            else{
                parseSchedule(groupId);
                toolbar.setTitle(groupId);
            }
        }
        else{
            timeTable = savedInstanceState.getParcelableArrayList("timeTable");
            recyclerView.setAdapter(new ScheduleAdapter(rebuildTimeTable(timeTable)));
            //listView.setAdapter(new NewScheduleAdapter(timeTable, this));
            //
            drawerItems = savedInstanceState.getStringArrayList("drawerItems");
        }

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        drawerList = findViewById(R.id.drawer_list);
        drawerList.setAdapter(new DrawerArrayAdapter(this, drawerItems));
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItemText = parent.getAdapter().getItem(position).toString();
                String addGroup = getString(R.string.drawer_add_a_group);
                if(clickedItemText.equals(addGroup)) {
                    Intent intent = new Intent(parent.getContext(),IntroductionActivity.class);
                    startActivity(intent);
                }
                else{
                    //- schedule will update even if current group was clicked
                    //- every time a group id in drawer clicked it's schedule rewrites to the db
                   parseSchedule(clickedItemText);
                   toolbar.setTitle(clickedItemText);
                   drawerLayout.closeDrawer(Gravity.LEFT);
                }

                //TODO: complete an implementation
            }
        });
    }

    private void parseSchedule(String group){
        ScheduleParser parser = new ScheduleParser(new WeakReference<>(this),group);
        parser.execute();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("timeTable", timeTable);
        outState.putStringArrayList("drawerItems",drawerItems);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void updateDrawerItems(){
        DrawerDbManager dbManager = new DrawerDbManager(this, DrawerDbConstants.GROUP_TABLE);
        dbManager.openDB();
        drawerItems = dbManager.getContent();
        dbManager.closeDB();
        drawerItems.add(getString(R.string.drawer_add_a_group));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_delete:{
                deleteGroup(toolbar.getTitle().toString());
                return true;
            }
            default:{
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void deleteGroup(String groupID){
        //update UI
        timeTable = new ArrayList<>();
        timeTable.add(new Schedule("Successfully deleted","",""));
        recyclerView.setAdapter(new ScheduleAdapter(rebuildTimeTable(timeTable)));
        //listView.setAdapter(new NewScheduleAdapter(timeTable, this));
        //
        toolbar.setTitle(getString(R.string.app_name));

        //delete sharedPref
        putStringToSharedPref("groupID", drawerItems.get(0));

        //delete from navigation drawer
        //DrawerDbManager dbManager = new DrawerDbManager(recyclerView.getContext(),DrawerDbConstants.GROUP_TABLE);
        DrawerDbManager dbManager = new DrawerDbManager(drawerList.getContext(),DrawerDbConstants.GROUP_TABLE);
        //
        dbManager.openDB();
        dbManager.deleteItemFromDb(groupID);
        dbManager.closeDB();
        updateDrawerItems();
        drawerList.setAdapter(new DrawerArrayAdapter(this, drawerItems));

        //delete from schedule_db.db
       Thread ScheduleDbRemover = new Thread(new Runnable() {
           @Override
           public void run() {
               //DbManager dbManager = new DbManager(recyclerView.getContext(),groupID);
               DbManager dbManager = new DbManager(drawerList.getContext(),groupID);
               //
               dbManager.openDB();
               dbManager.deleteTable();
               dbManager.closeDb();
           }
       });
       ScheduleDbRemover.start();
    }
}