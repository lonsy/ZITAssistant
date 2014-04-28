package com.zte.zita.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "ZITAssistant.db";
	
	private static final int DATABASE_VERSION = 1;
	
	//table names
	public static final String TABLE_NAME_DAILY = "zita_daily";
	
	//create tables
	private static final String CREATE_TABLE_DAILY = "create table "+TABLE_NAME_DAILY+"(" +
			"daily_id INTEGER PRIMARY KEY AUTOINCREMENT " //ID
			+ ",creation_date TimeStamp NOT NULL DEFAULT (datetime('now','localtime')) " //创建日期
			+ ",created_by char(20) " //创建人 工号
			+ ",last_update_date TimeStamp NOT NULL DEFAULT (datetime('now','localtime')) " //最后更新日期
			+ ",enable_flag char(1) NOT NULL DEFAULT 'Y' " //是否有效 
			+ ",daily_status integer " //日志状态 1：草稿 2：已提交 
			+ ",daily_date date " //日志日期
			+ ",daily_content ntext " //日志内容
			+ "); ";
	
    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    	Log.i("start", "create tables");
    	db.execSQL(CREATE_TABLE_DAILY);
    	Log.i("end", "crate tables");
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//这样每次都会把所有表删除，重新创建，要判断新老版本的差别
		/*db.execSQL(DROP_THANKS_MESSAGE);
		db.execSQL(DROP_THANKS_IMG);
		db.execSQL(DROP_USER);
		this.onCreate(db);*/
	}

}
