package com.zte.zita.utils;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class ZDate {
	private static final String[] WEEK_STRINGS = new String[] {"��һ", "�ܶ�", "����", "����", "����", "����", "����"};
	private static final String[] MONTH_STRINGS = new String[] {"һ��", "����", "����", "����", "����", "����" 
		,"����","����","����","ʮ��","ʮһ��","ʮ����"};

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
	 * ��ȡ��ǰ���������ܵ���ʼ����
	 * @return
	 */
	public static ZDate getCurrentWeekStartDay()
	{
		ZDate date = new ZDate();
		return getWeekStartDay(date);
	}
	
	/**
	 * ��ȡָ�����������ܵ���ʼ����
	 * @param date
	 * @return
	 */
	public static ZDate getWeekStartDay(ZDate date)
	{
		int dayForWeek = date.getDayForWeek();		
		return date.add(1 - dayForWeek);
	}
	
	/**
	 * ����ָ�������������µ�����
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
	 * �ж����ڼ�
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
	 * ��ȡ���ڼ��ַ���
	 * @param day
	 * @return ��һ~����
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
	 * �Ƿ��ڽ���֮ǰ
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
	 * ��ȡ�·ݵĵڼ���
	 * @return
	 */
	public int getDayOfMonth()
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(this.date);
		return cal.get(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * ��ȡһ���еĵڼ�����
	 * @return
	 */
	public int getMonth()
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(this.date);
		return cal.get(Calendar.MONTH);
	}
	
	/**
	 * ��ȡһ���еĵڼ����ַ���
	 * @param day
	 * @return ��һ~����
	 */
	public String getMonthString() 
	{
		return MONTH_STRINGS[this.getMonth()];
	}
	
	/**
	 * ��ȡ���
	 * @return
	 */
	public int getYear()
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(this.date);
		return cal.get(Calendar.YEAR);
	}
	
	/**
	 * �Ƿ����գ���������Ϊ��Ϣ��
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
