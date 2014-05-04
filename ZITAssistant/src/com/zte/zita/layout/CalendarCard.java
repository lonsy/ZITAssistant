package com.zte.zita.layout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zte.zita.R;

public class CalendarCard extends RelativeLayout {
	
	private TextView cardTitle;
	private int itemLayout = R.layout.card_item_simple;
	private OnItemRender mOnItemRender;
	private OnItemRender mOnItemRenderDefault;
	private OnCellItemClick mOnCellItemClick;
	private Calendar dateDisplay;
	private ArrayList<CalendarDayLayout> cells = new ArrayList<CalendarDayLayout>();
	private LinearLayout cardGrid;

	public CalendarCard(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	public CalendarCard(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public CalendarCard(Context context) {
		super(context);
		init(context);
	}
	
	private void init(Context ctx) {
		if (isInEditMode()) return;
		View layout = LayoutInflater.from(ctx).inflate(R.layout.card_view, null, false);
		
		if (dateDisplay == null)
			dateDisplay = Calendar.getInstance();
		
		cardTitle = (TextView)layout.findViewById(R.id.cardTitle);
		cardGrid = (LinearLayout)layout.findViewById(R.id.cardGrid);
		
		//月份标题
		cardTitle.setText(new SimpleDateFormat("yyyy年MM月", Locale.getDefault()).format(dateDisplay.getTime()));

		//周几
		//Calendar cal = Calendar.getInstance();
		//cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		((TextView)layout.findViewById(R.id.cardDay1)).setText("日");
		//cal.add(Calendar.DAY_OF_WEEK, 1);
		((TextView)layout.findViewById(R.id.cardDay2)).setText("一");
		//cal.add(Calendar.DAY_OF_WEEK, 1);
		((TextView)layout.findViewById(R.id.cardDay3)).setText("二");
		//cal.add(Calendar.DAY_OF_WEEK, 1);
		((TextView)layout.findViewById(R.id.cardDay4)).setText("三");
		//cal.add(Calendar.DAY_OF_WEEK, 1);
		((TextView)layout.findViewById(R.id.cardDay5)).setText("四");
		//cal.add(Calendar.DAY_OF_WEEK, 1);
		((TextView)layout.findViewById(R.id.cardDay6)).setText("五");
		//cal.add(Calendar.DAY_OF_WEEK, 1);
		((TextView)layout.findViewById(R.id.cardDay7)).setText("六");
		
		LayoutInflater la = LayoutInflater.from(ctx);
		for(int y=0; y<cardGrid.getChildCount(); y++) {
			LinearLayout row = (LinearLayout)cardGrid.getChildAt(y);
			for(int x=0; x<row.getChildCount(); x++) {
				CalendarDayLayout cell = (CalendarDayLayout)row.getChildAt(x);
				cell.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						/*for(CheckableLayout c : cells)
							c.setChecked(false);
						((CheckableLayout)v).setChecked(true);*/
						
						if (getOnCellItemClick()!= null)
							getOnCellItemClick().onCellClick(v, (CardGridItem)v.getTag());
					}
				});
				
				View cellContent = la.inflate(itemLayout, cell, false);
				cell.addView(cellContent);
				cells.add(cell);
			}
		}
		
		addView(layout);
		
		mOnItemRenderDefault = new OnItemRender() {
			@Override
			public void onRender(CalendarDayLayout v, CardGridItem item) {
				v.setText(item.getDayOfMonth().toString());
			}
		};
		
		updateCells();
	}
	
	private void updateCells() {
		Calendar cal;
		Integer counter = 0;
		if (dateDisplay != null) 
			cal = (Calendar)dateDisplay.clone();
		else
			cal = Calendar.getInstance();
		
		cal.set(Calendar.DAY_OF_MONTH, 1);
		
		//日期排序，为一星期中的第几天减1
		int daySpacing = cal.get(Calendar.DAY_OF_WEEK) - 1;
		
		//添加第一行的上个月的最后几天
		if (daySpacing > 0) {
			Calendar prevMonth = (Calendar)cal.clone();
			prevMonth.add(Calendar.MONTH, -1);
			prevMonth.set(Calendar.DAY_OF_MONTH, prevMonth.getActualMaximum(Calendar.DAY_OF_MONTH) - daySpacing + 1);
			for(int i=0; i<daySpacing; i++) {
				CalendarDayLayout cell = cells.get(counter);
				cell.setTag(new CardGridItem(Integer.valueOf(prevMonth.get(Calendar.DAY_OF_MONTH))).setEnabled(false));
				cell.setEnabled(false);
				cell.setVisibility(View.INVISIBLE);
				(mOnItemRender == null ? mOnItemRenderDefault : mOnItemRender).onRender(cell, (CardGridItem)cell.getTag());
				counter++;
				prevMonth.add(Calendar.DAY_OF_MONTH, 1);
			}
		}
		
		int firstDay = cal.get(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		int lastDay = cal.get(Calendar.DAY_OF_MONTH)+1;
		for(int i=firstDay; i<lastDay; i++) {
			cal.set(Calendar.DAY_OF_MONTH, i-1);
			Calendar date = (Calendar)cal.clone();
			date.add(Calendar.DAY_OF_MONTH, 1);
			CalendarDayLayout cell = cells.get(counter);
			cell.setTag(new CardGridItem(i).setEnabled(true).setDate(date));
			cell.setEnabled(true);
			cell.setVisibility(View.VISIBLE);
			(mOnItemRender == null ? mOnItemRenderDefault : mOnItemRender).onRender(cell, (CardGridItem)cell.getTag());
			counter++;
		}
		
		if (dateDisplay != null) 
			cal = (Calendar)dateDisplay.clone();
		else
			cal = Calendar.getInstance();
		
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		
		//计算最后一行的最后一天之后还剩下的空格
		daySpacing = 6 - cal.get(Calendar.DAY_OF_WEEK);

		if (daySpacing > 0) {
			for(int i=0; i<daySpacing; i++) {
				CalendarDayLayout cell = cells.get(counter);
				cell.setTag(new CardGridItem(i+1).setEnabled(false)); // .setDate((Calendar)cal.clone())
				cell.setEnabled(false);
				cell.setVisibility(View.INVISIBLE);
				(mOnItemRender == null ? mOnItemRenderDefault : mOnItemRender).onRender(cell, (CardGridItem)cell.getTag());
				counter++;
			}
		}
		
		if (counter < cells.size()) {
			for(int i=counter; i<cells.size(); i++) {
				cells.get(i).setVisibility(View.GONE);
			}
		}
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed && cells.size() > 0) {
			for(CalendarDayLayout cell : cells) {
				//每个日期单元的高度设置为60dp
				cell.getLayoutParams().height = 60;
			}
		}
	}

	public int getItemLayout() {
		return itemLayout;
	}

	public void setItemLayout(int itemLayout) {
		this.itemLayout = itemLayout;
		//mCardGridAdapter.setItemLayout(itemLayout);
	}

	public OnItemRender getOnItemRender() {
		return mOnItemRender;
	}

	public void setOnItemRender(OnItemRender mOnItemRender) {
		this.mOnItemRender = mOnItemRender;
		//mCardGridAdapter.setOnItemRender(mOnItemRender);
	}

	public Calendar getDateDisplay() {
		return dateDisplay;
	}

	public void setDateDisplay(Calendar dateDisplay) {
		this.dateDisplay = dateDisplay;
		cardTitle.setText(new SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(dateDisplay.getTime()));
	}

	public OnCellItemClick getOnCellItemClick() {
		return mOnCellItemClick;
	}

	public void setOnCellItemClick(OnCellItemClick mOnCellItemClick) {
		this.mOnCellItemClick = mOnCellItemClick;
	}
	
	/**
	 * call after change any input data - to refresh view
	 */
	public void notifyChanges() {
		//mCardGridAdapter.init();
		updateCells();
	}

}
