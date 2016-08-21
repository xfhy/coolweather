package com.coolweather.app.util;

import android.text.TextUtils;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

/**
 * 2016年8月21日9:39:30
 * 
 * 用于解析服务器返回的省市县数据(都是"代号|城市,代号|城市")这种格式的 
 * 所以需要提供一个类来解析和处理这种数据
 * 
 * @author XFHY
 * 
 * 
 */
public class Utility {
	
	/**
	 * 解析和处理服务器返回的省级数据(解析出来并存到数据库中)
	 * @param coolWeatherDB 常用的数据库操作封装的类
	 * @param response  数据库返回的数据
	 * @return 解析和处理成功则返回true ,否则返回false
	 */
	public synchronized static boolean handleProvincesResponse(
			CoolWeatherDB coolWeatherDB, String response) {
		if(!TextUtils.isEmpty(response)){   //TextUtils.isEmpty()当字符串为null或者长度为0时返回true
			String[] allProvinces = response.split(",");     //省份之间的间隔是逗号,这里将所有的省份取出来封装成一个数组
			if( allProvinces != null && allProvinces.length > 0 ){
				for(String p : allProvinces){   //将每个元素取出来(一个元素就是一个省份的代号与名称)
					String[] array = p.split("\\|");    //将省份的代号与名称分开
					
					//将一个省份数据封装成类
					Province province = new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					
					//将解析出来的数据存储到Province表
					coolWeatherDB.saveProvince(province);
				}
				return true;   //将数据存储到数据库成功
			}
		}
		return false;
	}
	
	/**
	 * 解析和处理服务器返回的市级数据
	 * @param coolWeatherDB
	 * @param response
	 * @param provinceId  省 的 id
	 * @return
	 */
	public static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB,
			String response,int provinceId){
		if(!TextUtils.isEmpty(response)){   //TextUtils.isEmpty()当字符串为null或者长度为0时返回true
			String[] allCities = response.split(",");     //城市之间的间隔是逗号,这里将所有的城市取出来封装成一个数组
			if( allCities != null && allCities.length > 0 ){
				for(String c : allCities){   //将每个元素取出来(一个元素就是一个城市的代号与名称)
					String[] array = c.split("\\|");    //将城市的代号与名称分开
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					
					//将解析出来的数据存储到数据库City表中
					coolWeatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 解析和处理服务器返回的县级数据
	 * @param coolWeatherDB
	 * @param response
	 * @param cityId  城市id   外键
	 * @return
	 */
	public static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB,String response, int cityId){
		if(!TextUtils.isEmpty(response)){   //TextUtils.isEmpty()当字符串为null或者长度为0时返回true
			String[] allCounties = response.split(",");     //县级之间的间隔是逗号,这里将所有的县级取出来封装成一个数组
			if( allCounties != null && allCounties.length > 0 ){
				for(String c : allCounties){   //将每个元素取出来(一个元素就是一个县级的代号与名称)
					String[] array = c.split("\\|");    //将县级的代号与名称分开
					
					County county = new County();
					county.setCountyCode(array[0]);
					county.setCountyName(array[1]);
					county.setCityId(cityId);
					
					//将解析出来的数据存储到数据库County表
					coolWeatherDB.saveCounty(county);
				}
				return true;
			}
		}
		return false;
	}
	
}
