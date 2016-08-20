package com.coolweather.app.model;

/**
 * 2016年8月20日10:38:15
 * 
 * County表的实体类 每张表在代码中最好能有一个实体类,这样会非常方便于后续的开发工作
 * 
 * @author XFHY
 * 
 */
public class County {
	private int id;
	private String countyName;  //县名
	private String countyCode;  //县代号
	private int cityId;         //County表关联City表的外键

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCountyName() {
		return countyName;
	}

	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}

	public String getCountyCode() {
		return countyCode;
	}

	public void setCountyCode(String countyCode) {
		this.countyCode = countyCode;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

}
