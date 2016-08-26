package com.coolweather.app.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.db.ListViewDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.DayWeather;
import com.coolweather.app.model.Province;

/**
 * 2016��8��21��9:39:30
 * 
 * ���ڽ������������ص�ʡ��������(����"����|����,����|����")���ָ�ʽ�� 
 * ������Ҫ�ṩһ�����������ʹ�����������
 * 
 * �����ʹ�����������ص�JSON����
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
	
	/**
	 * �������������ص�JSON����,�����������������ݴ洢������(SharedPreferences�ļ�)
	 * @param context
	 * @param response
	 */
	public static void handleWeatherResponse(Context context,String response){
		try {		
			JSONObject jsonObject = new JSONObject(response);
			JSONObject data = jsonObject.getJSONObject("data");
			String currentTemp = data.getString("wendu");   //��ǰ�¶�
			
			JSONArray jsonArray = data.getJSONArray("forecast");  //ȡ��һ���������͵Ķ���
			
			JSONObject tempJsonObject = jsonArray.getJSONObject(0);   //�ƶ�ȥ���������
			String windDirect = tempJsonObject.getString("fengxiang");   //����
			String windPower = tempJsonObject.getString("fengli");       //����
			String highTemp = tempJsonObject.getString("high");          //����
			String weatherDesp = tempJsonObject.getString("type");       //��������
			String lowTemp = tempJsonObject.getString("low");            //����
			
			//���������ص��¶���Ϣ��������:   "high":"���� 37��",      ������ֻ��Ҫ������¶ȼ���
			String[] temp = highTemp.split(" ");       
			highTemp = temp[1];
			temp = lowTemp.split(" ");
			lowTemp = temp[1];
			
			/*---------------------��ȡδ��3���������Ϣ------------------*/
			Calendar cal = Calendar.getInstance();         //ʵ����Calendar����    ������ȡ����
			ListViewDB listViewDB = new ListViewDB();      //ʵ����ListViewDBʵ��
			listViewDB.deleteAllInfo();    //�洢֮ǰ��ɾ�����ݿ���֮ǰʣ�µ���������
			for (int i = 0; i < 3; i++) { 
				JSONObject tempJsonObject2 = jsonArray.getJSONObject(i);
				DayWeather dayWeather = new DayWeather();
				//��ȡ��ǰ���ں���
				dayWeather.setDayDate( (cal.get(Calendar.DATE)+i)+"��" );
				dayWeather.setWeatherType(tempJsonObject2.getString("type"));
				
				  //���������ص��¶���Ϣ  :   ����24��C   ����32��C    ����ֻ��Ҫ�����24��C����
				String low = tempJsonObject2.getString("low"); 
				temp = low.split(" ");
				low = temp[1];
				dayWeather.setLowTemp(low);             //����¶�
				 
				String high = tempJsonObject2.getString("high");
				temp = high.split(" ");
				high = temp[1];
				dayWeather.setHighTemp(high);            //����¶�
				
				listViewDB.saveDayWeather(dayWeather);   // ��DayWeatherʵ���洢�����ݿ�
			}
			String cityName = data.getString("city");    //��ȡ��ǰ������
			Log.d("xfhy",cityName);
			
			saveWeatherInfo(context, cityName, currentTemp, windDirect, windPower, highTemp, weatherDesp, lowTemp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * �����������ص�����������Ϣ�洢��SharedPreferences�ļ���
	 * @param context
	 * @param cityName
	 * @param weatherCode
	 * @param temp1
	 * @param temp2
	 * @param weatherDesp
	 * @param publishTime
	 */
	public static void saveWeatherInfo(Context context, String cityName,
			String currentTemp,String windDirect, String windPower, String highTemp, String weatherDesp,
			String lowTemp) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��M��d��",Locale.CHINA);   //�������ڸ�ʽ
		
		//�õ�Editor����,�Ϳ��Ա༭��
		Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		
		//������
		editor.putBoolean("city_selected", true);        //��ǰ�Ѿ�ѡ���˳���
		editor.putString("city_name", cityName);         //������      
		editor.putString("current_temp", currentTemp);   //��ǰ�¶�
		editor.putString("wind_direct", windDirect);     //����
		editor.putString("wind_power", windPower);       //����
		editor.putString("high_temp", highTemp);         //����¶�
		editor.putString("low_temp", lowTemp);           //����¶�
		editor.putString("weather_desp", weatherDesp);   //��������  
		editor.putString("current_date", sdf.format(new Date()));    //��ǰ����
		editor.commit();   //�ύ
	}
	
	
	
}
