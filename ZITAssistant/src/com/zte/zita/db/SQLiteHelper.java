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
			+ ",creation_date TimeStamp NOT NULL DEFAULT (datetime('now','localtime')) " //��������
			+ ",created_by char(20) " //������ ����
			+ ",last_update_date TimeStamp NOT NULL DEFAULT (datetime('now','localtime')) " //����������
			+ ",enable_flag char(1) NOT NULL DEFAULT 'Y' " //�Ƿ���Ч 
			+ ",daily_status integer " //��־״̬ 1���ݸ� 2�����ύ 
			+ ",daily_date date " //��־����
			+ ",daily_content ntext " //��־����
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
		//����ÿ�ζ�������б�ɾ�������´�����Ҫ�ж����ϰ汾�Ĳ��
		/*db.execSQL(DROP_THANKS_MESSAGE);
		db.execSQL(DROP_THANKS_IMG);
		db.execSQL(DROP_USER);
		this.onCreate(db);*/
	}

}
