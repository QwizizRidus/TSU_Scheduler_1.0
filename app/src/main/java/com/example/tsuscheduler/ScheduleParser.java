package com.example.tsuscheduler;


import android.content.Context;
import android.os.AsyncTask;


import com.example.tsuscheduler.DataBase.DbManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;


    public class ScheduleParser extends AsyncTask<String, String, String> {

        private Document doc;
        private ArrayList<Schedule> timeTable = new ArrayList<>();
        private WeakReference<ParseListener> parseListener;
        private String groupID;


        public interface ParseListener {
            void onPostParse(ArrayList<Schedule> data, String groupID);
        }

        public ScheduleParser(WeakReference<ParseListener> context, String groupID) {
            parseListener = context;
            this.groupID = groupID;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                doc = Jsoup.connect("http://schedule.tsu.tula.ru").data("group", groupID).timeout(30000).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            parseSchedule();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            parseListener.get().onPostParse(timeTable, groupID);
        }

        private void parseSchedule() {
            if(doc == null) {
                getCacheFromDB();
                return;
            }
            Elements ttElements = doc.getElementsByClass("tt");

            if (ttElements.size() == 0) {
                getCacheFromDB();
                return;
            }
            Element tbodyScheduleElement = ttElements.get(1).child(0);
            Elements trElements = tbodyScheduleElement.children();

            Schedule scheduleBuff = new Schedule();

            Element curTrElement = trElements.get(0);
            scheduleBuff.setDate(curTrElement.text());

            for (int i = 1; i < trElements.size(); i++) {
                curTrElement = trElements.get(i);
                if (curTrElement.childrenSize() == 1) {
                    timeTable.add(scheduleBuff);
                    scheduleBuff = new Schedule();
                    scheduleBuff.setDate(curTrElement.text());
                } else {
                    scheduleBuff.addTime(curTrElement.getElementsByClass("time").text());


                    scheduleBuff.addContent(curTrElement.getElementsByClass("disc").text()
                    + "\n>>" + curTrElement.getElementsByClass("aud").text());
                }
            }
            timeTable.add(scheduleBuff);
        }



        private void getCacheFromDB() {
            DbManager dbManager = new DbManager((Context)parseListener.get(),groupID);
            dbManager.openDB();
            timeTable = dbManager.getTimeTable();
            dbManager.closeDb();
        }



    }


