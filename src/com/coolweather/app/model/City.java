package com.coolweather.app.model;

/**
 * 2016年8月20日10:35:12
 * 
 * City表的实体类 每张表在代码中最好能有一个实体类,这样会非常方便于后续的开发工作
 * 
 * @author XFHY
 * 
 */
public class City {
	private int id;
	private String cityName;  //城市名
	private String cityCode;  //城市代号
	private int provinceId;   //City表关联Province表的外键

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public int getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}

}
