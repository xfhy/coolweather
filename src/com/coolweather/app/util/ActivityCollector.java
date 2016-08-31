package com.coolweather.app.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

/**
 * 2016��8��28��8:45:01
 * ���������ר�Ŷ����еĻ���й���
 * @author XFHY
 *
 */
public class ActivityCollector {
	//�����������װ���еĻ
	public static List<Activity> activities = new ArrayList<Activity>();  
	
	/**
	 * ��ӻ����������ļ���
	 * @param activity  ��Ҫ��ӵĻ
	 */
	public static void addActivity(Activity activity){
		activities.add(activity);
	}
	
	/**
	 * �ӹ����ļ������Ƴ����activity
	 * @param activity ��Ҫ�Ƴ���activity
	 */
	public static void removeActivity(Activity activity){
		activities.remove(activity);
	}
	
	/**
	 * finish���еĻ
	 */
	public static void finishAll(){
		for(Activity activity : activities){
			if(!activity.isFinishing()){
				activity.finish();
			}
		}
	}
	
	/**
	 * ��ȡ��ǰ�ڻջ����Ļ������
	 * @return ��ǰ�ڻջ����Ļ������
	 */
	public static int activityCount(){
		return activities.size();
	}
	
}
