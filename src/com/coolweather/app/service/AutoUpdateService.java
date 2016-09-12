package com.coolweather.app.service;

import com.coolweather.app.model.Configure;
import com.coolweather.app.receiver.AutoUpdateReceiver;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * 2016年8月26日19:50:59
 *  这个服务用于在后台自动更新天气
 *  
 * @author XFHY
 * 
 * 服务的生命周期:
 * Call to startService() -> onCreate() -> OnStartCommand() -> Service running ->
 *    onDestroy() -> Service shut down
 * 
 * 原理:每隔8小时就启动一次广播,广播立刻又启动这个服务    保证AutoUpdateService每8小时就会启动一次  在AutoUpdateService
 * 里写一个子线程,在子线程里面更新天气信息.将服务器返回的JSON数据解析并存储到SharedPerfenerces文件中.当用户下次启动显示天气的活动时
 * ,是直接读取的SharedPerfenerces文件中的数据的.所以,完成了天气的更新.
 * 
 * 服务需要去注册
 * 
 */
public class AutoUpdateService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//Log.d("xfhy", "服---------------------------务");
		
		// 判断用户是否设置了自动更新     设置了更新才去更新天气,否则就直接继续下面的增加时间
		if (Configure.getWhetherToUpdate()) {
			// 开启子线程,去调用更新天气的方法
			new Thread(new Runnable() {

				@Override
				public void run() {
					Log.d("xfhy", "开始更新天气");
					updateWeather();   //开始更新天气
				}

			}).start();
		}

		// 定时管理的类
		AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
		int anHour = Configure.getUpdateFrequency() * 60 * 60 * 1000; // 这是X小时的毫秒数
		long triggerAtTime = SystemClock.elapsedRealtime() + anHour; // 获得系统开机至今所经历的毫秒数
		// 启动AutoUpdateReceiver这个广播
		Intent i = new Intent(this, AutoUpdateReceiver.class);
		/*
		 * 每隔x小时就启动一次广播,广播立刻又启动这个服务 保证AutoUpdateService每x小时就会启动一次
		 * 参数:1.Context,2.一般用不到,传入0即可,3.Intent对象,通过这个对象可以构建出PendingIntent的"意图".
		 * 4.用于确定PendingIntent的行为
		 */
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
		// 参数:1.ELAPSED_REALTIME_WAKEUP表示开机至今的时间再加上延迟执行的时间,2.定时任务触发的时间,3.PendingIntent
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
		return super.onStartCommand(intent, flags, startId);
	}
	

	/**
	 * 更新天气信息
	 */
	private void updateWeather() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String weatherCode = prefs.getString("weather_code","");
		String address = "http://wthrcdn.etouch.cn/weather_mini?citykey="+weatherCode;
		
		//判断一下是否有网络连接
		if(HttpUtil.isNetworkAvailable()){
			HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
				
				@Override
				public void onFinish(String response) {
					//将服务器返回的天气数据交给Utility的handleWeatherResponse()方法去处理
					//解析服务器返回的JSON数据,并将解析出来的数据存储到本地
					Utility.handleWeatherResponse(AutoUpdateService.this, response);
				}
				
				@Override
				public void onError(Exception e) {
					e.printStackTrace();
				}
			});
		}
		
	}
	
}
