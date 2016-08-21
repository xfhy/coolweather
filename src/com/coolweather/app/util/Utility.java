package com.coolweather.app.util;

import android.text.TextUtils;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

/**
 * 2016��8��21��9:39:30
 * 
 * ���ڽ������������ص�ʡ��������(����"����|����,����|����")���ָ�ʽ�� 
 * ������Ҫ�ṩһ�����������ʹ�����������
 * 
 * @author XFHY
 * 
 * 
 */
public class Utility {
	
	/**
	 * �����ʹ�����������ص�ʡ������(�����������浽���ݿ���)
	 * @param coolWeatherDB ���õ����ݿ������װ����
	 * @param response  ���ݿⷵ�ص�����
	 * @return �����ʹ���ɹ��򷵻�true ,���򷵻�false
	 */
	public synchronized static boolean handleProvincesResponse(
			CoolWeatherDB coolWeatherDB, String response) {
		if(!TextUtils.isEmpty(response)){   //TextUtils.isEmpty()���ַ���Ϊnull���߳���Ϊ0ʱ����true
			String[] allProvinces = response.split(",");     //ʡ��֮��ļ���Ƕ���,���ｫ���е�ʡ��ȡ������װ��һ������
			if( allProvinces != null && allProvinces.length > 0 ){
				for(String p : allProvinces){   //��ÿ��Ԫ��ȡ����(һ��Ԫ�ؾ���һ��ʡ�ݵĴ���������)
					String[] array = p.split("\\|");    //��ʡ�ݵĴ��������Ʒֿ�
					
					//��һ��ʡ�����ݷ�װ����
					Province province = new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					
					//���������������ݴ洢��Province��
					coolWeatherDB.saveProvince(province);
				}
				return true;   //�����ݴ洢�����ݿ�ɹ�
			}
		}
		return false;
	}
	
	/**
	 * �����ʹ�����������ص��м�����
	 * @param coolWeatherDB
	 * @param response
	 * @param provinceId  ʡ �� id
	 * @return
	 */
	public static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB,
			String response,int provinceId){
		if(!TextUtils.isEmpty(response)){   //TextUtils.isEmpty()���ַ���Ϊnull���߳���Ϊ0ʱ����true
			String[] allCities = response.split(",");     //����֮��ļ���Ƕ���,���ｫ���еĳ���ȡ������װ��һ������
			if( allCities != null && allCities.length > 0 ){
				for(String c : allCities){   //��ÿ��Ԫ��ȡ����(һ��Ԫ�ؾ���һ�����еĴ���������)
					String[] array = c.split("\\|");    //�����еĴ��������Ʒֿ�
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					
					//���������������ݴ洢�����ݿ�City����
					coolWeatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * �����ʹ�����������ص��ؼ�����
	 * @param coolWeatherDB
	 * @param response
	 * @param cityId  ����id   ���
	 * @return
	 */
	public static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB,String response, int cityId){
		if(!TextUtils.isEmpty(response)){   //TextUtils.isEmpty()���ַ���Ϊnull���߳���Ϊ0ʱ����true
			String[] allCounties = response.split(",");     //�ؼ�֮��ļ���Ƕ���,���ｫ���е��ؼ�ȡ������װ��һ������
			if( allCounties != null && allCounties.length > 0 ){
				for(String c : allCounties){   //��ÿ��Ԫ��ȡ����(һ��Ԫ�ؾ���һ���ؼ��Ĵ���������)
					String[] array = c.split("\\|");    //���ؼ��Ĵ��������Ʒֿ�
					
					County county = new County();
					county.setCountyCode(array[0]);
					county.setCountyName(array[1]);
					county.setCityId(cityId);
					
					//���������������ݴ洢�����ݿ�County��
					coolWeatherDB.saveCounty(county);
				}
				return true;
			}
		}
		return false;
	}
	
}
