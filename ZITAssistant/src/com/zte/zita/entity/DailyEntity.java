package com.zte.zita.entity;

import java.util.Date;

/**
 * �ռ�ʵ����
 * @author lonsy
 */
public class DailyEntity {
	
	/**
	 * �ռ�ID
	 */
	private int dailyId;

	/**
	 * ������  ����
	 */
	private String createdBy;
	
	/**
	 * �ռ�״̬
	 */
	private int dailyStatus;
	
	/**
	 * �ռ����� 2014-04-26
	 */
	private String dailyDate;
	
	/**
	 * �ռ�����
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
