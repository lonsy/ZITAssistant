package com.zte.zita.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.TextView;

import com.zte.zita.R;
import com.zte.zita.utils.ZDate;

public class DailyListActivity extends Activity {
	private final String LOG_TAG = "DailyListActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		
		// 标题栏设置
		ActionBar bar = getActionBar();
		bar.setCustomView(R.layout.actionbar_layout);
		ZDate currentDate = new ZDate();
		((TextView)this.findViewById(R.id.actionbar_title)).setText(currentDate.getMonthString() + " " + currentDate.getYear());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.list, menu);
		return true;
	}
	
	@Override 
	public boolean onKeyDown(int keyCode, KeyEvent event) {         
		//按下键盘上返回按钮 不让其返回登录页面
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			Intent MyIntent = new Intent(Intent.ACTION_MAIN);
			MyIntent.addCategory(Intent.CATEGORY_HOME);
			startActivity(MyIntent);
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}

}
