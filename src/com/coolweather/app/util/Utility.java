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
 * 2016年8月21日9:39:30
 * 
 * 用于解析服务器返回的省市县数据(都是"代号|城市,代号|城市")这种格式的 
 * 所以需要提供一个类来解析和处理这种数据
 * 
 * 解析和处理服务器返回的JSON数据
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
	
	/**
	 * 解析服务器返回的JSON数据,并将解析出来的数据存储到本地(SharedPreferences文件)
	 * @param context
	 * @param response
	 */
	public static void handleWeatherResponse(Context context,String response){
		try {		
			JSONObject jsonObject = new JSONObject(response);
			JSONObject data = jsonObject.getJSONObject("data");
			String currentTemp = data.getString("wendu");   //当前温度
			
			JSONArray jsonArray = data.getJSONArray("forecast");  //取出一个数组类型的东西
			
			JSONObject tempJsonObject = jsonArray.getJSONObject(0);   //制度去今天的天气
			String windDirect = tempJsonObject.getString("fengxiang");   //风向
			String windPower = tempJsonObject.getString("fengli");       //风力
			String highTemp = tempJsonObject.getString("high");          //高温
			String weatherDesp = tempJsonObject.getString("type");       //天气类型
			String lowTemp = tempJsonObject.getString("low");            //低温
			
			//服务器返回的温度信息是这样的:   "high":"高温 37℃",      我现在只需要后面的温度即可
			String[] temp = highTemp.split(" ");       
			highTemp = temp[1];
			temp = lowTemp.split(" ");
			lowTemp = temp[1];
			
			/*---------------------读取未来3天的天气信息------------------*/
			Calendar cal = Calendar.getInstance();         //实例化Calendar对象    用来获取日期
			ListViewDB listViewDB = new ListViewDB();      //实例化ListViewDB实例
			listViewDB.deleteAllInfo();    //存储之前先删除数据库中之前剩下的垃圾数据
			for (int i = 0; i < 3; i++) { 
				JSONObject tempJsonObject2 = jsonArray.getJSONObject(i);
				DayWeather dayWeather = new DayWeather();
				//获取当前日期号数
				dayWeather.setDayDate( (cal.get(Calendar.DATE)+i)+"日" );
				dayWeather.setWeatherType(tempJsonObject2.getString("type"));
				
				  //服务器返回的温度信息  :   低温24°C   高温32°C    现在只需要后面的24°C即可
				String low = tempJsonObject2.getString("low"); 
				temp = low.split(" ");
				low = temp[1];
				dayWeather.setLowTemp(low);             //最低温度
				 
				String high = tempJsonObject2.getString("high");
				temp = high.split(" ");
				high = temp[1];
				dayWeather.setHighTemp(high);            //最高温度
				
				listViewDB.saveDayWeather(dayWeather);   // 将DayWeather实例存储到数据库
			}
			String cityName = data.getString("city");    //获取当前城市名
			Log.d("xfhy",cityName);
			
			saveWeatherInfo(context, cityName, currentTemp, windDirect, windPower, highTemp, weatherDesp, lowTemp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将服务器返回的所有天气信息存储到SharedPreferences文件中
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
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日",Locale.CHINA);   //定义日期格式
		
		//得到Editor对象,就可以编辑了
		Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		
		//放数据
		editor.putBoolean("city_selected", true);        //当前已经选择了城市
		editor.putString("city_name", cityName);         //城市名      
		editor.putString("current_temp", currentTemp);   //当前温度
		editor.putString("wind_direct", windDirect);     //风向
		editor.putString("wind_power", windPower);       //风力
		editor.putString("high_temp", highTemp);         //最高温度
		editor.putString("low_temp", lowTemp);           //最低温度
		editor.putString("weather_desp", weatherDesp);   //天气类型  
		editor.putString("current_date", sdf.format(new Date()));    //当前日期
		editor.commit();   //提交
	}
	
	
	
}
