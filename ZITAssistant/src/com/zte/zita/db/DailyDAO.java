package com.zte.zita.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.zte.zita.entity.DailyEntity;

/**
 * 日记数据操作类
 * @author lonsy
 */
public class DailyDAO {
	private static final String LOG_TAG = "DailyDAO";
	
	public static void addDaily(SQLiteDatabase database, DailyEntity daily)
	{		
		//插入新日记
		try {  
			ContentValues cv=new ContentValues(); 
            cv.put("created_by", daily.getCreatedBy());             
            cv.put("daily_status", daily.getDailyStatus());             
            cv.put("daily_date", daily.getDailyDate());
            cv.put("daily_content", daily.getDailyContent());  
            database.insertWithOnConflict(SQLiteHelper.TABLE_NAME_DAILY, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        } catch (Exception e) {
        	Log.e(LOG_TAG, "add Daily failed...");
            e.printStackTrace();  
        }
	}
	
	/**
	 * 根据创建人和日期获取日记
	 * @param database
	 * @param createdBy
	 * @param dailyDate
	 * @return 存在则返回日记实体，不存在则返回null
	 */
	public static DailyEntity getDaily(SQLiteDatabase database, String createdBy, String dailyDate)
	{
		StringBuffer condition = new StringBuffer();
		condition.append("created_by='").append(createdBy)
			.append("' and daily_date='").append(dailyDate).append("' and enable_flag='Y' ");
		
		Cursor cursor = database.query(SQLiteHelper.TABLE_NAME_DAILY, 
				new String[] {"daily_id", "created_by", "daily_status", "daily_date", "daily_content"},  //列
				condition.toString(), //条件
				null, //条件参数
				null, //groupBy 
				null, //having
				null //orderBy
				);
		
		if (cursor.moveToNext())
		{			
			DailyEntity daily = new DailyEntity();
			daily.setDailyId(cursor.getInt(0));
			daily.setCreatedBy(cursor.getString(1));
			daily.setDailyStatus(cursor.getInt(2));
			daily.setDailyDate(cursor.getString(3));
			daily.setDailyContent(cursor.getString(4));
			
			return daily;
		}
		
		return null;
	}
	
	/**
	 * 失效日记
	 * @param database
	 * @param dailyId
	 * @return 
	 */
	public static void disableDaily(SQLiteDatabase database, int dailyId)
	{
		try {  
			ContentValues cv=new ContentValues(); 
            cv.put("enable_flag", "N"); 
            database.update(SQLiteHelper.TABLE_NAME_DAILY, cv, "daily_id=" + dailyId, null);
        } catch (Exception e) {
        	Log.e(LOG_TAG, "disable Daily failed...");
            e.printStackTrace();  
        }
	}
	
	/**
	 * 更新日记
	 * @param database
	 * @param dailyId
	 * @return 
	 */
	public static void updateDaily(SQLiteDatabase database, DailyEntity daily)
	{
		try {  
			ContentValues cv=new ContentValues();
            cv.put("daily_content", daily.getDailyContent());
            cv.put("daily_status", daily.getDailyStatus());
            database.update(SQLiteHelper.TABLE_NAME_DAILY, cv, "daily_id=" + daily.getDailyId(), null);
            
            //更新最后更新日期
            StringBuffer update = new StringBuffer();
            update.append("update ").append(SQLiteHelper.TABLE_NAME_DAILY)
            	.append(" set last_update_date = datetime('now','localtime') where daily_id=")
            	.append(daily.getDailyId());
            database.execSQL(update.toString());
        } catch (Exception e) {
        	Log.e(LOG_TAG, "update Daily failed...");
            e.printStackTrace();  
        }
	}
}
