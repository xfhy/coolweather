package com.coolweather.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 2016年8月20日9:36:17
 * 用于操作CoolWeather的数据库
 * @author XFHY
 *
 */
public class CoolWeatherOpenHelper extends SQLiteOpenHelper {

	/**
	 * Province表建表语句    省份
	 * id主键,自增
	 * province_name:省份名
	 * province_code:省份代号
	 */
	public static final String CREATE_PROVINCE = "create table Province (" +
			"id integer primary key autoincrement, " +
			"province_name text, " +
			"province_code text, " +
			"unique (province_code), " +
			"unique (province_name))";
	
	/**
	 * City表建表语句   城市
	 * id:主键,自增
	 * city_name :城市名
	 * city_code :城市代号
	 * province_id :City表关联Province表的外键
	 */
	public static final String CREATE_CITY = "create table City (" +
			"id integer primary key autoincrement, " +
			"city_name text, " +
			"city_code text, " +
			"province_id integer, " +
			"unique (city_name), " +
			"unique (city_code) )";
	
	/**
	 * County表建表语句  县级
	 * id:主键,自增
	 * county_name :县名
	 * county_code :县代号
	 * city_id :County表关联City表的外键
	 */
	public static final String CREATE_COUNTY = "create table County (" +
			"id integer primary key autoincrement, " +
			"county_name text, " +
			"county_code text, " +
			"city_id integer, " +
			"unique (county_name), " +
			"unique (county_code) )";
	
	/**
	 * DayWeather表建表语句   用于显示(3天的天气预报)ListView的子项的
	 * day_date : 日期:号数
	 * weather_type : 天气类型
	 * low_temp : 最低温度
	 * high_temp : 最高温度
	 */
	public static final String CREATE_DAYWEATHER = "create table DayWeather (" +
			"id integer primary key autoincrement, " +
			"day_date text, " +
			"weather_type text, " +
			"low_temp text, " +
			"high_temp text)";
	
	//构造方法  参数:Context,,数据库名,查询数据的时候返回一个自定义的Cursor(一般传入null),数据库版本号
	public CoolWeatherOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//创建数据库的同时即创建表
		db.execSQL(CREATE_PROVINCE);  //创建Province表
		db.execSQL(CREATE_CITY);      //创建City表
		db.execSQL(CREATE_COUNTY);    //创建County表
		db.execSQL(CREATE_DAYWEATHER);    //创建DayWeather表
	}

	//更新数据库时调用
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
