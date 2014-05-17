package com.zte.zita.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

import com.zte.zita.R;

public class SplashActivity extends Activity {
	private final String LOG_TAG = "SplashActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		 new Handler().postDelayed(new Runnable() {  
	            public void run() {  
	                Intent mainIntent = new Intent(SplashActivity.this,  
	                        LoginActivity.class);  
	                SplashActivity.this.startActivity(mainIntent);  
	                SplashActivity.this.finish();  
	            }  
	  
	        }, 3000);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
