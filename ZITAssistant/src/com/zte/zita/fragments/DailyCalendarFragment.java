package com.zte.zita.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;

import com.zte.zita.R;
import com.zte.zita.activities.DailyDetailActivity;
import com.zte.zita.activities.DailyListActivity;
import com.zte.zita.activities.LoginActivity;
import com.zte.zita.db.SQLiteHelper;
import com.zte.zita.entity.DayValueEntity;
import com.zte.zita.layout.CalendarCard;
import com.zte.zita.utils.PreferenceUtil;
import com.zte.zita.utils.ZDate;
import com.zte.zita.utils.ZDateTask;

public class DailyCalendarFragment extends Fragment {
		
	private ArrayList<ArrayList<HashMap<String, Object>>> dataList;
	
	private String userNumber;
	
	private SQLiteDatabase database = null;
	
	private ZDate startDate;
	
	private ZDate endDate;
	
	private ListView listView;
	
	private CalendarAdapter calendarAdapter;
	
	/**
	 * 进度提示框
	 */
	private ProgressDialog processDialog = null;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_daily_calendar, container, false);
		
		this.userNumber = PreferenceUtil.getStringPre(this.getActivity(), PreferenceUtil.USER_NUMBER, null);
		
		if (this.database==null)
		{
			this.database = new SQLiteHelper(this.getActivity()).getWritableDatabase();
		}
        
        this.dataList = new ArrayList<ArrayList<HashMap<String, Object>>>();
        
        this.startDate = ZDate.getCurrentMonthStartDay();
        this.endDate = null;
        
        this.listView = (ListView)v.findViewById(R.id.list_calendar_lv);
        this.calendarAdapter = new CalendarAdapter(this.getActivity());
        this.listView.setAdapter(this.calendarAdapter);
        this.listView.setOnScrollListener(this.onScrollListener);

        initDate(true, true);
        initDate(true, false);
        initDate(true, false);
        initDate(false, false);
        
        return v;
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
	
	public void getDate(boolean isBottom)
	{
		initDate(isBottom, true);
	}	
	
	/**
	 * 加载数据
	 * @param isBottom 是否往下
	 */
	private void initDate(boolean isBottom, boolean needLoading)
	{
		if (needLoading)
		{
			this.processDialog = new ProgressDialog(this.getActivity());
			this.processDialog.setMessage("加载中...");
			this.processDialog.show();
		}
		
		ZDate date = null;
		if (this.endDate == null)
		{
			date = this.startDate.clone();
			endDate = startDate.clone();
		}
		else
		{			
			if (isBottom)
			{
				date = this.endDate.addMonth(1);
				endDate = endDate.addMonth(1);
			}
			else
			{
				date = this.startDate.addMonth(-1);
				startDate = startDate.addMonth(-1);
			}
		}
		int daysCount = date.getMonthDaysCount();
		String[] dayStrs = new String[daysCount];
			dayStrs[0] = date.getDayString();
	        for (int i = 1; i < daysCount; i++) {
	        	dayStrs[i] = date.add(i).getDayString();
	        }
		
		if (isBottom)
		{
	    	new ZDateTask(this.database, this.userNumber, new ZDateTask.ZDateProcessListener() {				
				@Override
				public void onPostExecute(ArrayList<HashMap<String, Object>> result) {
					if (result != null)
					{
						dataList.add(result);
						calendarAdapter.notifyDataSetChanged();	
					}
					if (processDialog != null)
					{
						processDialog.cancel();
					}
				}
			}).execute(dayStrs);
		}
		else
		{
	    	new ZDateTask(this.database, this.userNumber, new ZDateTask.ZDateProcessListener() {				
				@Override
				public void onPostExecute(ArrayList<HashMap<String, Object>> result) {
					if (result != null)
					{
						dataList.add(0, result);
						calendarAdapter.notifyDataSetChanged();	
						
						listView.setSelection(1);
					}
					if (processDialog != null)
					{
						processDialog.cancel();
					}
				}
			}).execute(dayStrs);
		}
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
    
    //滚动监听
    private OnScrollListener onScrollListener = new OnScrollListener() {  
        private boolean isBottom = false;   //是否到达底端  
        
        private boolean isTop = false;   //是否到达顶端
        //listview的状态发送改变时执行  
        @Override  
        public void onScrollStateChanged(AbsListView view, int scrollState) {  
            if(isBottom && scrollState==OnScrollListener.SCROLL_STATE_IDLE){  
            	getDate(true);
                
                isBottom = false;  
            }  
            
            if(isTop && scrollState==OnScrollListener.SCROLL_STATE_IDLE){  
            	getDate(false);
                
                isTop = false;  
            } 
        }  
        
        //在滚动的过程中不断执行  
        @Override  
        public void onScroll(AbsListView view, int firstVisibleItem,  
                int visibleItemCount, int totalItemCount) {  
        	isTop = false; 
        	isBottom = false;  
            if(firstVisibleItem + visibleItemCount == totalItemCount){  
                isBottom = true;  
            }
            else
            {                 
                if (firstVisibleItem==0)
                {
                	isTop = true;
                }
            }
        }  
    };  
    
    public class CalendarAdapter extends BaseAdapter {  
        private LayoutInflater mInflater;// 动态布局映射  
  
        public CalendarAdapter(Context context) {  
            this.mInflater = LayoutInflater.from(context);  
        }  
  
        // 决定ListView有几行可见  
        @Override  
        public int getCount() {  
            return dataList.size();// ListView的条目数  
        }  
  
        @Override  
        public Object getItem(int arg0) {  
            return null;  
        }  
  
        @Override  
        public long getItemId(int arg0) {  
            return 0;  
        }  
  
        @Override  
        public View getView(int position, View convertView, ViewGroup parent) {  
            convertView = mInflater.inflate(R.layout.calendar_view, null);//根据布局文件实例化view  
            CalendarCard card = (CalendarCard)convertView;
            card.init(dataList.get(position), DailyCalendarFragment.this);
            
            return convertView;  
        }  
    }  

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==0)
		{
			if(resultCode==Activity.RESULT_OK)
			{
				Object day = data.getExtras().get("currentDateString");
				if (day!=null)
				{
					//直接重启Activity，待完善为更新部分UI
					/*String dayString = day.toString();
					Log.v("", dayString);*/
					Intent intent = this.getActivity().getIntent();
					this.getActivity().finish();
					this.getActivity().startActivity(intent);
				}
			}
		}		
	}
}
