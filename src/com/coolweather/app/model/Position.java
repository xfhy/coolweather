package com.coolweather.app.model;

/**
 * ��¼��ǰλ����Ϣ
 * 
 * 2016��9��19��23:47:51
 * 
 * @author XFHY ��ǰλ�õ�ʡ��,ʡ��id ��ǰλ�õĳ���,����id ��ǰλ�õ�����,����id
 */
public class Position {
	private static String province;   //��ǰλ�õ�ʡ������
	private static String provinceId; //��ǰλ��ʡ�ݵ�id
	private static String city;       //��ǰλ�õĳ�������
	private static String cityId;     //��ǰλ�õĳ���id
	private static String county;     //��ǰλ�õ���������
	private static String countyId;   //��ǰλ�õ�����id

	/**
	 * ��ȡ��ǰλ�õ�ʡ������
	 * @return
	 */
	public static String getProvince() {
		return province;
	}

	/**
	 * ���õ�ǰλ�õ�ʡ������
	 * @param province
	 */
	public static void setProvince(String province) {
		Position.province = province;
	}

	/**
	 * ��ȡ��ǰλ��ʡ�ݵ�id
	 * @return
	 */
	public static String getProvinceId() {
		return provinceId;
	}

	/**
	 * ���õ�ǰλ��ʡ�ݵ�id
	 * @param provinceId
	 */
	public static void setProvinceId(String provinceId) {
		Position.provinceId = provinceId;
	}

	/**
	 * ��ȡ��ǰλ�õĳ�������
	 * @return
	 */
	public static String getCity() {
		return city;
	}

	/**
	 * ���õ�ǰλ�õĳ�������
	 * @param city
	 */
	public static void setCity(String city) {
		Position.city = city;
	}

	/**
	 * ��ȡ��ǰλ�õĳ���id
	 * @return
	 */
	public static String getCityId() {
		return cityId;
	}

	/**
	 * ���õ�ǰλ�õĳ���id
	 * @param cityId
	 */
	public static void setCityId(String cityId) {
		Position.cityId = cityId;
	}

	/**
	 * ��ȡ��ǰλ�õ���������
	 * @return
	 */
	public static String getCounty() {
		return county;
	}

	/**
	 * ���õ�ǰλ�õ���������
	 * @param county
	 */
	public static void setCounty(String county) {
		Position.county = county;
	}

	/**
	 * ��ȡ��ǰλ�õ�����id
	 * @return
	 */
	public static String getCountyId() {
		return countyId;
	}

	/**
	 * ���õ�ǰλ�õ�����id
	 * @param countyId
	 */
	public static void setCountyId(String countyId) {
		Position.countyId = countyId;
	}

}
