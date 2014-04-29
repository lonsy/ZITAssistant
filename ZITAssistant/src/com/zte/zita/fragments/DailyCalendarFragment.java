package com.zte.zita.fragments;

import com.zte.zita.R;
import com.zte.zita.activities.DailyDetailActivity;
import com.zte.zita.activities.DailyListActivity;
import com.zte.zita.activities.LoginActivity;
import com.zte.zita.entity.DayValueEntity;
import com.zte.zita.utils.PreferenceUtil;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class DailyCalendarFragment extends Fragment {
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_daily_calendar, container, false);
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//设置此Fragment拥有菜单，否则接收不到点击事件
        setHasOptionsMenu(true);   
	}
	
	/**
	 * 菜单渲染
	 */
	@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.calendar, menu);
    }
    
    @Override  
    public boolean onOptionsItemSelected(MenuItem menuItem) {  
        switch (menuItem.getItemId()) {  
        	case R.id.ic_add:  
        		Intent intent = new Intent(this.getActivity(), DailyDetailActivity.class);
        		intent.putExtra("dailyStatus", DayValueEntity.STATUS_NOT);
        		intent.putExtra("currentDateString", "");
        		intent.putExtra("dailyId", -1);
        		this.startActivityForResult(intent, 0);  
        		break;  
        	case R.id.ic_list: 
        		Intent intent1 = new Intent(this.getActivity(), DailyListActivity.class);
    			this.startActivity(intent1);
        		break;    
        	case R.id.ic_logout:  
        		OnClickListener okListener = new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {		        		
		    			PreferenceUtil.setStringPre(getActivity(), PreferenceUtil.USER_NUMBER, null);
		        		Intent intent2 = new Intent(getActivity(), LoginActivity.class);
		    			startActivity(intent2);
					}        			
        		};
        		ZDialogFragment mdf = new ZDialogFragment("提示", "确定要注销吗", okListener, null);
        		FragmentTransaction ft = getFragmentManager().beginTransaction();
        		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        		mdf.show(ft, "df");
        		break;   
        	default:  
        		break;  
        }  
        return true;  
    }  
}
