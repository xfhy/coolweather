package com.coolweather.app.db;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Position;
import com.coolweather.app.model.Province;
import com.coolweather.app.util.LogUtil;

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
	 * 从数据库中读取全国所有省份的(id)代号
	 * @return 返回省级代号的集合List<String>
	 */
	public List<String> loadProvincesId(){
		List<String> provincesIdList = new ArrayList<String>();
		Cursor cursor = db.query("Province", null, null, null, null, null, null);  // 查询
		
		//moveToFirst:Move the cursor to the first row.    返回：whether the move succeeded
		if(cursor.moveToFirst()){
			do{
				String province_code;  //省级代号
				province_code = cursor.getString(cursor.getColumnIndex("province_code"));
				provincesIdList.add(province_code);
				LogUtil.d("xfhy","读取 loadProvincesId() ->  省级代号"+province_code);
			}while(cursor.moveToNext());   //是否还有下一个
		}
		if(cursor != null){  //注意,最后需要把Cursor对象关闭,关闭前需要判断cursor对象是否是null
			cursor.close();
		}
		return provincesIdList;
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
		if(cursor != null){  //注意,最后需要把Cursor对象关闭,关闭前需要判断cursor对象是否是null
			cursor.close();
		}
		return list;
	}
	
	/**
	 * 从数据库中读取该省份所有城市id
	 * @provinceId 需要查询所有城市id的省
	 * @return 返回该省城市代号的集合
	 */
	public List<String> loadCityId(int provinceId) {
		List<String> cityIdList = new ArrayList<String>();
		String provinceIdTemp = "";
		if(provinceId >0 && provinceId <= 9){
			provinceIdTemp = "0"+String.valueOf(provinceId);
		}else{
			provinceIdTemp = String.valueOf(provinceId);
		}
		/*
		 * query():参数: 表名,返回的列,where部分,前面的?占位符的值,groupBy ,having ,orderBy
		 */
		Cursor cursor = db.query("City", null, "province_id = ?",
				new String[] { provinceIdTemp }, null, null, null);
		if(cursor.moveToFirst()){
			do{
				String cityId;
				cityId = cursor.getString(cursor.getColumnIndex("city_code"));
				cityIdList.add(cityId);
			}while(cursor.moveToNext());
		}
		return cityIdList;
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
	
	/**
	 * 从数据库中读取该城市下的所有县的id信息
	 * @param cityId 需要读取所有县的城市id
	 * @return 返回含有该城市所有县id的集合List<String>
	 * 9.18
	 */
	public List<String> loadCountiesId(String cityId){
		List<String> countiesIdList = new ArrayList<String>();
		Cursor cursor = db.query("County", null, "city_id = ?", new String[]{cityId},
				null, null, null);
		if(cursor.moveToFirst()){
			do{
				String countyId;
				countyId = cursor.getString(cursor.getColumnIndex("county_code"));
				countiesIdList.add(countyId);
			}while(cursor.moveToNext());
		}
		if(cursor != null){
			cursor.close();
		}
		return countiesIdList;
	}
	
	/**
	 * 根据省份名从数据库查询该省份id,将这个id保存到Position中
	 * 
	 */
	public void provinceNameToId(String provinceName){
		//去掉provinceName后面的省字
		provinceName = provinceName.substring(0,provinceName.length()-1);
		String provinceId = null;
		Cursor cursor = db.query("Province", null, "province_name = ?", new String[]{provinceName},
				null, null, null);
		
		if(cursor.moveToFirst()){ 
			do{
				provinceId = cursor.getString(cursor.getColumnIndex("province_code"));
			}while(cursor.moveToNext());
		}
		if(cursor != null){  //关闭Cursor对象
			cursor.close();
		}
		if(provinceId != null){   //设置当前的省份id
			LogUtil.d("position","省份id -> "+provinceId);
			Position.setProvinceId(provinceId);    //设置当前的省份id
		}
		
	}
	
	/**
	 * 根据城市名从数据库查询该城市id,将这个id保存到Position中
	 * @param cityName
	 */
	public void cityNameToId(String cityName){
		//去掉cityName最后的市字      从百度地图获取到的当前城市名是类似于"成都市",这种的,而数据库中存储的是"成都"
		cityName = cityName.substring(0,cityName.length()-1);
		String cityId = null;
		Cursor cursor = db.query("City", null, "city_name=?", new String[]{cityName},
				null, null, null);
		if(cursor.moveToFirst()){
			do{
				cityId = cursor.getString(cursor.getColumnIndex("city_code"));
			}while(cursor.moveToNext());
		}
		if(cursor != null){
			cursor.close();
		}
		if(cityId != null){
			LogUtil.d("position","城市id -> "+cityId);
			Position.setCityId(cityId);   //设置当前的城市id
		}
	}
	
	/**
	 * 根据县区名从数据库查询该县区id,将这个id保存到Position中
	 * @param countyName
	 */
	public void countyNameToId(String countyName){
		if(countyName.contains("区")){
			countyName = countyName.substring(0,countyName.length()-1);
		}
		String countyId = null;
		Cursor cursor = db.query("County", null, "county_name=?", new String[]{countyName},
				null, null, null);
		if(cursor.moveToFirst()){
			do{
				countyId = cursor.getString(cursor.getColumnIndex("county_code"));
			}while(cursor.moveToNext());
		}
		if(cursor != null){
			cursor.close();
		}
		if(countyId != null){
			LogUtil.d("position","县区id -> "+countyId);
			Position.setCountyId(countyId);
		}
	}
	
}
