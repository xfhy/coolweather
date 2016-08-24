package com.coolweather.app.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.coolweather.app.model.DayWeather;
import com.coolweather.app.util.MyApplication;

/**
 * 2016��8��23��23:04:52
 * תΪListView��Ƶ����ݿ��������
 * @author XFHY
 *
 */
public class ListViewDB {
	/**
	 * ���ݿ�汾
	 */
	public static final String DB_NAME = "cool_weather";

	/**
	 * ���ݿ�汾
	 */
	public static final int VERSION = 1;
	
	private SQLiteDatabase db;
	
	public ListViewDB(){
		// ʵ����CoolWeatherOpenHelper����,�����������ݿ�
		CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(MyApplication.getContext(),
				DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase(); // ������ݿ� ��д��ķ�ʽ
	}
	
	/**
	 * ɾ�����ݿ������е�DayWeather�����Ϣ
	 */
	public void deleteAllInfo(){
		db.delete("DayWeather", null, null);   //�洢֮ǰ,��ɾ�������������������
	}
	
	/**
	 * ��DayWeatherʵ���洢�����ݿ�
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
	 * �����ݿ��ж�ȡ3�������Ԥ��(DayWeather�ļ���)
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
