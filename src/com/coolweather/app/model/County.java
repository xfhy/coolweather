package com.coolweather.app.model;

/**
 * 2016��8��20��10:38:15
 * 
 * County���ʵ���� ÿ�ű��ڴ������������һ��ʵ����,������ǳ������ں����Ŀ�������
 * 
 * @author XFHY
 * 
 */
public class County {
	private int id;
	private String countyName;  //����
	private String countyCode;  //�ش���
	private int cityId;         //County�����City������

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
