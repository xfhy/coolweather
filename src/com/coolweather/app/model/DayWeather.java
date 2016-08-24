package com.coolweather.app.model;

/**
 * 2016��8��23��21:07:12
 * ������ʾListView��һ��
 * �������ó�һ����
 * @author XFHY
 *
 */
public class DayWeather {
	private String dayDate; // ����:����
	private String weatherType; // ��������
	private String lowTemp; // ����¶�
	private String highTemp; // ����¶�

	public DayWeather(){
		
	}
	
	/**
	 * ���췽��
	 * 
	 * @param dayDate
	 *            ����:����
	 * @param weatherType
	 *            ��������
	 * @param lowTemp
	 *            ����¶�
	 * @param highTemp
	 *            ����¶�
	 */
	public DayWeather(String dayDate, String weatherType, String lowTemp,
			String highTemp) {
		this.dayDate = dayDate;
		this.weatherType = weatherType;
		this.lowTemp = lowTemp;
		this.highTemp = highTemp;
	}

	public String getDayDate() {
		return dayDate;
	}

	public void setDayDate(String dayDate) {
		this.dayDate = dayDate;
	}

	public String getWeatherType() {
		return weatherType;
	}

	public void setWeatherType(String weatherType) {
		this.weatherType = weatherType;
	}

	public String getLowTemp() {
		return lowTemp;
	}

	public void setLowTemp(String lowTemp) {
		this.lowTemp = lowTemp;
	}

	public String getHighTemp() {
		return highTemp;
	}

	public void setHighTemp(String highTemp) {
		this.highTemp = highTemp;
	}

}