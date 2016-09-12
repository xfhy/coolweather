package com.coolweather.app.model;

import com.coolweather.app.util.MyApplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 2016年9月11日13:24:37
 * 
 * 配置 设置里面的东西
 * 
 * @author XFHY
 * 
 * 配置信息是保存到SharedPreferences文件中的
 * 
 */
public class Configure {

	/**
	 * 更新频率
	 */
	private static int UpdateFrequency;

	/**
	 * 是否更新
	 */
	private static boolean WhetherToUpdate;

	/**
	 * 设置更新频率
	 * @param updateFrequency
	 */
	public static void setUpdateFrequency(int updateFrequency) {
		//得到SharedPreferences文件
		SharedPreferences config = MyApplication.getContext()
				.getSharedPreferences("config", Context.MODE_PRIVATE);
		//得到Editor对象
		Editor editor = config.edit(); 
		//封装数据到Editor对象中
		editor.putInt("update_rate", updateFrequency);
		//提交数据
		editor.commit();
	}
	
	/**
	 * 返回更新频率
	 * @return
	 */
	public static int getUpdateFrequency() {
		// 得到SharedPreferences文件   文件名称,打开方式(这里是private私有)
		SharedPreferences config = MyApplication.getContext()
				.getSharedPreferences("config", Context.MODE_PRIVATE);
		//从SharedPreferences文件中获取更新频率
		UpdateFrequency = config.getInt("update_rate", 8);
		return UpdateFrequency;
	}
	
	/**
	 * 设置是否更新
	 * 
	 * @param whetherToUpdate
	 */
	public static void setWhetherToUpdate(boolean whetherToUpdate) {
		// 得到SharedPreferences文件
		SharedPreferences config = MyApplication.getContext()
				.getSharedPreferences("config", Context.MODE_PRIVATE);
		Editor editor = config.edit();
		editor.putBoolean("whether_to_update", whetherToUpdate);
		editor.commit();
	}
	
	/**
	 * 返回是否更新
	 * @return
	 */
	public static boolean getWhetherToUpdate() {
		SharedPreferences config = MyApplication.getContext()
				.getSharedPreferences("config", Context.MODE_PRIVATE);
		WhetherToUpdate = config.getBoolean("whether_to_update", true);
		return WhetherToUpdate;
	}
	
}
