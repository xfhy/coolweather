package com.coolweather.app.model;

/**
 * 2016年8月23日21:07:12
 * 用于显示ListView的一行
 * 单独设置成一个类
 * @author XFHY
 *
 */
public class DayWeather {
	private String dayDate; // 日期:号数
	private String weatherType; // 天气类型
	private String lowTemp; // 最低温度
	private String highTemp; // 最高温度

	public DayWeather(){
		
	}
	
	/**
	 * 构造方法
	 * 
	 * @param dayDate
	 *            日期:号数
	 * @param weatherType
	 *            天气类型
	 * @param lowTemp
	 *            最低温度
	 * @param highTemp
	 *            最高温度
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