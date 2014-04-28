package com.zte.zita.entity;

/**
 * ����ʵ�壬�����������Ժ�ĳ��ֵ
 * @author lonsy
 *
 */
public class DayValueEntity {
	/**
	 * û���ռ�
	 */
	public static final int STATUS_NOT = 0;
	
	/**
	 * �ѱ���
	 */
	public static final int STATUS_SAVE = 1;
	
	/**
	 * ���ύ
	 */
	public static final int STATUS_DONE = 2;	
	
	/**
	 * �Ƿ�����
	 */
	private boolean isWorkDay;
	
	/**
	 * �ռ�״̬
	 */
	private int dailyStatus;
	
	/**
	 * ָ��ֵ	
	 */
	private Object dayValue;
	
	private DayValueEntity(boolean isWorkDay, int dailyStatus, Object dayValue)
	{
		this.isWorkDay = isWorkDay;
		this.dailyStatus = dailyStatus;
		this.dayValue = dayValue;		
	}
	
	public static DayValueEntity generateNewEntity(boolean isWorkDay, int dailyStatus, Object dayValue)
	{
		return new DayValueEntity(isWorkDay, dailyStatus, dayValue);
	}

	public boolean isWorkDay() {
		return isWorkDay;
	}

	public void setWorkDay(boolean isWorkDay) {
		this.isWorkDay = isWorkDay;
	}

	public int getDailyStatus() {
		return dailyStatus;
	}

	public void setDailyStatus(int dailyStatus) {
		this.dailyStatus = dailyStatus;
	}

	public Object getDayValue() {
		return dayValue;
	}

	public void setDayValue(Object dayValue) {
		this.dayValue = dayValue;
	}
}
