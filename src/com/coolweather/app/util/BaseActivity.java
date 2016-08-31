package com.coolweather.app.util;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * 2016年8月28日10:38:07
 * 
 * 这个类继承自Activity,让所有的活动类都继承自这个类
 * 这样有个好处,就是可以查看当前是在哪一个活动中,也方便管理活动类
 * @author XFHY
 *
 */
public class BaseActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.d("xfhy",getClass().getSimpleName());  //打印当前活动的名称
		//添加这个活动到活动管理器
		ActivityCollector.addActivity(this);  
	}
	
	//销毁时,即从活动管理器中移除当前活动
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}
	
}
