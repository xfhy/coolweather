package com.coolweather.app.model;

/**
 * 2016��8��20��10:29:28
 * 
 * Province���ʵ���� ÿ�ű��ڴ������������һ��ʵ����,������ǳ������ں����Ŀ�������
 * 
 * @author XFHY
 * 
 * 
 * 
 */
public class Province {
	private int id;
	private String provinceName;  //ʡ����
	private String provinceCode;  //ʡ�ݴ���

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
