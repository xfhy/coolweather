package com.coolweather.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 2016��8��20��9:36:17
 * ���ڲ���CoolWeather�����ݿ�
 * @author XFHY
 *
 */
public class CoolWeatherOpenHelper extends SQLiteOpenHelper {

	/**
	 * Province�������    ʡ��
	 * id����,����
	 * province_name:ʡ����
	 * province_code:ʡ�ݴ���
	 */
	public static final String CREATE_PROVINCE = "create table Province (" +
			"id integer primary key autoincrement, " +
			"province_name text, " +
			"province_code text)";
	
	/**
	 * City�������   ����
	 * id:����,����
	 * city_name :������
	 * city_code :���д���
	 * province_id :City�����Province������
	 */
	public static final String CREATE_CITY = "create table City (" +
			"id integer primary key autoincrement, " +
			"city_name text, " +
			"city_code text, " +
			"province_id integer)";
	
	/**
	 * County�������  �ؼ�
	 * id:����,����
	 * county_name :����
	 * county_code :�ش���
	 * city_id :County�����City������
	 */
	public static final String CREATE_COUNTY = "create table County (" +
			"id integer primary key autoincrement, " +
			"county_name text, " +
			"county_code text, " +
			"city_id integer)";
	
	//���췽��  ����:Context,,���ݿ���,��ѯ���ݵ�ʱ�򷵻�һ���Զ����Cursor(һ�㴫��null),���ݿ�汾��
	public CoolWeatherOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//�������ݿ��ͬʱ��������
		db.execSQL(CREATE_PROVINCE);  //����Province��
		db.execSQL(CREATE_CITY);      //����City��
		db.execSQL(CREATE_COUNTY);    //����County��
	}

	//�������ݿ�ʱ����
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
