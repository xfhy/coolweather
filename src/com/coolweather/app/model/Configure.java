package com.coolweather.app.model;

import com.coolweather.app.util.MyApplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 2016��9��11��13:24:37
 * 
 * ���� ��������Ķ���
 * 
 * @author XFHY
 * 
 * ������Ϣ�Ǳ��浽SharedPreferences�ļ��е�
 * 
 */
public class Configure {

	/**
	 * ����Ƶ��
	 */
	private static int UpdateFrequency;

	/**
	 * �Ƿ����
	 */
	private static boolean WhetherToUpdate;

	/**
	 * ���ø���Ƶ��
	 * @param updateFrequency
	 */
	public static void setUpdateFrequency(int updateFrequency) {
		//�õ�SharedPreferences�ļ�
		SharedPreferences config = MyApplication.getContext()
				.getSharedPreferences("config", Context.MODE_PRIVATE);
		//�õ�Editor����
		Editor editor = config.edit(); 
		//��װ���ݵ�Editor������
		editor.putInt("update_rate", updateFrequency);
		//�ύ����
		editor.commit();
	}
	
	/**
	 * ���ظ���Ƶ��
	 * @return
	 */
	public static int getUpdateFrequency() {
		// �õ�SharedPreferences�ļ�   �ļ�����,�򿪷�ʽ(������private˽��)
		SharedPreferences config = MyApplication.getContext()
				.getSharedPreferences("config", Context.MODE_PRIVATE);
		//��SharedPreferences�ļ��л�ȡ����Ƶ��
		UpdateFrequency = config.getInt("update_rate", 8);
		return UpdateFrequency;
	}
	
	/**
	 * �����Ƿ����
	 * 
	 * @param whetherToUpdate
	 */
	public static void setWhetherToUpdate(boolean whetherToUpdate) {
		// �õ�SharedPreferences�ļ�
		SharedPreferences config = MyApplication.getContext()
				.getSharedPreferences("config", Context.MODE_PRIVATE);
		Editor editor = config.edit();
		editor.putBoolean("whether_to_update", whetherToUpdate);
		editor.commit();
	}
	
	/**
	 * �����Ƿ����
	 * @return
	 */
	public static boolean getWhetherToUpdate() {
		SharedPreferences config = MyApplication.getContext()
				.getSharedPreferences("config", Context.MODE_PRIVATE);
		WhetherToUpdate = config.getBoolean("whether_to_update", true);
		return WhetherToUpdate;
	}
	
}
