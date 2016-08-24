package com.coolweather.app.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.coolweather.app.model.DayWeather;
import com.coolweather.app.util.MyApplication;

/**
 * 2016年8月23日23:04:52
 * 转为ListView设计的数据库操作的类
 * @author XFHY
 *
 */
public class ListViewDB {
	/**
	 * 数据库版本
	 */
	public static final String DB_NAME = "cool_weather";

	/**
	 * 数据库版本
	 */
	public static final int VERSION = 1;
	
	private SQLiteDatabase db;
	
	public ListViewDB(){
		// 实例化CoolWeatherOpenHelper对象,用来操作数据库
		CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(MyApplication.getContext(),
				DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase(); // 获得数据库 可写入的方式
	}
	
	/**
	 * 删除数据库中所有的DayWeather表的信息
	 */
	public void deleteAllInfo(){
		db.delete("DayWeather", null, null);   //存储之前,先删除里面的所有垃圾数据
	}
	
	/**
	 * 将DayWeather实例存储到数据库
	 * @param dayWeather
	 */
	public void saveDayWeather(DayWeather dayWeather){
		
		if(dayWeather != null){
			ContentValues values = new ContentValues();
			values.put("day_date", dayWeather.getDayDate());
			values.put("weather_type", dayWeather.getWeatherType());
			values.put("low_temp", dayWeather.getLowTemp());
			values.put("high_temp", dayWeather.getHighTemp());
			db.insert("DayWeather", null, values);
		}
	}
	
	/**
	 * 从数据库中读取3天的天气预报(DayWeather的集合)
	 * @return
	 */
	public List<DayWeather> loadDayWeather(){
		List<DayWeather> list = new ArrayList<DayWeather>();
		Cursor cursor = db.query("DayWeather", null, null, null,
				null, null, null);
		
		if(cursor.moveToFirst()){
			do{
				DayWeather dayWeather = new DayWeather();
				dayWeather.setDayDate(cursor.getString(cursor.getColumnIndex("day_date")));
				dayWeather.setWeatherType(cursor.getString(cursor.getColumnIndex("weather_type")));
				dayWeather.setLowTemp(cursor.getString(cursor.getColumnIndex("low_temp")));
				dayWeather.setHighTemp(cursor.getString(cursor.getColumnIndex("high_temp")));
				list.add(dayWeather);
			}while(cursor.moveToNext());
		}
		if(cursor != null){
			cursor.close();
		}
		
		return list;
	}
}
