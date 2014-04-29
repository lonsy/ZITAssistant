package com.zte.zita.utils;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class ZDate {
	private static final String[] WEEK_STRINGS = new String[] {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
	private static final String[] MONTH_STRINGS = new String[] {"一月", "二月", "三月", "四月", "五月", "六月" 
		,"七月","八月","九月","十月","十一月","十二月"};

	private Date date;
	
	public ZDate(Date date)
	{
		Calendar initCal=Calendar.getInstance();
		initCal.setTime(date);
		
		Calendar cal=Calendar.getInstance();
		cal.set(Calendar.YEAR, initCal.get(Calendar.YEAR));
		cal.set(Calendar.MONTH, initCal.get(Calendar.MONTH));
		cal.set(Calendar.DAY_OF_MONTH, initCal.get(Calendar.DAY_OF_MONTH));
		
		this.date = cal.getTime();
	}
	
	public ZDate(String dayString)
	{
		try 
		{
			this.date = new SimpleDateFormat("yyyy-MM-dd").parse(dayString);
		} catch (Exception ex)
		{
			this.date = null;
		}
	}
	
	public ZDate()
	{
		this(new Date());
	}

	/**
	 * 获取当前日期所在周的起始日期
	 * @return
	 */
	public static ZDate getCurrentWeekStartDay()
	{
		ZDate date = new ZDate();
		return getWeekStartDay(date);
	}
	
	/**
	 * 获取指定日期所在周的起始日期
	 * @param date
	 * @return
	 */
	public static ZDate getWeekStartDay(ZDate date)
	{
		int dayForWeek = date.getDayForWeek();		
		return date.add(1 - dayForWeek);
	}
	
	/**
	 * 增加指定天数，返回新的日期
	 * @param day
	 */
	public ZDate add(int day)
	{
		Calendar c = Calendar.getInstance();  
		c.setTime(this.date);
		c.add(Calendar.DATE, day);
		return new ZDate(c.getTime());
	}
	
	/**
	 * 判断星期几
	 * @param day
	 * @return 1~7
	 */
	public int getDayForWeek() {  
		Calendar c = Calendar.getInstance();  
		c.setTime(this.date);  
		int dayForWeek = 0;  
		if(c.get(Calendar.DAY_OF_WEEK) == 1){  
			dayForWeek = 7;  
		}else{  
			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;  
		}  
		return dayForWeek;  
	}
	
	/**
	 * 获取星期几字符串
	 * @param day
	 * @return 周一~周日
	 */
	public String getDayForWeekString() 
	{
		return WEEK_STRINGS[this.getDayForWeek()-1];
	}
	
	public long getDayId()
	{
		return this.date.getTime();
	}
	
	/**
	 * 是否在今天之前
	 * @return
	 */
	public boolean isBeforeToday()
	{
		Date today = new Date();
		if (today.getTime() > this.date.getTime())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * 获取月份的第几天
	 * @return
	 */
	public int getDayOfMonth()
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(this.date);
		return cal.get(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * 获取一年中的第几个月
	 * @return
	 */
	public int getMonth()
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(this.date);
		return cal.get(Calendar.MONTH);
	}
	
	/**
	 * 获取一年中的第几个字符串
	 * @param day
	 * @return 周一~周日
	 */
	public String getMonthString() 
	{
		return MONTH_STRINGS[this.getMonth()];
	}
	
	/**
	 * 获取年份
	 * @return
	 */
	public int getYear()
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(this.date);
		return cal.get(Calendar.YEAR);
	}
	
	/**
	 * 是否工作日，周六、日为休息天
	 * @return
	 */
	public boolean isWorkDay()
	{
		int dayForWeek = this.getDayForWeek();
		if (6==dayForWeek || 7==dayForWeek)
		{
			return false;
		}
		return true;
	}

	@SuppressLint("SimpleDateFormat")
	public String getDayString()
	{
		return new SimpleDateFormat("yyyy-MM-dd").format(this.date);
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
