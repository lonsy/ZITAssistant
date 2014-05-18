package com.zte.zita.entity;

/**
 * 日期实体，包括基本属性和某个值
 * @author lonsy
 *
 */
public class DayValueEntity {
	/**
	 * 没有日志
	 */
	public static final int STATUS_NOT = 0;
	
	/**
	 * 已保存
	 */
	public static final int STATUS_SAVE = 1;
	
	/**
	 * 已提交
	 */
	public static final int STATUS_DONE = 2;	
	
	/**
	 * 是否工作日
	 */
	private boolean isWorkDay;
	
	/**
	 * 是否在今日之前
	 */
	private boolean isBeforeToday;
	
	/**
	 * 日志状态
	 */
	private int dailyStatus;
	
	/**
	 * 指定值	
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
