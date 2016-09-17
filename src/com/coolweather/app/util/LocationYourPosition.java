package com.coolweather.app.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * 定位自己的位置
 * 
 * @author XFHY
 * 
 */
public class LocationYourPosition {
	/**
	 * LocationManager对象
	 */
	public static LocationManager locationManager;
	/**
	 * 位置提供器
	 */
	public static String provider;

	/**
	 * 服务器返回的数据
	 */
	public static StringBuilder resultData = new StringBuilder();
	
	/**
	 * 纬度
	 */
	public static String latitude;
	
	/**
	 * 经度
	 */
	public static String longitude;
	
	/**
	 * 构造方法
	 */
	public LocationYourPosition() {
	}

	/**
	 * 初始化位置,获取经纬度 判断是否手机已开启GPS或数据网络
	 */
	public static void getLocationXY() {
		// 获取到LocationManager的实例
		locationManager = (LocationManager) MyApplication.getContext()
				.getSystemService(Context.LOCATION_SERVICE);
		// 获取所有可用的位置提供器
		List<String> providerList = locationManager.getProviders(true);
		if (providerList.contains(LocationManager.GPS_PROVIDER)) {
			provider = LocationManager.GPS_PROVIDER;
			//Log.d("xfhy", "已开启GPS");
		} else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
			provider = LocationManager.NETWORK_PROVIDER;
			//Log.d("xfhy", "已开启NETWORK_PROVIDER");
		} else {
			// 当没有可用的位置提供器时,弹出Toast提示用户
			Toast.makeText(MyApplication.getContext(), "亲,请确认已开启GPS或数据网络",
					Toast.LENGTH_LONG).show();
			return;
		}

		// 将选择好的位置提供器传入getLastKnownLocation()中即可得到一个Location对象
		// 该对象已包含经度,纬度,海拔等一系列信息
		Location location = locationManager.getLastKnownLocation(provider);
		if (location != null) {
			latitude = String.valueOf(location.getLatitude());
			longitude = String.valueOf(location.getLongitude());
			showLocation(location);
		} else {
			Toast.makeText(MyApplication.getContext(), "亲,定位失败~请到开阔的地方",
					Toast.LENGTH_LONG).show();
		}

		// 添加位置监听器 参数:位置提供器,时间间隔,位置间隔,监听器
		locationManager.requestLocationUpdates(provider, 5000, 1,
				locationListener);
	}

	// 实例化 位置监听器
	static LocationListener locationListener = new LocationListener() {

		// 当位置发生改变时
		@Override
		public void onLocationChanged(Location location) {
			Toast.makeText(MyApplication.getContext(), "亲,位置已经变化啦",
					Toast.LENGTH_LONG).show();
			latitude = String.valueOf(location.getLatitude());
			longitude = String.valueOf(location.getLongitude());
			// 自己写的方法,显示当前位置
			showLocation(location);
			//getDistrict();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		@Override
		public void onProviderEnabled(String provider) {

		}

		@Override
		public void onProviderDisabled(String provider) {

		}

	};

	/**
	 * 显示当前位置信息
	 * 
	 * @param location
	 */
	private static void showLocation(Location location) {
		String currentPosition = "当前的位置是  纬度:" + location.getLatitude() + "\n"
				+ "经度:" + location.getLongitude();
		//Log.d("xfhy", "纬度:" + location.getLatitude());
		//Log.d("xfhy", "经度:" + location.getLongitude());
		Toast.makeText(MyApplication.getContext(), currentPosition,
				Toast.LENGTH_SHORT).show();
	}

	/**
	 * 关闭位置监听器
	 */
	public static void onDestroy() {
		if (locationManager != null) {
			// 关闭监听器
			locationManager.removeUpdates(locationListener);
		}
	}

	/**
	 * 获取当前的区县名     解析服务器返回的json数据
	 * @return 
	 */
	public static String getDistrict(String reallyResultData) {
		Log.d("xfhy","getDistrict()");
		String district  = null;
		//原始返回的数据中有renderReverse&&renderReverse(....)   需要将外面的杂质其去掉
		try {
			int indexOfSmall1 = reallyResultData.indexOf("(");
			int indexOfSmall2 = reallyResultData.lastIndexOf(")");
			Log.d("xfhy","indexOfSmall1 :"+indexOfSmall1);
			Log.d("xfhy","indexOfSmall2 :"+indexOfSmall2);
			reallyResultData = reallyResultData.substring(indexOfSmall1+1, indexOfSmall2);
			Log.d("xfhy","reallyResultData :"+reallyResultData);
			JSONObject jsonObject = new JSONObject(reallyResultData);
			JSONObject jsonResult = jsonObject.getJSONObject("result");
			JSONObject jsonAddressComponent = jsonResult.getJSONObject("addressComponent");
			district = jsonAddressComponent.getString("district");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.d("xfhy","县区 : "+district);
		return district;
	}
	
	/**
	 * 从服务器端获取当前位置json数据
	 * @param httpUrl
	 */
	public static void getRequest(final HttpCallbackListener listener){
		// ak IOOZbu8un3e9laZTXCtYqoAOoZHdMft4
		/*
		 * http://api.map.baidu.com/geocoder/v2/?
		 * ak=IOOZbu8un3e9laZTXCtYqoAOoZHdMft4& mcode=1A:2C:32:
		 * 7E:1B:9C:16:63:B5:F3:7A:AB:03:E3:62:0B:BF:05:F6:6E;com.coolweather.app.util&
		 * callback=renderReverse& location=39.983424,116.322987& output=json&
		 * pois=1
		 */
		//这是
		final String httpUrl = "http://api.map.baidu.com/geocoder/v2/?"
				+ "ak=IOOZbu8un3e9laZTXCtYqoAOoZHdMft4&"
				+ "mcode=1A:2C:32:7E:1B:9C:16:63:B5:F3:7A:AB:03:E3:62:0B:BF:05:F6:6E;com.coolweather.app.util&"
				+ "callback=renderReverse&location=" + latitude + ","
				+ longitude + "&output=json&pois=1";
		
		new Thread(new Runnable(){

			@Override
			public void run() {
				HttpURLConnection connection = null;
				try {
					URL url = new URL(httpUrl);
					connection = (HttpURLConnection)url.openConnection();
					connection.setRequestMethod("GET");    //设置URL请求的方法
					connection.setConnectTimeout(8000);    //设置一个连接指定的超时值（以毫秒为单位）
					connection.setReadTimeout(8000);       //将读超时设置为指定的超时值，以毫秒为单位
					
					//服务器返回的数据是以输入流的方式
					InputStream in = connection.getInputStream();
					//将字节流转换成字符流
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					
					String line = "";
					while( (line=reader.readLine()) != null ){  //读取服务器的数据,一行一行地读
						resultData.append(line);
					}
					Log.d("xfhy","resultData :"+resultData.toString());
					
					//将服务器返回的数据放到了HttpCallbackListener实例的onFinish()中,java的回调机制会让调用这个方法的类获取到数据
					if(listener != null){
						listener.onFinish(resultData.toString());
					}
				} catch (Exception e) {
					e.printStackTrace();
					//将连接服务器时的错误信息放到HttpCallbackListener的实例的onError()中,
					//java的回调机制会让调用sendHttpRequest()这个方法的类获取到数据
					if( listener != null ){
						listener.onError(e);
					}
				} finally {
					//最后,记得断开
					if(connection != null){
						connection.disconnect();
					}
				}
			}
			
		}).start();
		
	}
	
}
