package com.zte.zita.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

import com.zte.zita.R;
import com.zte.zita.fragments.DailyDetailFragment;

public class DailyDetailActivity extends Activity {
	
	private DailyDetailFragment details;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//如果是横向的，则销毁当前Activity
		/*if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			finish();
			return;
		}*/
		details = new DailyDetailFragment();

		if (savedInstanceState == null)
		{
			details.setArguments(this.getIntent().getExtras());
			this.getFragmentManager().beginTransaction()
				.add(android.R.id.content, details).commit();
		}
	}

    //点击其他地方隐藏键盘
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (DailyDetailActivity.this.getCurrentFocus() != null) {
				if (DailyDetailActivity.this.getCurrentFocus().getWindowToken() != null) {
					InputMethodManager imm= (InputMethodManager) getSystemService(DailyDetailActivity.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(DailyDetailActivity.this
							.getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}
		}
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.daily_detail, menu);
		return true;
	}
	
	@Override
	public void onStart() {		
		super.onStart();
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}
	
	@Override 
	public boolean onKeyDown(int keyCode, KeyEvent event) {         
		//按下键盘上返回按钮 不让其返回登录页面
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			this.details.onClick(null);
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}

}
