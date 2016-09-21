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
 * 2016��8��20��10:55:36
 * 
 * ������һЩ���õ����ݿ������װ����,�Է������ʹ��
 * 
 * @author XFHY
 * 
 */
public class CoolWeatherDB {

	/**
	 * ���ݿ�汾
	 */
	public static final String DB_NAME = "cool_weather";

	/**
	 * ���ݿ�汾
	 */
	public static final int VERSION = 1;

	private static CoolWeatherDB coolWeatherDB;

	private SQLiteDatabase db;

	/**
	 * �����췽��˽�л� ���Ҫ����һ����������������һ����ֻ����һ��ʵ��������
	 * һʵ��������϶�Ҫ���ù��췽������������췽�������������ⲿ�϶��޷�ֱ�ӵ��ã��Ϳ϶�������new�ؼ��ֵ��ù��췽��ʵ����
	 * 
	 * @param context
	 */
	private CoolWeatherDB(Context context) {
		// ʵ����CoolWeatherOpenHelper����,�����������ݿ�
		CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context,
				DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase(); // ������ݿ� ��д��ķ�ʽ
	}

	/**
	 * ��ȡCoolWeatherDBʵ�� synchronized : ������ͬ����һ��ʱ��ֻ����һ���̵߳õ�ִ��
	 * 
	 * @param context
	 * @return ����CoolWeatherDBʵ��
	 */
	public synchronized static CoolWeatherDB getInstance(Context context) {
		if (coolWeatherDB == null) { // ��coolWeatherDBΪnullʱ�ʹ���ʵ��,new
										// һ������,������Է����Լ���˽�й��췽��
			coolWeatherDB = new CoolWeatherDB(context);
		}
		return coolWeatherDB; // ����CoolWeatherDBʵ��
	}

	/**
	 * ��Provinceʵ���洢�����ݿ�
	 * 
	 * @param province
	 *            Provinceʵ��
	 */
	public void saveProvince(Province province) {
		if (province != null) {
			ContentValues values = new ContentValues(); // ���ڷ�װ����
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			db.insert("Province", null, values); // ���ContentValues��װ���˵����ݵ����ݿ�
		}
	}

	/**
	 * �����ݿ��ȡȫ�����е�ʡ����Ϣ
	 * @return  ����List<Province>�б�
	 */
	public List<Province> loadProvinces() {
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db
				.query("Province", null, null, null, null, null, null); // ��ѯ

		//moveToFirst:Move the cursor to the first row.    ���أ�whether the move succeeded
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
				list.add(province);   //��ӵ�ǰProvince����List�б���
			}while(cursor.moveToNext());   //moveToNext():�ƶ�����һ��
		}
		
		if(cursor != null){  //ע��,�����Ҫ��Cursor����ر�,�ر�ǰ��Ҫ�ж�cursor�����Ƿ���null
			cursor.close();
		}

		return list;
	}

	/**
	 * �����ݿ��ж�ȡȫ������ʡ�ݵ�(id)����
	 * @return ����ʡ�����ŵļ���List<String>
	 */
	public List<String> loadProvincesId(){
		List<String> provincesIdList = new ArrayList<String>();
		Cursor cursor = db.query("Province", null, null, null, null, null, null);  // ��ѯ
		
		//moveToFirst:Move the cursor to the first row.    ���أ�whether the move succeeded
		if(cursor.moveToFirst()){
			do{
				String province_code;  //ʡ������
				province_code = cursor.getString(cursor.getColumnIndex("province_code"));
				provincesIdList.add(province_code);
				LogUtil.d("xfhy","��ȡ loadProvincesId() ->  ʡ������"+province_code);
			}while(cursor.moveToNext());   //�Ƿ�����һ��
		}
		if(cursor != null){  //ע��,�����Ҫ��Cursor����ر�,�ر�ǰ��Ҫ�ж�cursor�����Ƿ���null
			cursor.close();
		}
		return provincesIdList;
	}
	
	/**
	 * ��Cityʵ���洢�����ݿ�
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
	 * �����ݿ��ȡ��ʡ�ݵ����г���
	 * @return
	 */
	public List<City> loadCities(int provinceId){
		List<City> list = new ArrayList<City>();
		/*
		 *query():����:  ����,���ص���,where����,ǰ���?ռλ����ֵ,groupBy ,having ,orderBy  
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
		if(cursor != null){  //ע��,�����Ҫ��Cursor����ر�,�ر�ǰ��Ҫ�ж�cursor�����Ƿ���null
			cursor.close();
		}
		return list;
	}
	
	/**
	 * �����ݿ��ж�ȡ��ʡ�����г���id
	 * @provinceId ��Ҫ��ѯ���г���id��ʡ
	 * @return ���ظ�ʡ���д��ŵļ���
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
		 * query():����: ����,���ص���,where����,ǰ���?ռλ����ֵ,groupBy ,having ,orderBy
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
	 * ��Countyʵ���洢�����ݿ�
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
	 * �����ݿ��ж�ȡĳ�����µ������ص���Ϣ
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
	 * �����ݿ��ж�ȡ�ó����µ������ص�id��Ϣ
	 * @param cityId ��Ҫ��ȡ�����صĳ���id
	 * @return ���غ��иó���������id�ļ���List<String>
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
	 * ����ʡ���������ݿ��ѯ��ʡ��id,�����id���浽Position��
	 * 
	 */
	public void provinceNameToId(String provinceName){
		//ȥ��provinceName�����ʡ��
		provinceName = provinceName.substring(0,provinceName.length()-1);
		String provinceId = null;
		Cursor cursor = db.query("Province", null, "province_name = ?", new String[]{provinceName},
				null, null, null);
		
		if(cursor.moveToFirst()){ 
			do{
				provinceId = cursor.getString(cursor.getColumnIndex("province_code"));
			}while(cursor.moveToNext());
		}
		if(cursor != null){  //�ر�Cursor����
			cursor.close();
		}
		if(provinceId != null){   //���õ�ǰ��ʡ��id
			LogUtil.d("position","ʡ��id -> "+provinceId);
			Position.setProvinceId(provinceId);    //���õ�ǰ��ʡ��id
		}
		
	}
	
	/**
	 * ���ݳ����������ݿ��ѯ�ó���id,�����id���浽Position��
	 * @param cityName
	 */
	public void cityNameToId(String cityName){
		//ȥ��cityName��������      �Ӱٶȵ�ͼ��ȡ���ĵ�ǰ��������������"�ɶ���",���ֵ�,�����ݿ��д洢����"�ɶ�"
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
			LogUtil.d("position","����id -> "+cityId);
			Position.setCityId(cityId);   //���õ�ǰ�ĳ���id
		}
	}
	
	/**
	 * ���������������ݿ��ѯ������id,�����id���浽Position��
	 * @param countyName
	 */
	public void countyNameToId(String countyName){
		if(countyName.contains("��")){
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
			LogUtil.d("position","����id -> "+countyId);
			Position.setCountyId(countyId);
		}
	}
	
}
