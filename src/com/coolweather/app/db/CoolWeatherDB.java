package com.coolweather.app.db;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 2016年8月20日10:55:36
 * 
 * 这个类把一些常用的数据库操作封装起来,以方便后面使用
 * 
 * @author XFHY
 * 
 */
public class CoolWeatherDB {

	/**
	 * 数据库版本
	 */
	public static final String DB_NAME = "cool_weather";

	/**
	 * 数据库版本
	 */
	public static final int VERSION = 1;

	private static CoolWeatherDB coolWeatherDB;

	private SQLiteDatabase db;

	/**
	 * 将构造方法私有化 如果要限制一个类对象产生，即：一个类只能有一个实例化对象，
	 * 一实例化对象肯定要调用构造方法，如果将构造方法藏起来，则外部肯定无法直接调用，就肯定不能用new关键字调用构造方法实例。
	 * 
	 * @param context
	 */
	private CoolWeatherDB(Context context) {
		// 实例化CoolWeatherOpenHelper对象,用来操作数据库
		CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context,
				DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase(); // 获得数据库 可写入的方式
	}

	/**
	 * 获取CoolWeatherDB实例 synchronized : 这里是同步的一个时间只能有一个线程得到执行
	 * 
	 * @param context
	 * @return 返回CoolWeatherDB实例
	 */
	public synchronized static CoolWeatherDB getInstance(Context context) {
		if (coolWeatherDB == null) { // 当coolWeatherDB为null时就创建实例,new
										// 一个出来,这里可以访问自己的私有构造方法
			coolWeatherDB = new CoolWeatherDB(context);
		}
		return coolWeatherDB; // 返回CoolWeatherDB实例
	}

	/**
	 * 将Province实例存储到数据库
	 * 
	 * @param province
	 *            Province实例
	 */
	public void saveProvince(Province province) {
		if (province != null) {
			ContentValues values = new ContentValues(); // 用于封装数据
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			db.insert("Province", null, values); // 添加ContentValues封装好了的数据到数据库
		}
	}

	/**
	 * 从数据库读取全国所有的省份信息
	 * @return  返回List<Province>列表
	 */
	public List<Province> loadProvinces() {
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db
				.query("Province", null, null, null, null, null, null); // 查询

		//moveToFirst:Move the cursor to the first row.    返回：whether the move succeeded
		if (cursor.moveToFirst()) {
			do{
				Province province = new Province();
				int id;
				String province_name;
				String province_code;
				
				/*getColumnIndex():Returns the zero-based index for the given column name, or -1 if the column
				*doesn't exist.
				*getString():Returns the value of the requested column as a String.
				*/
				id = cursor.getInt(cursor.getColumnIndex("id"));
				province_name = cursor.getString(cursor.getColumnIndex("province_name"));
				province_code = cursor.getString(cursor.getColumnIndex("province_code"));
				
				province.setId(id);
				province.setProvinceName(province_name); 
				province.setProvinceCode(province_code);
				list.add(province);   //添加当前Province对象到List列表中
			}while(cursor.moveToNext());   //moveToNext():移动到下一行
		}
		
		if(cursor != null){  //注意,最后需要把Cursor对象关闭,关闭前需要判断cursor对象是否是null
			cursor.close();
		}

		return list;
	}

	/**
	 * 将City实例存储到数据库
	 * @param city
	 */
	public void saveCity(City city){
		if(city != null){
			ContentValues values = new ContentValues();
			values.put("city_name",city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("province_id", city.getProvinceId());
			db.insert("City", null, values);
		}
	}
	
	/**
	 * 从数据库读取该省份的所有城市
	 * @return
	 */
	public List<City> loadCities(int provinceId){
		List<City> list = new ArrayList<City>();
		/*
		 *query():参数:  表名,返回的列,where部分,前面的?占位符的值,groupBy ,having ,orderBy  
		 */
		Cursor cursor = db.query("City", null, "province_id = ?",
				new String[]{String.valueOf(provinceId)}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				City city = new City();
				
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setProvinceId(provinceId);
				list.add(city);
			}while(cursor.moveToNext());
		}
		if(cursor != null){
			cursor.close();
		}
		return list;
	}
	
	/**
	 * 将County实例存储到数据库
	 * @param county
	 */
	public void saveCounty(County county){
		if(county != null){
			ContentValues values = new ContentValues();
			values.put("county_name", county.getCountyName());
			values.put("county_code", county.getCountyCode());
			values.put("city_id", county.getCityId());
			db.insert("County", null, values);
		}
	}
	
	/**
	 * 从数据库中读取某城市下的所有县的信息
	 * @param cityId
	 * @return
	 */
	public List<County> loadCounties(int cityId){
		List<County> list = new ArrayList<County>();
		Cursor cursor = db.query("County", null, "city_id = ?", 
				new String[]{String.valueOf(cityId)}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				County county = new County();
				county.setCityId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
				county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
				county.setCityId(cityId);
				list.add(county);
			}while(cursor.moveToNext());
		}
		if(cursor != null){
			cursor.close();
		}
		return list;
	}
	
}
