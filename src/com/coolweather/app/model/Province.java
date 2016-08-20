package com.coolweather.app.model;

/**
 * 2016年8月20日10:29:28
 * 
 * Province表的实体类 每张表在代码中最好能有一个实体类,这样会非常方便于后续的开发工作
 * 
 * @author XFHY
 * 
 * 
 * 
 */
public class Province {
	private int id;
	private String provinceName;  //省份名
	private String provinceCode;  //省份代号

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

}
