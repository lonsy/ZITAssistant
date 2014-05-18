package com.zte.zita.entity;

import java.util.Date;

/**
 * 日志实体类
 * @author lonsy
 */
public class DailyEntity {
	
	/**
	 * 日志ID
	 */
	private int dailyId;

	/**
	 * 创建人  工号
	 */
	private String createdBy;
	
	/**
	 * 日志状态
	 */
	private int dailyStatus;
	
	/**
	 * 日志日期 2014-04-26
	 */
	private String dailyDate;
	
	/**
	 * 日志内容
	 */
	private String dailyContent;

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public int getDailyStatus() {
		return dailyStatus;
	}

	public void setDailyStatus(int dailyStatus) {
		this.dailyStatus = dailyStatus;
	}

	public String getDailyContent() {
		return dailyContent;
	}

	public void setDailyContent(String dailyContent) {
		this.dailyContent = dailyContent;
	}

	public String getDailyDate() {
		return dailyDate;
	}

	public void setDailyDate(String dailyDate) {
		this.dailyDate = dailyDate;
	}

	public int getDailyId() {
		return dailyId;
	}

	public void setDailyId(int dailyId) {
		this.dailyId = dailyId;
	}
}
