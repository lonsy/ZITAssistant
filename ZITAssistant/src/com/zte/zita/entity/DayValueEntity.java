package com.zte.zita.entity;

/**
 * ����ʵ�壬�����������Ժ�ĳ��ֵ
 * @author lonsy
 *
 */
public class DayValueEntity {
	/**
	 * û����־
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
	 * �Ƿ��ڽ���֮ǰ
	 */
	private boolean isBeforeToday;
	
	/**
	 * ��־״̬
	 */
	private int dailyStatus;
	
	/**
	 * ָ��ֵ	
	 */
	private Object dayValue;
	
	private DayValueEntity(boolean isWorkDay, int dailyStatus, Object dayValue, boolean isBeforeToday)
	{
		this.isWorkDay = isWorkDay;
		this.dailyStatus = dailyStatus;
		this.dayValue = dayValue;		
		this.isBeforeToday = isBeforeToday;
	}
	
	public static DayValueEntity generateNewEntity(boolean isWorkDay, int dailyStatus, Object dayValue, boolean isBeforeToday)
	{
		return new DayValueEntity(isWorkDay, dailyStatus, dayValue, isBeforeToday);
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

	public boolean isBeforeToday() {
		return isBeforeToday;
	}

	public void setBeforeToday(boolean isBeforeToday) {
		this.isBeforeToday = isBeforeToday;
	}
}
