package com.coolweather.app.model;

/**
 * 2016��8��20��10:35:12
 * 
 * City���ʵ���� ÿ�ű��ڴ������������һ��ʵ����,������ǳ������ں����Ŀ�������
 * 
 * @author XFHY
 * 
 */
public class City {
	private int id;
	private String cityName;  //������
	private String cityCode;  //���д���
	private int provinceId;   //City�����Province������

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
