package com.coolweather.app.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

/**
 * 2016年8月28日8:45:01
 * 这个类用于专门对所有的活动进行管理
 * @author XFHY
 *
 */
public class ActivityCollector {
	//这个集合用于装所有的活动
	public static List<Activity> activities = new ArrayList<Activity>();  
	
	/**
	 * 添加活动到这个管理活动的集合
	 * @param activity  需要添加的活动
	 */
	public static void addActivity(Activity activity){
		activities.add(activity);
	}
	
	/**
	 * 从管理活动的集合中移除这个activity
	 * @param activity 需要移除的activity
	 */
	public static void removeActivity(Activity activity){
		activities.remove(activity);
	}
	
	/**
	 * finish所有的活动
	 */
	public static void finishAll(){
		for(Activity activity : activities){
			if(!activity.isFinishing()){
				activity.finish();
			}
		}
	}
	
	/**
	 * 获取当前在活动栈里面的活动的数量
	 * @return 当前在活动栈里面的活动的数量
	 */
	public static int activityCount(){
		return activities.size();
	}
	
}
