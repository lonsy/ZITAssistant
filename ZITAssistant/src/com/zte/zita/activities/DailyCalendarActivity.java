package com.zte.zita.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.zte.zita.R;
import com.zte.zita.utils.ZDate;

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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.list, menu);
		return true;
	}

}
