package com.zte.zita.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

import com.zte.zita.R;
import com.zte.zita.activities.DailyCalendarActivity;
import com.zte.zita.activities.DailyDetailActivity;
import com.zte.zita.activities.LoginActivity;
import com.zte.zita.db.SQLiteHelper;
import com.zte.zita.entity.DayValueEntity;
import com.zte.zita.utils.PreferenceUtil;
import com.zte.zita.utils.ZDate;
import com.zte.zita.utils.ZDateTask;

public class DailyListFragment extends Fragment {
	private final String LOG_TAG = "DailyListFragment";
	
	/**
	 * 起始日期
	 */
	private ZDate dayStart;
	
	/**
	 * 结束日期
	 */
	private ZDate dayEnd;
	
	private ArrayList<HashMap<String, Object>> dayList;
	
	private SimpleAdapter dayAdapter;
	
	private String userNumber;
	
	private SQLiteDatabase database = null;
	
	private ListView listView;
	
	/**
	 * 明细碎片是否可见
	 */
	//private boolean hasDetailFragment; 
	
	/**
	 * 当前显示的明细位置
	 */
	private int currentDetailPosition;
	
	/**
	 * ListView的当前显示位置
	 */
	private int currentViewPosition = 0;
	
	private String lastDateString = "";
	
	/**
	 * 新建日记返回的日期是否在List范围内
	 */
	private boolean hasDetailInList = false;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_daily_list, container, false);
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		//判断明细碎片是否可见
		//View detailsFrame = this.getActivity().findViewById(R.id.daily_detail_fragment);
		//this.hasDetailFragment = detailsFrame!=null && detailsFrame.getVisibility()==View.VISIBLE;
		
		this.userNumber = PreferenceUtil.getStringPre(this.getActivity(), PreferenceUtil.USER_NUMBER, null);
		
		//初始化起始日期，取当前周的第一天
		this.dayStart = ZDate.getCurrentWeekStartDay();
		this.dayEnd = this.dayStart.add(-1);
		
		this.dayList = new ArrayList<HashMap<String, Object>>();
		
		if (this.database==null)
		{
			this.database = new SQLiteHelper(this.getActivity()).getWritableDatabase();
		}

		this.listView = (ListView)this.getActivity().findViewById(R.id.list_lv);
		dayAdapter = new SimpleAdapter(this.getActivity(),  
				dayList,  
                R.layout.view_daily_as_day_layout,  
                new String[]{"day","dayWeek", "dailyContent", "dailyStatus"},  
                new int[]{R.id.view_daily_tw_day, R.id.view_daily_tw_day_week, R.id.view_daily_tw_content, R.id.view_daily_iv_status}); 
		dayAdapter.setViewBinder(viewBinder);
		listView.setAdapter(dayAdapter);
		listView.setOnScrollListener(onScrollListener);
		
		this.listView.setSelection(0);
		
		//监听listview的点击事件
		this.listView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				/*ListView list = (ListView)parent;
				@SuppressWarnings("unchecked")
				HashMap<String, Object> map = (HashMap<String, Object>)list.getItemAtPosition(position);
				Log.v(LOG_TAG, map.get("dayTime").toString());
				
				//启动日记显示
				DayValueEntity entity = (DayValueEntity)map.get("dailyStatus");
				if (entity.getDailyStatus() == DayValueEntity.STATUS_NOT)
				{
					//没有日记，则新建
					Intent intent = new Intent(getActivity(), DailyEditorActivity.class);
					intent.putExtra("currentDateString", map.get("dayTime").toString());
					startActivity(intent);
				}*/
				showDetails(position);
			}			
		});
		
		this.loadDate(true);
		
		if (savedInstanceState != null) {
			this.currentDetailPosition = savedInstanceState.getInt("currentChoice", 0);
		}
		
		/*if (this.hasDetailFragment) {
			this.listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			showDetails(this.currentDetailPosition);
		}
		else
		{
			Log.v(null, "details in invisibe!");
		}*/
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("currentChoice", this.currentDetailPosition);
	}
	
	private void showDetails(int index) {
		this.currentDetailPosition = index;
		
		HashMap<String, Object> map = (HashMap<String, Object>)this.listView.getItemAtPosition(index);
		
		/*if (this.hasDetailFragment) {
			this.listView.setItemChecked(index, true);
			
			DailyDetailFragment details = (DailyDetailFragment)this.getFragmentManager().findFragmentById(R.id.daily_detail_fragment);
			
			details = DailyDetailFragment.newInstance(map.get("dayTime").toString());
			
			FragmentTransaction ft = this.getFragmentManager().beginTransaction();
			if (index == 0)
			{
				ft.replace(R.id.daily_detail_fragment, details);
			}
			else
			{
				ft.replace(R.id.daily_detail_fragment, details);
			}
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.commit();
		}*/ 
		DayValueEntity entity = (DayValueEntity)map.get("dailyStatus");
		if (entity.getDailyStatus() == DayValueEntity.STATUS_NOT)
		{
			//没有日记，则新建
			Intent intent = new Intent(getActivity(), DailyDetailActivity.class);
			intent.putExtra("currentDateString", map.get("dayTime").toString());
			startActivityForResult(intent, 0);
		}
		else
		{
			Intent intent = new Intent();
			intent.setClass(getActivity(), DailyDetailActivity.class);
			intent.putExtra("currentDateString", map.get("dayTime").toString());
			intent.putExtra("dailyStatus", entity.getDailyStatus());
			intent.putExtra("dailyContent", map.get("dailyContent").toString());
			intent.putExtra("dailyId", map.get("dailyId").toString());
			startActivityForResult(intent, 0);
		}
	}
	
	/**
	 * 加载数据
	 * @param isBottom 是否从底部
	 */
	private void loadDate(boolean isBottom)
	{
		String[] dayStrs = new String[14];
		if (isBottom)
		{
	        for (int i = 0; i < 14; i++) {
	        	ZDate tmpDate = this.dayEnd.add(1);
	        	dayStrs[i] = tmpDate.getDayString();
	        	this.dayEnd = tmpDate;
	        }
	    	
	    	new ZDateTask(this.database, this.userNumber, new ZDateTask.ZDateProcessListener() {				
				@Override
				public void onPostExecute(ArrayList<HashMap<String, Object>> result) {
					Iterator<HashMap<String, Object>> i = result.iterator();
					while (i.hasNext())
					{
						dayList.add((HashMap<String, Object>)i.next());
					}

	                dayAdapter.notifyDataSetChanged();
					setTitle();
				}
			}).execute(dayStrs);
		}
		else
		{
			for (int i=0; i<14; i++)
			{
	        	ZDate tmpDate = this.dayStart.add(-1);
	        	dayStrs[i] = tmpDate.getDayString();
	        	this.dayStart = tmpDate;
			}
	    	
	    	new ZDateTask(this.database, this.userNumber, new ZDateTask.ZDateProcessListener() {				
				@Override
				public void onPostExecute(ArrayList<HashMap<String, Object>> result) {
					Iterator<HashMap<String, Object>> i = result.iterator();
					while (i.hasNext())
					{
						dayList.add(0, (HashMap<String, Object>)i.next());
					}
	                dayAdapter.notifyDataSetChanged();
	        		listView.setSelection(13);
	        		currentViewPosition = 13;
					setTitle();
				}
			}).execute(dayStrs);
		}
	}
	
	//每个数据项的数据填充
	private ViewBinder viewBinder = new ViewBinder() {          
        public boolean setViewValue(View view, Object data,  
                String textRepresentation) {   
        	//日期与周几的渲染
        	if (view.getId() == R.id.view_daily_tw_day || view.getId() == R.id.view_daily_tw_day_week)
        	{
            	DayValueEntity day = (DayValueEntity)data;
            	TextView dayView = (TextView)view;
        		if (day.isWorkDay())
        		{
        			dayView.setTextColor(Color.parseColor("#000000"));
        		}
        		else
        		{
        			dayView.setTextColor(Color.parseColor("#CCCCCC"));
        		}
        		dayView.setText(day.getDayValue().toString());
        		
        		return true;
        	}
        	//状态三角形渲染
        	else if (view.getId() == R.id.view_daily_iv_status)
            {            	
            	DayValueEntity day = (DayValueEntity)data;
        		ImageView statusView = (ImageView)view;
        		//工作日、今天之前或有日志的非工作日才显示三角形
        		if ((day.isWorkDay() && day.isBeforeToday()) || day.getDailyStatus()==DayValueEntity.STATUS_DONE
        				 || day.getDailyStatus()==DayValueEntity.STATUS_SAVE)
        		{
        			if (DayValueEntity.STATUS_NOT == day.getDailyStatus())
        			{
                		statusView.setBackgroundResource(R.drawable.ic_daily_not);
        			}
        			else if (DayValueEntity.STATUS_SAVE == day.getDailyStatus())
        			{
                		statusView.setBackgroundResource(R.drawable.ic_daily_save);
        			}
        			else if (DayValueEntity.STATUS_DONE == day.getDailyStatus())
        			{
                		statusView.setBackgroundResource(R.drawable.ic_daily_done);
        			}
        		}
        		else
        		{
        			//空白需要设置，否则滚动容易没有刷新
            		statusView.setBackgroundResource(0);
        		}
            	
            	return true;
            }
            return false;  
        }  
    }; 
	
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
        //super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.list, menu);
    }
    
    //滚动监听
    private OnScrollListener onScrollListener = new OnScrollListener() {  
        private boolean isBottom = false;   //是否到达底端  
        
        private boolean isTop = false;   //是否到达顶端
        //listview的状态发送改变时执行  
        @Override  
        public void onScrollStateChanged(AbsListView view, int scrollState) {  
            if(isBottom && scrollState==OnScrollListener.SCROLL_STATE_IDLE){  
            	loadDate(true);
                
                isBottom = false;  
            }  
            else
            {
				setTitle();
            }
            
            if(isTop && scrollState==OnScrollListener.SCROLL_STATE_IDLE){  
            	loadDate(false);
                
                isTop = false;  
            } 
            else
            {
				setTitle();
            }
        }  
        
        //在滚动的过程中不断执行  
        @Override  
        public void onScroll(AbsListView view, int firstVisibleItem,  
                int visibleItemCount, int totalItemCount) {  
            if(firstVisibleItem + visibleItemCount == totalItemCount){  
                isBottom = true;  
            }
            else
            {  
            	isBottom = false; 
            }
            
            if (firstVisibleItem==0)
            {
            	isTop = true;
            }
            else
            {  
            	isTop = false; 
            }
            
            currentViewPosition = firstVisibleItem;
        }  
    };  
    
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
        	case R.id.ic_calendar:  
        		Intent intent1 = new Intent(this.getActivity(), DailyCalendarActivity.class);
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==0)
		{
			if(resultCode==Activity.RESULT_OK)
			{
				Object day = data.getExtras().get("currentDateString");
				if (day!=null)
				{
					String dayString = day.toString();
					
					hasDetailInList = false;
					for (int i=0; i<dayList.size(); i++)
					{
						if (dayList.get(i).get("dayTime").equals(dayString))
						{
							currentDetailPosition = i;
							hasDetailInList = true;
							break;
						}
					}
					
					//刷新单天
					if (hasDetailInList) 
					{
						String[] dayStrs = new String[] {dayString};
			    	
				    	new ZDateTask(this.database, this.userNumber, new ZDateTask.ZDateProcessListener() {				
							@Override
							public void onPostExecute(ArrayList<HashMap<String, Object>> result) {
								Iterator<HashMap<String, Object>> i = result.iterator();
								while (i.hasNext())
								{
									dayList.set(currentDetailPosition, ((HashMap<String, Object>)i.next()));
								}
	
				                dayAdapter.notifyDataSetChanged();
							}
						}).execute(dayStrs);
					}
					/*this.dayList.clear();
					this.loadDate(true);
					
					this.listView.setSelection(0);
					this.currentViewPosition = 0;*/
				}
			}
		}		
	}
	
	private void setTitle()
	{
		String currentDateString = dayList.get(currentViewPosition).get("dayTime").toString();
		if (!this.lastDateString.equals(currentDateString))
		{
			ZDate currentDate = new ZDate(currentDateString);
			((TextView)this.getActivity().findViewById(R.id.actionbar_title)).setText(currentDate.getMonthString() + " " + currentDate.getYear());
			
			this.lastDateString = currentDateString;
			
			//Log.v(LOG_TAG, "设置标题");
		}

	}
	
	/**
	 * 销毁时关闭数据库连接
	 */
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		
		if (this.database != null)
		{
			this.database.close();
		}
	}
}
