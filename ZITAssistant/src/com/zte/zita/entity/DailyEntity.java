package com.zte.zita.entity;

import java.util.Date;

/**
 * ��־ʵ����
 * @author lonsy
 */
public class DailyEntity {
	
	/**
	 * ��־ID
	 */
	private int dailyId;

	/**
	 * ������  ����
	 */
	private String createdBy;
	
	/**
	 * ��־״̬
	 */
	private int dailyStatus;
	
	/**
	 * ��־���� 2014-04-26
	 */
	private String dailyDate;
	
	/**
	 * ��־����
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
