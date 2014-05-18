package com.zte.zita.utils;

import java.util.ArrayList;
import java.util.HashMap;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.zte.zita.db.DailyDAO;
import com.zte.zita.entity.DailyEntity;
import com.zte.zita.entity.DayValueEntity;

public class ZDateTask extends AsyncTask<String, Void, ArrayList<HashMap<String, Object>>> {
	private static final String LOG_TAG = "ZDateTask";
	
	private String userNumber;
	
	private SQLiteDatabase database;
	
	private ZDateProcessListener caller;

	public ZDateTask(SQLiteDatabase database, String userNumber, ZDateProcessListener listener)
	{
		this.database = database;
		this.userNumber = userNumber;
		this.caller = listener;
	}
	
	@Override
	protected ArrayList<HashMap<String, Object>> doInBackground(String... params) {	
		ArrayList<HashMap<String, Object>> dayList = new ArrayList<HashMap<String, Object>>();
		for (int i=0; i<params.length; i++)
		{		
			String dayStr = params[i];
			DailyEntity daily = DailyDAO.getDaily(database, this.userNumber, dayStr);
			if (daily==null)
			{
				daily = new DailyEntity();
				daily.setDailyId(-1);
				daily.setDailyContent("");
				daily.setDailyDate(dayStr);
				daily.setDailyStatus(DayValueEntity.STATUS_NOT);
			}
			
			ZDate day = new ZDate(daily.getDailyDate());
        	boolean isWorkDay = day.isWorkDay();
        	boolean isBeforeToday = day.isBeforeToday();
        	int dailyStatus = daily.getDailyStatus();
        	
			HashMap<String, Object> map=new HashMap<String, Object>();
			//日志ID
			map.put("dailyId", daily.getDailyId());
			//日期字符串
        	map.put("dayTime", day.getDayString());
        	//日期
            map.put("day", DayValueEntity.generateNewEntity(isWorkDay, dailyStatus, day.getDayOfMonth(), isBeforeToday));
            //周几
            map.put("dayWeek", DayValueEntity.generateNewEntity(isWorkDay, dailyStatus, day.getDayForWeekString(), isBeforeToday));
            //日志内容
            map.put("dailyContent", daily.getDailyContent());
            //状态
            map.put("dailyStatus", DayValueEntity.generateNewEntity(isWorkDay, dailyStatus, dailyStatus, isBeforeToday));
            
            dayList.add(map);
		}

		return dayList;
	}

	@Override
	protected void onPostExecute(ArrayList<HashMap<String, Object>> result) {
		this.caller.onPostExecute(result);
	}
	
	public interface ZDateProcessListener {
		public void onPostExecute(ArrayList<HashMap<String, Object>> result);
	}
}
