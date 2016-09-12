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
 * 2016��8��26��19:50:59
 *  ������������ں�̨�Զ���������
 *  
 * @author XFHY
 * 
 * �������������:
 * Call to startService() -> onCreate() -> OnStartCommand() -> Service running ->
 *    onDestroy() -> Service shut down
 * 
 * ԭ��:ÿ��8Сʱ������һ�ι㲥,�㲥�����������������    ��֤AutoUpdateServiceÿ8Сʱ�ͻ�����һ��  ��AutoUpdateService
 * ��дһ�����߳�,�����߳��������������Ϣ.�����������ص�JSON���ݽ������洢��SharedPerfenerces�ļ���.���û��´�������ʾ�����Ļʱ
 * ,��ֱ�Ӷ�ȡ��SharedPerfenerces�ļ��е����ݵ�.����,����������ĸ���.
 * 
 * ������Ҫȥע��
 * 
 */
public class AutoUpdateService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//Log.d("xfhy", "��---------------------------��");
		
		// �ж��û��Ƿ��������Զ�����     �����˸��²�ȥ��������,�����ֱ�Ӽ������������ʱ��
		if (Configure.getWhetherToUpdate()) {
			// �������߳�,ȥ���ø��������ķ���
			new Thread(new Runnable() {

				@Override
				public void run() {
					Log.d("xfhy", "��ʼ��������");
					updateWeather();   //��ʼ��������
				}

			}).start();
		}

		// ��ʱ�������
		AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
		int anHour = Configure.getUpdateFrequency() * 60 * 60 * 1000; // ����XСʱ�ĺ�����
		long triggerAtTime = SystemClock.elapsedRealtime() + anHour; // ���ϵͳ���������������ĺ�����
		// ����AutoUpdateReceiver����㲥
		Intent i = new Intent(this, AutoUpdateReceiver.class);
		/*
		 * ÿ��xСʱ������һ�ι㲥,�㲥����������������� ��֤AutoUpdateServiceÿxСʱ�ͻ�����һ��
		 * ����:1.Context,2.һ���ò���,����0����,3.Intent����,ͨ�����������Թ�����PendingIntent��"��ͼ".
		 * 4.����ȷ��PendingIntent����Ϊ
		 */
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
		// ����:1.ELAPSED_REALTIME_WAKEUP��ʾ���������ʱ���ټ����ӳ�ִ�е�ʱ��,2.��ʱ���񴥷���ʱ��,3.PendingIntent
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
		return super.onStartCommand(intent, flags, startId);
	}
	

	/**
	 * ����������Ϣ
	 */
	private void updateWeather() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String weatherCode = prefs.getString("weather_code","");
		String address = "http://wthrcdn.etouch.cn/weather_mini?citykey="+weatherCode;
		
		//�ж�һ���Ƿ�����������
		if(HttpUtil.isNetworkAvailable()){
			HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
				
				@Override
				public void onFinish(String response) {
					//�����������ص��������ݽ���Utility��handleWeatherResponse()����ȥ����
					//�������������ص�JSON����,�����������������ݴ洢������
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
