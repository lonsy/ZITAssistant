package com.zte.zita.activities;

import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zte.zita.R;
import com.zte.zita.utils.PreferenceUtil;
import com.zte.zita.utils.WebDataProcessListener;
import com.zte.zita.utils.WebDataTask;

public class LoginActivity extends Activity implements WebDataProcessListener {
	private final String LOG_TAG = "LoginActivity";
	
	/**
	 * 可输入的最大字符数
	 */
	private final int MAX_CHAR = 15;
	
	/**
	 * 进度提示框
	 */
	private ProgressDialog processDialog = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		//设置密码输入时候的回车响应
		((EditText)this.findViewById(R.id.login_et_password)).setOnEditorActionListener(new EditText.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_GO){
					login(v);
				}
				return false;
			}
		});
		
		((CheckBox)this.findViewById(R.id.login_cb_showpd)).setOnCheckedChangeListener(new OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
            	EditText passwordText = (EditText)findViewById(R.id.login_et_password);
                if(arg0.isChecked()){
                    //设置EditText的密码为可见的
                	passwordText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    //设置密码为隐藏的
                	passwordText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
            
        });
		
		String userNumber = PreferenceUtil.getStringPre(this, PreferenceUtil.USER_NUMBER, null);
		if (null != userNumber)
		{
			Intent intent = new Intent(this, DailyListActivity.class);
			this.startActivity(intent);
			this.finish();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (LoginActivity.this.getCurrentFocus() != null) {
				if (LoginActivity.this.getCurrentFocus().getWindowToken() != null) {
					InputMethodManager imm= (InputMethodManager) getSystemService(LoginActivity.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(LoginActivity.this
							.getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}
		}
		return super.onTouchEvent(event);
	}
	
	/**
	 * 取消登录，退出程序
	 * @param v
	 */
	public void cancelLogin(View v)
	{
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	/**
	 * 登录
	 * @param v
	 */
	public void login(View v)
	{
		//校验用户名和密码
		StringBuffer textError = new StringBuffer();
		boolean hasError = false;
		EditText usernameText = (EditText) this.findViewById(R.id.login_et_username);
		String username = usernameText.getText().toString();
		if (username.length() < 1) {
			textError.append(this.getString(R.string.username_hint));
			hasError = true;
		}
		if (!hasError && username.length() > this.MAX_CHAR)
		{
			textError.append(this.getString(R.string.username))
				.append(this.getString(R.string.text_too_long_error1))
				.append(this.MAX_CHAR)
				.append(this.getString(R.string.text_too_long_error2));
			hasError = true;
		}
		
		EditText passwordText = (EditText) this.findViewById(R.id.login_et_password);
		String password = passwordText.getText().toString();
		if (!hasError && password.length() < 1) {
			textError.append(this.getString(R.string.password_hint));
			hasError = true;
		}
		if (!hasError && password.length() > this.MAX_CHAR)
		{
			textError.append(this.getString(R.string.password_text))
				.append(this.getString(R.string.text_too_long_error1))
				.append(this.MAX_CHAR)
				.append(this.getString(R.string.text_too_long_error2));
			hasError = true;
		}
		
		if (hasError)
		{
			Toast toast = Toast.makeText(this, textError.toString(), Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();
			return;
		}
		
		//登录
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			StringBuffer url = new StringBuffer();
			url.append("https://210.21.236.181/access/baseRpcAuthorize?username=")
				.append(username).append("&password=").append(password);
			//url.append("https://210.21.236.181/access/baseRpcAuthorize?username=10146358&password=Abcd123456");
			
			//显示等待提示
			this.processDialog = new ProgressDialog(this);
			this.processDialog.setMessage("正在登录，请稍候...");
			this.processDialog.show();
	        
			new WebDataTask(this).execute(url.toString());
		} else {
			Toast toast = Toast.makeText(this, "网络连接不可用", Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();
		}
		
	}

	//网络访问返回方法，登录结果
	@Override
	public void onPostExecute(String result) {
		Log.v(LOG_TAG, result);
		
		//取消进度提示
		if (this.processDialog!=null && this.processDialog.isShowing())
		{
			this.processDialog.dismiss();
			this.processDialog = null;
		}
		
		if ("0".equals(result))
		{
			Toast toast = Toast.makeText(this, "服务器连接失败，请稍后重试!", Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();
			return;
		}
		
		Gson gson = new Gson();
		Map<String, Object> rs = gson.fromJson(result, new TypeToken<Map<String, Object>>() {}.getType());
		String resultCode = rs.get("result").toString();
		String tokenStr = null;
		if (null!=rs.get("token"))
		{
			tokenStr = rs.get("token").toString();
		}
		if ("1".equals(resultCode) && null!=tokenStr && !"".equals(tokenStr))
		{
			String userName = rs.get("name").toString();
			String userNumber = rs.get("empidUi").toString();
			Log.v(LOG_TAG, "userName:" + userName + " userNumber:" + userNumber);
			
			PreferenceUtil.setStringPre(this, PreferenceUtil.USER_NUMBER, userNumber);
			
			Intent intent = new Intent(this, DailyListActivity.class);
			this.startActivity(intent);
		}
		else
		{
			Toast toast = Toast.makeText(this, "用户名或密码错误，请重新输入!", Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();
			return;
		}
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
