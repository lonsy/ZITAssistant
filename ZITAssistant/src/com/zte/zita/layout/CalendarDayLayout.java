package com.zte.zita.layout;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zte.zita.R;
import com.zte.zita.activities.DailyDetailActivity;
import com.zte.zita.entity.DayValueEntity;

public class CalendarDayLayout extends RelativeLayout implements OnClickListener {
	
	private TextView cardDayText;

	private HashMap<String, Object> date;
	
	private Fragment fragment;
	
    @SuppressLint("NewApi")
    public CalendarDayLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public CalendarDayLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CalendarDayLayout(Context context) {
        super(context);
        init(context);
    }
    
    private void init(Context context) {
    	View.inflate(context, R.layout.card_cell, this); 
    	
    	this.cardDayText = (TextView)this.findViewById(R.id.cardDayText);
    }
    
    public void setText(String text)
    {
    	this.cardDayText.setText(text);
    }
    
    public void initDate(HashMap<String, Object> date, Fragment fragment)
    {
    	this.date = date;
    	this.fragment = fragment;
    	
    	DayValueEntity entity = (DayValueEntity)date.get("dailyStatus");
    	if (entity.isBeforeToday() && entity.isWorkDay() 
    			|| DayValueEntity.STATUS_NOT != entity.getDailyStatus())
    	{
    		if (DayValueEntity.STATUS_DONE == entity.getDailyStatus())
    		{
    			this.cardDayText.setBackgroundColor(Color.parseColor("#99CB34"));
    		}
    		else if (DayValueEntity.STATUS_SAVE == entity.getDailyStatus())
    		{
    			this.cardDayText.setBackgroundColor(Color.parseColor("#FE9A00"));
    		}
    		else if (DayValueEntity.STATUS_NOT == entity.getDailyStatus())
    		{
    			this.cardDayText.setBackgroundColor(Color.parseColor("#F3584A"));
    		}
    	}
    }
    
    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        return drawableState;
    }

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
	}

	@Override
	public void onClick(View v) {
		DayValueEntity entity = (DayValueEntity)this.date.get("dailyStatus");
		if (entity.getDailyStatus() == DayValueEntity.STATUS_NOT)
		{
			//没有日记，则新建
			Intent intent = new Intent(v.getContext(), DailyDetailActivity.class);
			intent.putExtra("currentDateString", this.date.get("dayTime").toString());
			fragment.startActivityForResult(intent, 0);
		}
		else
		{
			Intent intent = new Intent();
			intent.setClass(v.getContext(), DailyDetailActivity.class);
			intent.putExtra("currentDateString", this.date.get("dayTime").toString());
			intent.putExtra("dailyStatus", entity.getDailyStatus());
			intent.putExtra("dailyContent", this.date.get("dailyContent").toString());
			intent.putExtra("dailyId", this.date.get("dailyId").toString());
			fragment.startActivityForResult(intent, 0);
		}
	}
}
