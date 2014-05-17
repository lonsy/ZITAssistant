package com.zte.zita.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.zte.zita.R;
import com.zte.zita.fragments.DailyCalendarFragment;

public class DailyCalendarActivity extends Activity {
	private final String LOG_TAG = "DailyCalendarActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar);
		
		// 标题栏设置
		ActionBar bar = getActionBar();
		bar.setCustomView(R.layout.actionbar_layout);
		((TextView)this.findViewById(R.id.actionbar_title)).setText("日历");
		
		View v = this.findViewById(R.id.daily_calendar_fragment);
		DailyCalendarFragment f = new DailyCalendarFragment();
		//v.setOnTouchListener(f);
		FragmentTransaction t = getFragmentManager().beginTransaction();
		t.add(R.id.daily_calendar_fragment, f).commit();
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
