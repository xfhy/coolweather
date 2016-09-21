package com.coolweather.app.model;

/**
 * 记录当前位置信息
 * 
 * 2016年9月19日23:47:51
 * 
 * @author XFHY 当前位置的省份,省份id 当前位置的城市,城市id 当前位置的县区,县区id
 */
public class Position {
	private static String province;   //当前位置的省份名称
	private static String provinceId; //当前位置省份的id
	private static String city;       //当前位置的城市名称
	private static String cityId;     //当前位置的城市id
	private static String county;     //当前位置的县区名称
	private static String countyId;   //当前位置的县区id

	/**
	 * 获取当前位置的省份名称
	 * @return
	 */
	public static String getProvince() {
		return province;
	}

	/**
	 * 设置当前位置的省份名称
	 * @param province
	 */
	public static void setProvince(String province) {
		Position.province = province;
	}

	/**
	 * 获取当前位置省份的id
	 * @return
	 */
	public static String getProvinceId() {
		return provinceId;
	}

	/**
	 * 设置当前位置省份的id
	 * @param provinceId
	 */
	public static void setProvinceId(String provinceId) {
		Position.provinceId = provinceId;
	}

	/**
	 * 获取当前位置的城市名称
	 * @return
	 */
	public static String getCity() {
		return city;
	}

	/**
	 * 设置当前位置的城市名称
	 * @param city
	 */
	public static void setCity(String city) {
		Position.city = city;
	}

	/**
	 * 获取当前位置的城市id
	 * @return
	 */
	public static String getCityId() {
		return cityId;
	}

	/**
	 * 设置当前位置的城市id
	 * @param cityId
	 */
	public static void setCityId(String cityId) {
		Position.cityId = cityId;
	}

	/**
	 * 获取当前位置的县区名称
	 * @return
	 */
	public static String getCounty() {
		return county;
	}

	/**
	 * 设置当前位置的县区名称
	 * @param county
	 */
	public static void setCounty(String county) {
		Position.county = county;
	}

	/**
	 * 获取当前位置的县区id
	 * @return
	 */
	public static String getCountyId() {
		return countyId;
	}

	/**
	 * 设置当前位置的县区id
	 * @param countyId
	 */
	public static void setCountyId(String countyId) {
		Position.countyId = countyId;
	}

}
